package net.valeryvash.myawss3springrestapi.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "files")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "file_name")
    private String fileName;

    @OneToOne(
            mappedBy = "file",
            cascade = {CascadeType.PERSIST}
    )
    private Event event = new Event();


    /**
     * Helper methods
     */


}
