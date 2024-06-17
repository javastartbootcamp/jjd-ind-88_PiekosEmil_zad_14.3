import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Main main = new Main();
        main.run(scanner);
    }

    void run(Scanner scanner) {
        File file = new File("countries.csv");
        if (!file.exists()) {
            System.out.println("Brak pliku " + file + ".");
        } else {
            Map<String, Country> countryMap = CountryReader.readFileToMap(file);
            String code = chooseCountry(countryMap, scanner);
            System.out.println(countryMap.get(code));
        }
    }

    private String chooseCountry(Map<String, Country> countryMap, Scanner scanner) {
        boolean codeExist = true;
        String code = null;
        System.out.println("Podaj kod kraju, o którym chcesz zobaczyć informacje:");
        code = scanner.nextLine().toUpperCase();
        codeExist = conutryNotFound(countryMap, code);
        if (!codeExist) {
            System.out.println("Kod kraju " + code + " nie został znaleziony.");
        }
        return code;
    }

    private boolean conutryNotFound(Map<String, Country> countryMap, String inputCode) {
        for (String code : countryMap.keySet()) {
            if (code.equals(inputCode)) {
                return true;
            }
        }
        return false;
    }
}
