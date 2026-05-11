package es.unican.hgi834.sogaffer.model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "SORARE_ID", nullable = false)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID sorareId;

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
}
