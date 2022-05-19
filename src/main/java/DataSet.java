import java.util.*;
import java.util.function.Predicate;

import static java.util.stream.Collectors.groupingBy;

public class DataSet extends ArrayList<Record> {

    public DataSet() {
        super();
    }

    public DataSet(List<Record> records) {
        super();
        this.addAll(records);
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
        this.addAll(foundRecords);
        return foundRecords.size();
    }

    public DataSet byDay(String day) {
        Map<String, List<Record>> dailyStats = this
                .stream()
                .collect(groupingBy(Record::getDay));
        if (dailyStats.containsKey(day))
            return new DataSet(dailyStats.get(day));
        else
            return new DataSet();
    }
}
