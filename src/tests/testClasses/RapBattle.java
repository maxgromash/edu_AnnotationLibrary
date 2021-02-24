package testClasses;

import annotations.*;

import java.util.List;

@Constrained
public class RapBattle {

    @Size(min = 2, max = 16)
    List<@NotNull Rapper> rappers;

    @Negative
    @NotEmpty
    List<@NotNull String> guests;

    @AnyOf({"1703"})
    String place;

    List<@NotNull List<String>> testListNull = null;

    public RapBattle(@Size(min = 2, max = 16) List<@NotNull Rapper> rappers, @NotEmpty List<@NotNull String> guests, @AnyOf({"1703"}) String place) {
        this.rappers = rappers;
        this.guests = guests;
        this.place = place;
    }
}
