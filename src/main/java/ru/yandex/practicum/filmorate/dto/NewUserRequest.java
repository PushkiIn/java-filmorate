package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.NotAfterToday;
import ru.yandex.practicum.filmorate.validation.groups.OnCreate;

import java.time.LocalDate;

@Data
public class NewUserRequest {
    @NotNull(groups = OnCreate.class)
    @NotBlank(groups = OnCreate.class)
    @Email(groups = OnCreate.class)
    private String email;

    @NotNull(groups = OnCreate.class)
    @Pattern(regexp = "\\S+", groups = OnCreate.class)
    private String login;

    private String name;

    @NotAfterToday(groups = OnCreate.class)
    private LocalDate birthday;
}
