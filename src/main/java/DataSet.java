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

    private int totalRequestsData() {
        return records
                .stream()
                .map(Record::getByteSize)
                .reduce(Math::addExact)
                .get();
    }

    private int bytesizeByIp(List<Record> ipRecords) {
        return ipRecords
                .stream()
                .map(Record::getByteSize)
                .reduce(Math::addExact)
                .get();
    }

    private Stat getStatByIpAddress(Map.Entry<String, List<Record>> recordEntry) {
        int totalData = totalRequestsData();
        int bytesize = bytesizeByIp(recordEntry.getValue());
        int requests = recordEntry
                .getValue()
                .size();
        double bytesizePercentage = calculatePercentage(bytesize, totalData);
        double requestPercentage = calculatePercentage(requests, this.size());
        return new Stat(
                recordEntry.getKey(),
                requests,
                bytesize,
                requestPercentage,
                bytesizePercentage);
    }

    public List<Stat> getStatsByRemoteAddress() {
        if (records.isEmpty())
            return List.of();
        Map<String, List<Record>> groupedRecords = records
                .stream()
                .filter(Record::isOk)
                .collect(groupingBy(Record::getRemoteAddress));
        return groupedRecords
                .entrySet()
                .stream()
                .map(this::getStatByIpAddress)
                .toList();
    }
}
