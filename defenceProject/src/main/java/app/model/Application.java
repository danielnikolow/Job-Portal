package main.model;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "applications")
public class Application {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "job")
    private Job job;

    @Lob
    private byte[] cvFile;

    private String CvFileName;

    private String CvContentType;

    private String status;

    private LocalDate submittedOn;

    private boolean active;
}
