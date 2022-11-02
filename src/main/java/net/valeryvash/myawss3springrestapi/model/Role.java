package net.valeryvash.myawss3springrestapi.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "roles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role extends BaseEntity {

    @Column(name = "role_name")
    private String roleName;

    @ManyToMany(
            mappedBy = "roles",
            fetch = FetchType.LAZY
    )
    @ToString.Exclude
    private List<User> users;

    @Override
    public String toString() {
        return "Role{" +
               "id:'" + getId() + "'," +
               "roleName='" + roleName + '\'' +
               '}';
    }
}
