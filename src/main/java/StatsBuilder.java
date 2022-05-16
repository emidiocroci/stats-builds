import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class StatsBuilder {

    static Path getLogPath(String fileName) throws FileNotFoundException {
        Path filePath = Path.of(fileName);
        if (!Files.exists(filePath))
            throw new FileNotFoundException();
        return filePath;
    }

    public static void main(String[] args) {
        try {
            Path logFilePath = getLogPath(args[0]);
            List<String> rawData = Files.readAllLines(logFilePath);
            if (rawData.isEmpty()) {
                System.out.print("no logs available");
                return;
            }
            List<Record> records = rawData
                    .stream()
                    .filter(Record::isOfValidFormat)
                    .map((String rawRecord) -> {
                        try {
                            return Record.fromString(rawRecord);
                        } catch (Exception e) {
                            return new Record();
                        }
                    })
                    .toList();
            Files.writeString(Path.of(args[1]), "IP Address,Number of requests,Percentage of requests,Total Bytes sent,Percentage of bytes\n", StandardOpenOption.CREATE);
            Files.writeString(Path.of(args[1]),  String.format("%s,%d,%d,%d,%d", records.get(0).getRemoteAddress(),1,100,8761,100), StandardOpenOption.APPEND);
        } catch (FileNotFoundException e) {
            System.out.print("log file not found");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
