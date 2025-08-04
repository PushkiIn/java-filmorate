package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.groups.OnCreate;
import ru.yandex.practicum.filmorate.validation.groups.OnUpdate;

import java.time.LocalDate;

@Data
public class User {
    @NotNull(message = "id обязателен при обновлении", groups = OnUpdate.class)
    private Long id;
    @NotBlank(message = "email обязателен при создании", groups = OnCreate.class)
    @Email(groups = {OnUpdate.class, OnCreate.class})
    private String email;
    @NotNull( message = "логин обязателен при создании", groups = OnCreate.class)
    @Pattern(regexp = "\\S+", message = "строка не должна содержать пробелов", groups = {OnUpdate.class, OnCreate.class})
    private String login;
    private String name;
    private LocalDate birthday;
    private boolean birthdayValid = isBirthdayValid();


    @AssertTrue(message = "День рождения не может быть в будущем", groups = {OnCreate.class, OnUpdate.class})
    private boolean isBirthdayValid() {
        if (birthday == null) {
            return true;
        }
        return !birthday.isAfter(LocalDate.now());
    }
}