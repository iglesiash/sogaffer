package es.unican.hgi834.sogaffer.model.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "sorare_tokens")
public class SorareToken extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_SORARE_TOKEN_USER"))
    private User user;

    @Column(nullable = false)
    private String token;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
