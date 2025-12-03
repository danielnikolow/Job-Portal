package app.cv.client.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CvRequest {

    private UUID id;
    // Step 1: Основни данни
//    @Size(max = 100, message = "Пълното име не може да бъде повече от 100 символа")
    private String fullName;

    //    @Size(max = 100, message = "Желаната позиция не може да бъде повече от 100 символа")
    private String birthDate;

    //    @Email(message = "Невалиден email адрес")
    private String email;

    //    @Size(max = 20, message = "Телефонът не може да бъде повече от 20 символа")
    private String phone;

    //    @Size(max = 100, message = "Локацията не може да бъде повече от 100 символа")
    private String location;

    //    @Size(max = 200, message = "URL не може да бъде повече от 200 символа")
    private String linkedInUrl;

    //    @Size(max = 1000, message = "Резюмето не може да бъде повече от 1000 символа")
    private String summary;

    // Step 2: Опит
//    @Size(max = 100, message = "Длъжността не може да бъде повече от 100 символа")
    private String jobTitle;

    //    @Size(max = 100, message = "Компанията не може да бъде повече от 100 символа")
    private String company;

    private String experienceStartDate;
    private String experienceEndDate;

    //    @Size(max = 2000, message = "Описанието не може да бъде повече от 2000 символа")
    private String experienceDescription;

    // Step 3: Образование
//    @Size(max = 100, message = "Специалността не може да бъде повече от 100 символа")
    private String degree;

    //    @Size(max = 100, message = "Училището не може да бъде повече от 100 символа")
    private String school;

    private String educationStartDate;
    private String educationEndDate;

    //    @Size(max = 1000, message = "Допълнителната информация не може да бъде повече от 1000 символа")
    private String educationInfo;

    // Step 4: Умения
//    @Size(max = 500, message = "Техническите умения не могат да бъдат повече от 500 символа")
    private String technicalSkills;

    //    @Size(max = 500, message = "Езиците не могат да бъдат повече от 500 символа")
    private String languages;

    // Step 5: Финални настройки
//    @Size(max = 50, message = "Темата не може да бъде повече от 50 символа")
    private String cvName;

    private UUID userId;

    private LocalDate createdAt;
}