
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.groups.OnCreate;
import ru.yandex.practicum.filmorate.validation.groups.OnUpdate;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserValidationTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldValidateValidUserOnCreate() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("Логин");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user, OnCreate.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailIfEmailBlankOnCreate() {
        User user = new User();
        user.setEmail(" ");
        user.setLogin("Логин");
        Set<ConstraintViolation<User>> violations = validator.validate(user, OnCreate.class);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void shouldFailIfEmailInvalid() {
        User user = new User();
        user.setEmail("Неправильный емейл");
        user.setLogin("Логин");
        Set<ConstraintViolation<User>> violations = validator.validate(user, OnCreate.class);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void shouldFailIfLoginHasSpaces() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("Логин с пробелами");
        Set<ConstraintViolation<User>> violations = validator.validate(user, OnCreate.class);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("login")));
    }

    @Test
    void shouldFailIfLoginIsNullOnCreate() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin(null);
        Set<ConstraintViolation<User>> violations = validator.validate(user, OnCreate.class);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("login")));
    }

    @Test
    void shouldPassIfLoginHasNoSpaces() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("Логин");
        Set<ConstraintViolation<User>> violations = validator.validate(user, OnCreate.class);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldPassIfBirthdayIsNull() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("Логин");
        user.setBirthday(null);

        Set<ConstraintViolation<User>> violations = validator.validate(user, OnCreate.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailIfBirthdayInFuture() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("Логин");
        user.setBirthday(LocalDate.now().plusDays(1)); // будущее

        Set<ConstraintViolation<User>> violations = validator.validate(user, OnCreate.class);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("birthdayValid")));
    }

    @Test
    void shouldFailIfIdIsNullOnUpdate() {
        User user = new User();
        user.setId(null);
        user.setEmail("test@example.com");
        user.setLogin("Логин");
        Set<ConstraintViolation<User>> violations = validator.validate(user, OnUpdate.class);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("id")));
    }

    @Test
    void shouldPassIfIdIsPresentOnUpdate() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setLogin("Логин");
        Set<ConstraintViolation<User>> violations = validator.validate(user, OnUpdate.class);

        assertTrue(violations.isEmpty());
    }
}