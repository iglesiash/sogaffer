package es.unican.hgi834.sogaffer.model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_USER_EMAIL", columnNames = {"EMAIL"}),
        })
public class User extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "SORARE_ID", nullable = false)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID sorareId;

    @Column(nullable = false)
    private String email;

    @Column(name = "UPDATED_AT")
    private Instant updatedAt;

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getSorareId() {
        return sorareId;
    }

    public void setSorareId(UUID sorareId) {
        this.sorareId = sorareId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
