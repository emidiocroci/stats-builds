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
    void wrongArgs() {
        StatsBuilder.main(new String[] { outputFileName, "20220512"});
        assertEquals("invalid number of arguments", outputStreamCaptor.toString()
                .trim());
    }

    @Test
    void emptyLogs() throws IOException, URISyntaxException {
        String inputFileName = "./testFiles/empty.log";
        StatsBuilder.main(new String[] {inputFileName, outputFileName, "20220512"});
        assertEquals(
                readFile("./testFiles/empty.csv"),
                readFile(outputFileName));
    }

    @Test
    void noLogFile() {
        String inputFileName = "./testFiles/blabla.log";
        StatsBuilder.main(new String[] {inputFileName, outputFileName, "20220512"});
        assertEquals("log file not found", outputStreamCaptor.toString()
                .trim());
    }

    @Test
    void oneSuccessfulRequestsCsv() throws IOException, URISyntaxException {
        String inputFileName = "./testFiles/oneSuccessful.log";
        StatsBuilder.main(new String[] {inputFileName, outputFileName, "20220512"});
        assertEquals(
                readFile( "./testFiles/oneSuccessful.csv"),
                readFile(outputFileName));
    }

    @Test
    void twoSuccessfulRequestsCsv() throws IOException, URISyntaxException {
        String inputFileName = "./testFiles/twoSuccessful.log";
        String outputExpectedFileName = "./testFiles/twoSuccessful.csv";
        StatsBuilder.main(new String[] {inputFileName, outputFileName, "20220512"});
        assertEquals(
                readFile(outputExpectedFileName),
                readFile(outputFileName));
    }

    @Test
    void multipleCorrectFormatRequestsCsv() throws IOException, URISyntaxException {
        String inputFileName = "./testFiles/multipleCorrectFormat.log";
        String outputExpectedFileName = "./testFiles/multipleCorrectFormat.csv";
        StatsBuilder.main(new String[] {inputFileName, outputFileName, "20220512"});
        assertEquals(
                readFile(outputExpectedFileName),
                readFile(outputFileName));
    }

    @Test
    void multipleCorrectFormatSome401RequestsCsv() throws IOException, URISyntaxException {
        String inputFileName = "./testFiles/multipleCorrectFormatSome401.log";
        String outputExpectedFileName = "./testFiles/multipleCorrectFormatSome401.csv";
        StatsBuilder.main(new String[] {inputFileName, outputFileName, "20220512"});
        assertEquals(
                readFile(outputExpectedFileName),
                readFile(outputFileName));
    }
}