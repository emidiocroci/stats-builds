import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

public class StatsCollector {
    private DataSet dataSet;
    public StatsCollector(DataSet dataSet) {
        this.dataSet = dataSet;
    }
    public double calculatePercentage(double number, double total) {
        return number * 100 / total;
    }

    private int totalRequestsData() {
        return dataSet
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
        double requestPercentage = calculatePercentage(requests, dataSet.size());
        return new Stat(
                recordEntry.getKey(),
                requests,
                bytesize,
                requestPercentage,
                bytesizePercentage);
    }
    Comparator<Stat> byRequestsAndbytesizeReversed() {
        return Comparator
                .comparingInt(Stat::getRequests)
                .reversed()
                .thenComparing(Comparator
                        .comparingInt(Stat::getByteSize).reversed());
    }
    private List<Stat> collectDailyStat(DataSet dayDataset) {
        Map<String, List<Record>> groupedRecords = dayDataset
                .stream()
                .filter(Record::isOk)
                .collect(groupingBy(Record::getRemoteAddress));
        return groupedRecords
                .entrySet()
                .stream()
                .map(this::getStatByIpAddress)
                .sorted(byRequestsAndbytesizeReversed())
                .toList();
    }
    public String getYesterday() {
        LocalDate date = LocalDate.now().minusDays(1);
        return date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
    public DailyStat collect() {
        String day = getYesterday();
        DataSet datasetsByDay = dataSet.byDay(day);
        return new DailyStat(day, collectDailyStat(datasetsByDay));
    }
}
