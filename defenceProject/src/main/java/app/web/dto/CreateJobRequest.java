package app.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateJobRequest {

    @NotBlank(message = "Заглавието е задължително")
    @Size(max = 200, message = "Заглавието не може да бъде по-дълго от 200 символа")
    private String title;

    @Size(max = 100, message = "Екипът не може да бъде по-дълъг от 100 символа")
    private String team;

    @NotBlank(message = "Компанията е задължителна")
    @Size(max = 100, message = "Името на компанията не може да бъде по-дълго от 100 символа")
    private String company;

    @NotBlank(message = "Локацията е задължителна")
    @Size(max = 100, message = "Локацията не може да бъде по-дълга от 100 символа")
    private String location;

    @NotBlank(message = "Типът на заетостта е задължителен")
    private String employmentType;

    @NotBlank(message = "Моделът на работа е задължителен")
    private String workModel;

    @PositiveOrZero(message = "Минималната заплата трябва да бъде положително число")
    private BigDecimal salaryMin;

    @PositiveOrZero(message = "Максималната заплата трябва да бъде положително число")
    private BigDecimal salaryMax;

    private String currency;

    private String payCycle;

    @Size(max = 500, message = "Резюмето не може да бъде по-дълго от 500 символа")
    private String summary;

    @NotBlank(message = "Описанието е задължително")
    @Size(max = 5000, message = "Описанието не може да бъде по-дълго от 5000 символа")
    private String description;

    @Size(max = 2000, message = "Изискванията не могат да бъдат по-дълги от 2000 символа")
    private String requirements;

    @NotNull(message = "Датата на публикуване е задължителна")
    private LocalDate publishDate;

    @NotNull(message = "Крайният срок е задължителен")
    private LocalDate deadline;

}





