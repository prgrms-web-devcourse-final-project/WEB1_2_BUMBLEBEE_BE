package roomit.main.global.token.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "refresh")
public class RefreshEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String refresh;

    private String expiration;

    public void changeUsername(String username) {
        this.username = username;
    }

    public void changeRefresh(String refresh) {
        this.refresh = refresh;
    }

    public void changeExpiration(String expiration) {
        this.expiration = expiration;
    }
}
