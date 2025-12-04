package app.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String role;

    @NotBlank(message = "Първото име е задължително")
    private String firstName;

    private String lastName;

    @NotBlank(message = "Потребителско име е задължително")
    @Size(min = 5, max = 20, message = "Прякора трябва да бъде между 5 и 20 символа")
    private String username;

    @NotBlank(message = "Паролата е задължителна")
    @Size(min = 5, max = 20, message = "Паролата трябва да бъде между 5 и 20 символа")
    private String password;


}
