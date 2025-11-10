package main.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import main.model.UserRole;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private UserRole role;

    private String firstName;

    private String lastName;

    @NotBlank
    @Size(min = 6, max = 26, message = "Username length must be between 6 and 26 symbols.")
    private String username;

    @NotBlank
    @Size(min = 5, max = 8, message = "Password must be exactly 6 symbols.")
    private String password;


}
