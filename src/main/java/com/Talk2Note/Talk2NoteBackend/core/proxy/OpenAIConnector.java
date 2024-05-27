package com.Talk2Note.Talk2NoteBackend.core.proxy;

import com.Talk2Note.Talk2NoteBackend.core.enums.OpenAIRoleType;
import com.Talk2Note.Talk2NoteBackend.core.objects.OpenAIRole;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;

@Service
public class OpenAIConnector {

    @Value("${openai.secret-key}")
    private String OPEN_AI_SECRET_KEY;
    @Value("${openai.route.completion-url}")
    private String OPENAI_API_COMPLETION_URL;
    @Value("${openai.model}")
    private String OPENAI_MODEL;
    @Value("${openai.max-tokens}")
    private int MAX_TOKENS;

    @Value("${openai.roles.raw-to-meaningful.description}")
    private String RAW_TO_MEANINGFUL_DESC;
    @Value("${openai.roles.raw-to-meaningful.temperature}")
    private float RAW_TO_MEANINGFUL_TEMP;

    @Value("${openai.roles.md-auto.description}")
    private String MD_AUTO_DESC;
    @Value("${openai.roles.md-auto.temperature}")
    private float MD_AUTO_TEMP;

    private RestTemplate restTemplate;
    private HashMap<OpenAIRoleType, OpenAIRole> openAIRoles = new HashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public OpenAIConnector(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;

        List<OpenAIRole> generatedRoles = List.of(
                OpenAIRole.builder()
                        .actorName("Raw to Meaningful Text Converter")
                        .roleType(OpenAIRoleType.RAW_TO_MEANINGFUL)
                        .description("I will understand the topic and general meaning of given input and make the following job based on the meaning and topic of input. I will correct spelling, word choice, and meaning errors without changing the overall meaning of the text. I will ensure the text retains its original meaning and correct any ambiguities or unclear expressions. Additionally, I will pay attention to punctuation and capitalization errors, ensuring they are corrected while preserving the original context and structure of the text. I will return the text in the same format and order as the input.")
                        .temperature(0.3f)
                        .build(),
                OpenAIRole.builder()
                        .actorName("MD Auto Text Converter")
                        .roleType(OpenAIRoleType.MD_AUTO)
                        .description("I am a Markdown expert and note-taker. My goal task is to convert the given input text into a creatively formatted Markdown document. I will ensure the output is clear, organized, and visually appealing. I will use appropriate Markdown elements such as headings, lists, links, bold, italics, blockquotes, code blocks, and more to enhance readability and maintain the structural integrity of the text. I will preserve the meaning of the content while formatting it effectively. For instance, I will use '# ' for titles and '##', '###', etc., for subheadings. I will use '**bold**' for important terms and '*italics*' for emphasis. I will create lists with - for unordered lists and '1.', '2.', etc., for ordered lists. Format links as '[text](url)', I will use '>' for blockquotes and comments, and backticks for inline code and triple backticks for code blocks. I will make sure the final output is clear, organized, and visually appealing.")
                        .temperature(0.5f)
                        .build()
        );

        for (OpenAIRole role: generatedRoles) {
            this.openAIRoles.put(role.getRoleType(), role);
        }

    }


    public String compose(String promptString, OpenAIRoleType roleType) throws IOException, InterruptedException {

        OpenAIRole selectedRole = this.openAIRoles.get(roleType);
        if (selectedRole == null) {
            return null;
        }

        JSONObject payload = new JSONObject();
        JSONArray messages = new JSONArray();

        JSONObject initialDefinition = new JSONObject();
        initialDefinition.put("role", "system");
        initialDefinition.put("content", "I'm a helpful text adjuster and note taker ai assistant.");

        JSONObject formatIntroduction = new JSONObject();
        formatIntroduction.put("role", "system");
        formatIntroduction.put("content", "When given a JSON input formatted as {\"1\": \"sample text\", \"2\": \"sample text\", \"3\": \"sample text\", \"4\": \"sample text\"}, you should process the text inside each key-value pair based on the given instructions and return the modified text in the exact same format and order. Make sure to keep the JSON structure intact and only modify the text content as per the instructions. Here are some examples of input, processing instructions, and expected output:\n" +
                "Instructions: Add 'Processed' before each text.\n" +
                "Input: {\"1\": \"sample text\", \"2\": \"sample text\", \"3\": \"sample text\", \"4\": \"sample text\"}\n" +
                "Output: {\"1\": \"Processed sample text\", \"2\": \"Processed sample text\", \"3\": \"Processed sample text\", \"4\": \"Processed sample text\"}\n" +
                "Instructions: Convert each text to uppercase.\n" +
                "Input: {\"1\": \"example text\", \"2\": \"another example\", \"3\": \"more text\", \"4\": \"final text\"}\n" +
                "Output: {\"1\": \"EXAMPLE TEXT\", \"2\": \"ANOTHER EXAMPLE\", \"3\": \"MORE TEXT\", \"4\": \"FINAL TEXT\"}\n" +
                "Please follow these instructions for every input.");

        JSONObject formatConfirmation = new JSONObject();
        formatConfirmation.put("role", "user");
        formatConfirmation.put("content", "I will only give you only format string and when you do your job you will give me parsed as based on what i provided to you. Only result I care nothing much.");

        JSONObject roleBehaviorExplanation = new JSONObject();
        roleBehaviorExplanation.put("role", "system");
        roleBehaviorExplanation.put("content", "This is the role how I will behave to adjust texts: " + selectedRole.introduce());

        JSONObject promptObject = new JSONObject();
        promptObject.put("role", "user");
        promptObject.put("content", promptString);

        messages.put(initialDefinition);
        if (roleType == OpenAIRoleType.RAW_TO_MEANINGFUL) {
            messages.put(formatIntroduction);
            messages.put(formatConfirmation);
        }
        messages.put(roleBehaviorExplanation);
        messages.put(promptObject);

        payload.put("model", OPENAI_MODEL);
        payload.put("messages", messages);
        payload.put("temperature", selectedRole.getTemperature());

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(OPENAI_API_COMPLETION_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + OPEN_AI_SECRET_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            System.out.println("Error: " + response.statusCode());
            System.out.println(response.body());
        }

        JsonNode node = mapper.readTree(response.body());
        String content = node.get("choices").get(0).get("message").get("content").asText();

        return content;
    }

}
