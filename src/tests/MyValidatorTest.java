import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import testClasses.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

class MyValidatorTest {

    @Test
    void validateBookingForm() {
        Set<ValidationError> validationErrors = testForm();

        Set<ValidationError> errorsActual = Set.of(
                new MyValidationError(new Pair("must not be null", null), "guests[0].firstName"),
                new MyValidationError(new Pair("must not be null", null), "additionally.strings"),
                new MyValidationError(new Pair("must be one of 'Chocolate House' 'Castle' ",
                        "Studio"), "propertyType"),
                new MyValidationError(new Pair("must not be empty", ""), "additionally.info[1]"),
                new MyValidationError(new Pair("must not be null", null), "additionally.extras"),
                new MyValidationError(new Pair("must not be blank", ""), "guests[1].lastName"),
                new MyValidationError(new Pair("must be in range between 0 and 200",
                        -228), "guests[0].age"),
                new MyValidationError(new Pair("must be positive", -33),
                        "additionally.temperature"),
                new MyValidationError(new Pair("must be in range between 6 and 10",
                        List.of("Garbage", "", "21Savage")), "additionally.info"),
                new MyValidationError(new Pair("must be one of 'Japanese toilet' 'Basketball field' ",
                        "Carpet"), "amenities[0]")

        );

        Assertions.assertEquals(validationErrors.size(), 10);
        for (ValidationError error : validationErrors)
            Assertions.assertTrue(errorsActual.contains(error));
    }

    @Test
    void validateRappers() {
        Set<ValidationError> validationErrors = testRapBattle();

        Set<ValidationError> errorsActual = Set.of(
                new MyValidationError(new Pair("must be in range between 2 and 5",
                        Map.of()), "rappers[1].priorityVS"),
                new MyValidationError(new Pair("must be in range between 10 and 100",
                        Set.of("Окси", "Одинокая старуха")), "rappers[0].enemies"),
                new MyValidationError(new Pair("must not be empty", ""),
                        "rappers[1].topSongs[2]"),
                new MyValidationError(new Pair("must be one of '1703' ", "1702"), "place"),
                new MyValidationError(new Pair("must be in range between 2 and 5",
                        Map.of(1, "Лучший")), "rappers[0].priorityVS"),
                new MyValidationError(new Pair("must be in range between 10 and 100",
                        Set.of("Бодрый конб", "", "Оливье с горчицей")), "rappers[1].enemies"),
                new MyValidationError(new Pair("must be in range between 18 and 40",
                        17), "rappers[0].age"),
                new MyValidationError(new Pair("must not be empty", Map.of()), "rappers[1].priorityVS"),
                new MyValidationError(new Pair("@Negative wrong type",
                        List.of("Маркул", "Лукрам", "", "Барри солёный", "Смоки смог 174")), "guests")
        );

        Assertions.assertEquals(validationErrors.size(), 9);
        for (ValidationError error : validationErrors) {
            boolean f = errorsActual.contains(error);
            Assertions.assertTrue(errorsActual.contains(error));
        }

    }

    private Set<ValidationError> testRapBattle() {
        List<Rapper> rappers = List.of(
                new Rapper("Джони бой", 17, List.of("Твои глаза голубые", "Твои веки тяжелые"),
                        Set.of("Окси", "Одинокая старуха"), Map.of(1, "Лучший")),
                new Rapper("Гарри Топор", 33, List.of("Мои вены горят", "#Топорострееножа", ""),
                        Set.of("Бодрый конб", "", "Оливье с горчицей"), Map.of()));

        RapBattle battle = new RapBattle(rappers, List.of("Маркул", "Лукрам", "", "Барри солёный", "Смоки смог 174"),
                "1702");

        MyValidator validator = new MyValidator();
        return validator.validate(battle);


    }

    private Set<ValidationError> testForm() {
        List<GuestForm> guests = List.of(
                new GuestForm(null, "Markelov", -228),
                new GuestForm("Bobrik", "", 17)
        );
        Additionally additionally = new Additionally(-33, List.of("Garbage", "", "21Savage"));
        BookingForm bookingForm = new BookingForm(guests,
                List.of("Carpet", "Japanese toilet"), "Studio", additionally);
        MyValidator validator = new MyValidator();
        return validator.validate(bookingForm);

    }
}