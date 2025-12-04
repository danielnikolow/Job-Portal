package app.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class    EditAccountRequest {

    @Pattern(
            regexp = "^$|.{5,20}$",
            message = "Паролата трябва да бъде празна или между 5 и 20 символа"
    )
    private String username;

    private String location;

    @Email
    private String email;

    private String phone;

    @Pattern(
            regexp = "^$|.{5,20}$",
            message = "Паролата трябва да бъде празна или между 5 и 20 символа"
    )
    private String currentPassword;

    @Pattern(
            regexp = "^$|.{5,20}$",
            message = "Паролата трябва да бъде празна или между 5 и 20 символа"
    )
    private String newPassword;

    private String confirmPassword;

}
