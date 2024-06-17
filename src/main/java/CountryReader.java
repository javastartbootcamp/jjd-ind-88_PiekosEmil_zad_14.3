import java.io.*;
import java.util.*;

public class CountryReader {

    public static Map<String, Country> readFileToMap(File file) {
        Map<String, Country> countryMap = new HashMap<>();
        try (
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                Scanner sc = new Scanner(bufferedReader).useDelimiter(";");
        ) {
            while (sc.hasNextLine()) {
                String code = sc.next();
                String name = sc.next();
                sc.skip(";");
                long population = Long.parseLong(sc.nextLine());
                countryMap.put(code, new Country(code, name, population));
            }
        } catch (IOException e) {
            System.out.println("Nie znaleziono pliku");
        }
        return countryMap;
    }
}
