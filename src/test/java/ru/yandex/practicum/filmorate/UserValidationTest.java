package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.validation.groups.OnCreate;
import ru.yandex.practicum.filmorate.validation.groups.OnUpdate;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserValidationTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldValidateValidUserOnCreate() {
        NewUserRequest user = new NewUserRequest();
        user.setEmail("test@example.com");
        user.setLogin("Логин");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        Set<ConstraintViolation<NewUserRequest>> violations = validator.validate(user, OnCreate.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailIfEmailBlankOnCreate() {
        NewUserRequest user = new NewUserRequest();
        user.setEmail(" ");
        user.setLogin("Логин");
        Set<ConstraintViolation<NewUserRequest>> violations = validator.validate(user, OnCreate.class);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void shouldFailIfEmailInvalid() {
        NewUserRequest user = new NewUserRequest();
        user.setEmail("Неправильный емейл");
        user.setLogin("Логин");
        Set<ConstraintViolation<NewUserRequest>> violations = validator.validate(user, OnCreate.class);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void shouldFailIfLoginHasSpaces() {
        NewUserRequest user = new NewUserRequest();
        user.setEmail("test@example.com");
        user.setLogin("Логин с пробелами");
        Set<ConstraintViolation<NewUserRequest>> violations = validator.validate(user, OnCreate.class);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("login")));
    }

    @Test
    void shouldFailIfLoginIsNullOnCreate() {
        NewUserRequest user = new NewUserRequest();
        user.setEmail("test@example.com");
        user.setLogin(null);
        Set<ConstraintViolation<NewUserRequest>> violations = validator.validate(user, OnCreate.class);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("login")));
    }

    @Test
    void shouldPassIfLoginHasNoSpaces() {
        NewUserRequest user = new NewUserRequest();
        user.setEmail("test@example.com");
        user.setLogin("Логин");
        Set<ConstraintViolation<NewUserRequest>> violations = validator.validate(user, OnCreate.class);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldPassIfBirthdayIsNull() {
        NewUserRequest user = new NewUserRequest();
        user.setEmail("test@example.com");
        user.setLogin("Логин");
        user.setBirthday(null);

        Set<ConstraintViolation<NewUserRequest>> violations = validator.validate(user, OnCreate.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailIfBirthdayInFuture() {
        NewUserRequest user = new NewUserRequest();
        user.setEmail("test@example.com");
        user.setLogin("Логин");
        user.setBirthday(LocalDate.now().plusDays(1)); // будущее

        Set<ConstraintViolation<NewUserRequest>> violations = validator.validate(user, OnCreate.class);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("birthday")));
    }

    @Test
    void shouldFailIfIdIsNullOnUpdate() {
        UpdateUserRequest user = new UpdateUserRequest();
        user.setId(null);
        user.setEmail("test@example.com");
        user.setLogin("Логин");
        Set<ConstraintViolation<UpdateUserRequest>> violations = validator.validate(user, OnUpdate.class);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("id")));
    }

    @Test
    void shouldPassIfIdIsPresentOnUpdate() {
        UpdateUserRequest user = new UpdateUserRequest();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setLogin("Логин");
        Set<ConstraintViolation<UpdateUserRequest>> violations = validator.validate(user, OnUpdate.class);

        assertTrue(violations.isEmpty());
    }
}