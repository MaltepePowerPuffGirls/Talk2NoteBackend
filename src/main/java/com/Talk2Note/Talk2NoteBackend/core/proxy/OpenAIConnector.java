package com.Talk2Note.Talk2NoteBackend.core.proxy;

import com.Talk2Note.Talk2NoteBackend.core.enums.OpenAIRoleType;
import com.Talk2Note.Talk2NoteBackend.core.objects.OpenAIRole;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Value("${openai.role.description.raw-to-meaningful}")
    private String RawToMeaningfulDesc;
    @Value("${openai.role.description.md-auto}")
    private String MdAutoDesc;

    private RestTemplate restTemplate;
    private HashMap<OpenAIRoleType, OpenAIRole> openAIRoles = new HashMap<>();

    @Autowired
    public OpenAIConnector(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;

        List<OpenAIRole> generatedRoles = List.of(
                OpenAIRole.builder()
                        .actorName("Raw to Meaningful")
                        .roleType(OpenAIRoleType.RAW_TO_MEANINGFUL)
                        .temperature(0.3f)
                        .build(),
                OpenAIRole.builder()
                        .actorName("MD Auto")
                        .roleType(OpenAIRoleType.MD_AUTO)
                        .temperature(0.1f)
                        .build()
        );

        for (OpenAIRole role: generatedRoles) {
            this.openAIRoles.put(role.getRoleType(), role);
        }

    }


    public String compose(String promptString, OpenAIRoleType roleType) {

        OpenAIRole selectedRole = this.openAIRoles.get(roleType);
        if (selectedRole == null) {
            return null;
        }

        HttpHeaders headers = generateDefaultHeaders();
        Map<String, Object> body = new HashMap<>();

        JSONArray messages = new JSONArray();

        JSONObject actorDefinition = new JSONObject();
        actorDefinition.put("role", "system");
        actorDefinition.put("content", "from now on I will be acting as following sentences: " + selectedRole.introduce());

        JSONObject inputOutputFormat = new JSONObject();
        inputOutputFormat.put("role", "system");
        inputOutputFormat.put("content", "You must create a response that includes the sent string in a meaningful way, " +
                "between the \" << \" and \">>\" signs, and with the \"::\" sign at the beginning and end of each sentence.");

        JSONObject prompt = new JSONObject();
        prompt.put("role", "user");
        prompt.put("content", promptString);

        messages.put(actorDefinition);
        messages.put(inputOutputFormat);
        messages.put(prompt);

        body.put("model", OPENAI_MODEL);
        body.put("messages", messages);
        body.put("max_tokens", MAX_TOKENS);
        body.put("temperature" , selectedRole.getTemperature());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(OPENAI_API_COMPLETION_URL, HttpMethod.POST, entity, String.class);

        return response.getBody();
    }

    private HttpHeaders generateDefaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(OPEN_AI_SECRET_KEY);
        return headers;
    }

}
