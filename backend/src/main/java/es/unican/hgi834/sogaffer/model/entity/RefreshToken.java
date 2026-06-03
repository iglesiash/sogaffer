package es.unican.hgi834.sogaffer.model.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "refresh_tokens",
        uniqueConstraints = {
                @UniqueConstraint(name = "UQ_REFRESH_TOKEN_TOKEN", columnNames = {"TOKEN"})
        })
public class RefreshToken extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_REFRESH_TOKEN_USER"))
    private User user;

    @Column(name = "TOKEN", nullable = false)
    private String hashedToken;

    @Column(name = "EXPIRES_AT", nullable = false)
    private Instant expirationDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getHashedToken() {
        return hashedToken;
    }

    public void setHashedToken(String tokenHash) {
        this.hashedToken = tokenHash;
    }

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Instant expiresAt) {
        this.expirationDate = expiresAt;
    }
}