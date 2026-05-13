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

    @Column(name = "EXPIRATION_DATE", nullable = false)
    private Instant expirationDate;

    @Column(name = "IS_VALID", nullable = false)
    private boolean isValid;

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

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }
}
