import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.List;
import java.util.Map;

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
        StatsBuilder.main(new String[]{outputFileName, "20220512"});
        assertEquals("invalid number of arguments", outputStreamCaptor.toString()
                .trim());
    }

    @Test
    void emptyLogs() throws IOException, URISyntaxException {
        String inputFileName = "./testFiles/empty.log";
        StatsBuilder.main(new String[]{inputFileName, outputFileName, "20220512"});
        assertEquals(
                readFile("./testFiles/empty.csv"),
                readFile(outputFileName));
    }

    @Test
    void noLogFile() {
        String inputFileName = "./testFiles/blabla.log";
        StatsBuilder.main(new String[]{inputFileName, outputFileName, "20220512"});
        assertEquals("log file not found", outputStreamCaptor.toString()
                .trim());
    }

    @Test
    void oneSuccessfulRequestsCsv() throws IOException, URISyntaxException {
        String inputFileName = "./testFiles/oneSuccessful.log";
        StatsBuilder.main(new String[]{inputFileName, outputFileName, "20220512"});
        assertEquals(
                readFile("./testFiles/oneSuccessful.csv"),
                readFile(outputFileName));
    }

    @Test
    void twoSuccessfulRequestsCsv() throws IOException, URISyntaxException {
        String inputFileName = "./testFiles/twoSuccessful.log";
        String outputExpectedFileName = "./testFiles/twoSuccessful.csv";
        StatsBuilder.main(new String[]{inputFileName, outputFileName, "20220512"});
        assertEquals(
                readFile(outputExpectedFileName),
                readFile(outputFileName));
    }

    @Test
    void multipleCorrectFormatRequestsCsv() throws IOException, URISyntaxException {
        String inputFileName = "./testFiles/multipleCorrectFormat.log";
        String outputExpectedFileName = "./testFiles/multipleCorrectFormat.csv";
        StatsBuilder.main(new String[]{inputFileName, outputFileName, "20220512"});
        assertEquals(
                readFile(outputExpectedFileName),
                readFile(outputFileName));
    }

    @Test
    void multipleCorrectFormatSome401RequestsCsv() throws IOException, URISyntaxException {
        String inputFileName = "./testFiles/multipleCorrectFormatSome401.log";
        String outputExpectedFileName = "./testFiles/multipleCorrectFormatSome401.csv";
        StatsBuilder.main(new String[]{inputFileName, outputFileName, "20220512"});
        assertEquals(
                readFile(outputExpectedFileName),
                readFile(outputFileName));
    }

    void deleteFolder(String path) throws IOException {
        Path folderPath = Path.of(path);
        Files.walk(folderPath)
                .map(Path::toFile)
                .forEach(File::delete);
        Files.delete(folderPath);
    }

    @Test
    void multipleDayLogsDefaultArgs() throws IOException, URISyntaxException {
        String outputExpectedFileName = "./testFiles/multipleDay.csv";
        Path inputFile = Path.of("./logfiles/requests.log");
        Map.Entry<Long, String> yDate = StatsCollectorTest.getDateEntry(1);
        Map.Entry<Long, String> tDate = StatsCollectorTest.getDateEntry(0);
        Path logFolder = Path.of("./logfiles");
        if (!Files.exists(logFolder))
            Files.createDirectory(logFolder);
        Files.write(inputFile, List.of(
                "TIMESTAMP,BYTES,STATUS,REMOTE_ADDR",
                yDate.getKey() + ",876,200,10.98.7.2",
                yDate.getKey() + ",87652,200,10.98.7.1",
                yDate.getKey() + ",87,200,10.98.7.2",
                yDate.getKey() + ",8763,200,10.98.7.3",
                yDate.getKey() + ",8760,200,10.98.7.3",
                tDate.getKey() + ",87655,200,10.98.7.4",
                tDate.getKey() + ",8761,400,10.98.7.22",
                tDate.getKey() + ",8761,400,10.98.7.3",
                tDate.getKey() + ",900,200,10.98.7.2"
        ), StandardOpenOption.CREATE);
        StatsBuilder.main(new String[0]);
        assertEquals(
                readFile(outputExpectedFileName),
                readFile("./reports/ipaddr.csv"));
        deleteFolder("./logFiles");
        deleteFolder("./reports");
    }
}