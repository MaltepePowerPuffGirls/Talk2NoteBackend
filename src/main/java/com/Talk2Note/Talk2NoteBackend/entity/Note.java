package com.Talk2Note.Talk2NoteBackend.entity;

import com.Talk2Note.Talk2NoteBackend.core.enums.NoteStatus;
import com.Talk2Note.Talk2NoteBackend.core.enums.Priority;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "note")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "note_name", unique = true)
    private String noteName;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "note_status")
    private NoteStatus noteStatus;

    private boolean pinned;

    @Column(length = 1000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @JsonBackReference
    private User author;

    @OneToMany(mappedBy = "note")
    @JsonManagedReference
    private List<TextBlock> textBlocks;

    @OneToMany(mappedBy = "note")
    @JsonManagedReference
    private List<Member> members;

    @Column(name = "created_at")
    @Temporal(TemporalType.DATE)
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "modified_at")
    @Temporal(TemporalType.DATE)
    @UpdateTimestamp
    private Date modifiedAt;
}