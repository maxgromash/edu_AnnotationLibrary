package testClasses;

import annotations.*;

import java.util.List;
import java.util.Set;

@Constrained
public class Additionally {

    @Positive
    private final int temperature;

    @NotNull
    @Size(min = 6, max = 10)
    List<@NotNull @NotEmpty String> info;

    @NotNull
    @Size(min = 1, max = 10)
    List<Extra> strings;

    @NotNull
    Set<@NotNull Extra> extras;

    List<@NotNull List<String>> testListNull;

    public Additionally(int temperature, List<String> info) {
        strings = null;
        extras = null;
        this.info = info;
        this.temperature = temperature;
        testListNull = null;
    }
}

class Extra {
}

