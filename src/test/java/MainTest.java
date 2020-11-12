import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.fail;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Timeout(5)
public class MainTest {

    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Test
    void shouldWorkForOnlyOneCountry() throws Exception {
        // given
        copyFileFromResources("example1.csv", "countries.csv");
        provideInput("PL");

        // when
        Main.main(new String[]{});

        // then
        assertThat(outContent.toString()).contains("PL (Polska) ma 38000000 ludności.");
    }

    @Test
    void shouldWorkForMultipleCountries() throws Exception {
        // given
        copyFileFromResources("example2.csv", "countries.csv");
        provideInput("DE");

        // when
        Main.main(new String[]{});

        // then
        assertThat(outContent.toString()).contains("DE (Niemcy) ma 80000000 ludności.");
    }

    @Test
    void shouldDisplayMessageWhenNoFile() throws Exception {
        // given

        File file = new File("countries.csv");
        if (file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                fail("Failed to delete file");
            }
        }

        // when
        Main.main(new String[]{});

        // then
        assertThat(outContent.toString()).contains("Brak pliku countries.csv.");
    }

    @Test
    void shouldDisplayMessageWhenCountryNotFound() throws Exception {
        // given
        copyFileFromResources("example1.csv", "countries.csv");
        provideInput("XX");

        // when
        Main.main(new String[]{});

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
        System.setIn(originalIn);
    }

    private void provideInput(String... lines) {
        String input = String.join("\r\n", lines);

        ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
        System.setIn(testIn);
    }

    private void copyFileFromResources(String name, String output) throws Exception {
        URL resource = getClass().getResource(name);
        Files.copy(Path.of(resource.toURI()), new File(output).toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    private void writeToFile(String fileContent, String filename) throws IOException {
        File file = new File(filename);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(fileContent);
        fileWriter.close();
    }

    private void writeToFile(List<String> lines, String filename) throws IOException {
        File file = new File(filename);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(String.join("\n", lines));
        fileWriter.close();
    }

}
