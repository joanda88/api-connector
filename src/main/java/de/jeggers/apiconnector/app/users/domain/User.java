package de.jeggers.apiconnector.app.users.domain;

import lombok.*;
import org.hibernate.annotations.Type;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="user", schema = "api_connector")
public class User {
    @Id
    @Column(updatable = false)
    @Type(type = "uuid-char")
    private UUID id;

    private String userName;
    private String email;
    private String country;
    private String gender;
    private String latitude;
    private String longitude;

}
