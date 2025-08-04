import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.groups.OnCreate;
import ru.yandex.practicum.filmorate.validation.groups.OnUpdate;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FilmValidationTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        validatorFactory.close();
    }

    @Test
    void testNameNotBlankOnCreate() {
        Film film = new Film();
        film.setName(" ");
        film.setDescription("Описание");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(100L);

        Set<ConstraintViolation<Film>> violations = validator.validate(film, OnCreate.class);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void testDescriptionMaxLength() {
        Film film = new Film();
        film.setName("Имя");
        film.setDescription("a".repeat(201)); // 201 символ
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(100L);

        Set<ConstraintViolation<Film>> violations = validator.validate(film, OnCreate.class);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")));
    }

    @Test
    void testReleaseDateNotBefore1895() {
        Film film = new Film();
        film.setName("Имя");
        film.setDescription("Описание");
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        film.setDuration(100L);

        Set<ConstraintViolation<Film>> violations = validator.validate(film, OnCreate.class);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("releaseDateValid")));
    }

    @Test
    void testDurationPositive() {
        Film film = new Film();
        film.setName("Имя");
        film.setDescription("Описание");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(0L);

        Set<ConstraintViolation<Film>> violations = validator.validate(film, OnCreate.class);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("duration")));
    }

    @Test
    void testValidFilmOnCreate() {
        Film film = new Film();
        film.setName("Имя");
        film.setDescription("Описание");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120L);

        Set<ConstraintViolation<Film>> violations = validator.validate(film, OnCreate.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testIdNotNullOnUpdate() {
        Film film = new Film();
        film.setId(null);
        film.setName("Имя");
        film.setDescription("Описание");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(100L);

        Set<ConstraintViolation<Film>> violations = validator.validate(film, OnUpdate.class);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("id")));
    }

    @Test
    void testValidFilmOnUpdate() {
        Film film = new Film();
        film.setId(1L);
        film.setName("Имя");
        film.setDescription("Описание");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(100L);

        Set<ConstraintViolation<Film>> violations = validator.validate(film, OnUpdate.class);
        assertTrue(violations.isEmpty());
    }
}