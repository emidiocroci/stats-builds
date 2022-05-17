import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StatsBuilderTest {
    String outputFileName = "./testFiles/result.csv";
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        try {
            Files.delete(Path.of(outputFileName));
        } catch (IOException e) {
        }
    }

    String readFile(String path) throws IOException, URISyntaxException {
        return Files.readString(Path.of(path));
    }

    @Test
    void emptyLogs() {
        String inputFileName = "./testFiles/empty.log";
        StatsBuilder.main(List.of(inputFileName, outputFileName)
                .toArray(String[]::new));
        assertEquals("no logs available", outputStreamCaptor.toString()
                .trim());
    }

    @Test
    void noLogFile() {
        String inputFileName = "./testFiles/blabla.log";
        StatsBuilder.main(List.of(inputFileName, outputFileName)
                .toArray(String[]::new));
        assertEquals("log file not found", outputStreamCaptor.toString()
                .trim());
    }

    @Test
    void oneSuccessfulRequestsCsv() throws IOException, URISyntaxException {
        String inputFileName = "./testFiles/oneSuccessful.log";
        String outputExpectedFileName = "./testFiles/oneSuccessful.csv";
        StatsBuilder.main(List.of(inputFileName, outputFileName)
                .toArray(String[]::new));
        assertEquals(
                readFile(outputExpectedFileName),
                readFile(outputFileName));
    }

    @Test
    void twoSuccessfulRequestsCsv() throws IOException, URISyntaxException {
        String inputFileName = "./testFiles/twoSuccessful.log";
        String outputExpectedFileName = "./testFiles/twoSuccessful.csv";
        StatsBuilder.main(List.of(inputFileName, outputFileName)
                .toArray(String[]::new));
        assertEquals(
                readFile(outputExpectedFileName),
                readFile(outputFileName));
    }
}