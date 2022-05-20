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
    public String getYesterday() {
        LocalDate date = LocalDate.now().minusDays(1);
        return date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
    public static void main(String[] args) {
        try {
            List<String> rawData = getRawDataFromFile(args[0]);
            DataSet dataSet = new DataSet();
            dataSet.loadData(rawData);
            if (dataSet.size() == 0) {
                System.out.print("no logs available");
                return;
            }
            StatsCollector collector = new StatsCollector(dataSet);
            DailyStat stats = collector.collect(args[2]);
            Files.writeString(Path.of(args[1]), "IP Address,Number of requests,Percentage of requests,Total Bytes sent,Percentage of bytes\n", StandardOpenOption.CREATE);
            List<String> results = stats
                    .stream()
                    .map(stat -> stat.toString(new CsvFormatter()))
                    .toList();
            Files.write(Path.of(args[1]), results, StandardOpenOption.APPEND);
        } catch (FileNotFoundException e) {
            System.out.print("log file not found");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
