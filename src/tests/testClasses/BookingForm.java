package testClasses;

import annotations.AnyOf;
import annotations.Constrained;
import annotations.NotNull;
import annotations.Size;

import java.util.List;

@Constrained
public class BookingForm {
    @NotNull
    @Size(min = 2, max = 17)
    private List<@NotNull GuestForm> guests;
    @NotNull
    private List<@AnyOf({"Japanese toilet", "Basketball field"}) String> amenities;
    @NotNull
    @AnyOf({"Chocolate House", "Castle"})
    private String propertyType;
    @NotNull
    private Additionally additionally;


    public BookingForm(List<GuestForm> guests, List<String> amenities, String
            propertyType, Additionally additionally) {
        this.additionally = additionally;
        this.propertyType = propertyType;
        this.guests = guests;
        this.amenities = amenities;
    }
}
