package net.valeryvash.myawss3springrestapi.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "events")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type")
    private EventType eventType = EventType.CREATED;

    @CreationTimestamp
    @Column(name = "created")
    private Date created = new Date();

    @ManyToOne(
            cascade = {CascadeType.PERSIST},
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "file_id")
    private File file;

}
