package testClasses;

import annotations.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Constrained
public class Rapper {
    @NotEmpty
    String name;

    @NotNull
    @InRange(min = 18, max = 40)
    Integer age;

    @NotNull
    @NotEmpty
    List<@NotNull @NotEmpty String> topSongs;


    @NotNull
    @Size(min = 10, max = 100)
    Set<@NotEmpty String> enemies;

    @NotEmpty
    @Size(min = 2, max = 5)
    Map<@NotNull Integer, String> priorityVS;

    public Rapper(@NotEmpty String name, Integer age, List< String> topSongs,
                  Set<String> enemies, Map<Integer, String> priorityVS) {
        this.name = name;
        this.age = age;
        this.topSongs = topSongs;
        this.enemies = enemies;
        this.priorityVS = priorityVS;
    }
}
