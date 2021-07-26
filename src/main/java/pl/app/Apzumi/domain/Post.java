package pl.app.Apzumi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Post {

    @JsonIgnore
    int userId;
    @Id
    Long id;
    String title;
    String body;
    @JsonIgnore
    boolean updated = false;
}
