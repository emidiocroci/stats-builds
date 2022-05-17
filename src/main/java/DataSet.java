import java.util.*;
import java.util.function.Predicate;

public class DataSet {
    List<Record> records;
    public DataSet() {
        records = new ArrayList<>();
    }
    private List<Record> getRecordsFromRawData(List<String> rawData) {
        return rawData
                .stream()
                .map((String rawRecord) -> {
                    try {
                        return Record.fromString(rawRecord);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Predicate.not(Objects::isNull))
                .toList();
    }
    public int loadData(List<String> rawData) {
        List<Record> foundRecords = getRecordsFromRawData(rawData);
        records.addAll(foundRecords);
        return foundRecords.size();
    }
    public int size() {
        return records.size();
    }
}
