import java.util.*;
import java.util.function.Predicate;

import static java.util.stream.Collectors.groupingBy;

public class DataSet {
    List<Record> records;
    public DataSet() {
        records = new ArrayList<>();
    }
    public DataSet(List<Record> records) {
        this.records = new ArrayList<>();
        this.records.addAll(records);
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
    public double calculatePercentage(double obtained, double total) {
        return obtained * 100 / total;
    }
    public List<Stat> getStatsByRemoteAddress() {
        if (records.isEmpty())
            return List.of();
        Map<String, List<Record>> groupedRecords = records
                .stream()
                .filter(Record::isOk)
                .collect(groupingBy(Record::getRemoteAddress));
        String key = records.get(0).getRemoteAddress();
        List<Stat> stats = new ArrayList<>();
        int totalData = groupedRecords
                .get(key)
                .stream()
                .map(Record::getByteSize)
                .reduce((prev, cur) -> prev + cur).get();
        int totalRequests = groupedRecords.get(key).size();
        stats.add(new Stat(
                key,
                totalRequests,
                totalData,
                0,
                0));
        return stats;
    }
}
