import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class StatsBuilder {
    private static Path getLogPath(String fileName) throws FileNotFoundException {
        Path filePath = Path.of(fileName);
        if (!Files.exists(filePath))
            throw new FileNotFoundException();
        return filePath;
    }

    private static List<String> getRawDataFromFile(String rawData) throws IOException {
        Path logFilePath = getLogPath(rawData);
        if (rawData.isEmpty())
            return List.of();
        else
            return Files.readAllLines(logFilePath);
    }
    private static String getYesterday() {
        LocalDate date = LocalDate.now().minusDays(1);
        return date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
    private static String[] getArgsOrDefault(String[] args) throws IllegalArgumentException {
        if (args.length > 0 && args.length< 3)
            throw new IllegalArgumentException();
        return args.length == 3 ? args : new String[] {
                "./logFiles/requests.log",
                "./reports/ipaddr.csv",
                getYesterday()
        };
    }
    public static void main(String[] args) {
        try {
            args = getArgsOrDefault(args);
            List<String> rawData = getRawDataFromFile(args[0]);
            DataSet dataSet = new DataSet();
            dataSet.loadData(rawData);
            StatsCollector collector = new StatsCollector(dataSet);
            DailyStat stats = collector.collect(args[2]);
            Path outputPath = Path.of(args[1]);
            Path parentPath = outputPath.getParent();
            if (!Files.exists(parentPath))
                Files.createDirectory(parentPath);
            Files.writeString(Path.of(args[1]), stats.toCsv(), StandardOpenOption.CREATE);
        } catch (FileNotFoundException e) {
            System.out.print("log file not found");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            System.out.print("invalid number of arguments");
        }
    }
}
