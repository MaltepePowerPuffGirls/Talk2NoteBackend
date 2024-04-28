package com.Talk2Note.Talk2NoteBackend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Data
@Builder
@Entity
@Table(name = "member")
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "authority", length = 50)
    private String authority;

    private boolean accepted;

    @ManyToOne
    @JoinColumn(name = "note_id")
    @JsonBackReference
    private Note note;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "created_at")
    @Temporal(TemporalType.DATE)
    @CreationTimestamp
    private Date invitedAt; // its same with createdAt

    @Column(name = "accepted_at")
    private Date acceptedAt;

    @Column(name = "modified_at")
    @Temporal(TemporalType.DATE)
    @UpdateTimestamp
    private Date modifiedAt;
}
