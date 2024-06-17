import java.util.Objects;

public class Country {
    private final String code;
    private final String name;
    private final long population;

    public Country(String code, String name, long population) {
        this.code = code;
        this.name = name;
        this.population = population;
    }

    @Override
    public String toString() {
        return name + " (" + code + ")" + " ma " + population + " ludności.";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Country country = (Country) o;
        return population == country.population && Objects.equals(code, country.code) && Objects.equals(name, country.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, population);
    }
}
