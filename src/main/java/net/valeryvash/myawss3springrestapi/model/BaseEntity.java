package net.valeryvash.myawss3springrestapi.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @CreationTimestamp
    @Column(name = "created")
    private Date created;

    @UpdateTimestamp
    @Column(name = "updated")
    private Date updated;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.ACTIVE;

    @PrePersist
    public void prePersist() {
        this.created = new Date();
        this.updated = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        this.updated = new Date();
    }

}
