import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

import static org.assertj.core.api.AssertionsForClassTypes.fail;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Timeout(5)
public class MainTest {

    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final Main main = new Main();

    @Test
    void shouldWorkForOnlyOneCountry() throws Exception {
        // given
        copyFileFromResources("example1.csv", "countries.csv");
        provideInput("PL");

        // when
        Main.main(new String[]{});

        // then
        assertThat(outContent.toString()).contains("Polska (PL) ma 38000000 ludności.");
    }

    @Test
    void shouldWorkForMultipleCountries() throws Exception {
        // given
        copyFileFromResources("example2.csv", "countries.csv");
        Scanner scanner = provideInput("DE");

        // when
        main.run(scanner);

        // then
        assertThat(outContent.toString()).contains("Niemcy (DE) ma 80000000 ludności.");
    }

    @Test
    void shouldDisplayMessageWhenNoFile() {
        // given

        File file = new File("countries.csv");
        if (file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                fail("Failed to delete file");
            }
        }

        Scanner scanner = provideInput();

        // when
        main.run(scanner);

        // then
        assertThat(outContent.toString()).contains("Brak pliku countries.csv.");
    }

    @Test
    void shouldDisplayMessageWhenCountryNotFound() throws Exception {
        // given
        copyFileFromResources("example1.csv", "countries.csv");
        Scanner scanner = provideInput("XX");

        // when
        main.run(scanner);

        // then
        assertThat(outContent.toString()).contains("Kod kraju XX nie został znaleziony.");
    }

    @BeforeEach
    void init() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void cleanup() {
        System.setOut(originalOut);
    }

    private Scanner provideInput(String... lines) {
        String input = String.join("\r\n", lines);

        ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
        return new Scanner(testIn);
    }

    private void copyFileFromResources(String name, String output) throws Exception {
        URL resource = getClass().getResource(name);
        Files.copy(Path.of(resource.toURI()), new File(output).toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

}
