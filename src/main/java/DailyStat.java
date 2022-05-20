import java.util.ArrayList;
import java.util.List;

public class DailyStat extends ArrayList<Stat> {
    private String day;
    private List<Stat> stats;
    public String getDay() {
        return day;
    }
    public DailyStat(String day, List<Stat> stats) {
        this.day = day;
        this.addAll(stats);
    }
    public String toCsv() {
        List<String> entries = new ArrayList<>();
        entries.add("IP Address,Number of requests,Percentage of requests,Total Bytes sent,Percentage of bytes");
        entries.addAll(this.stream().map(stat -> stat.toString(new CsvFormatter()))
                .toList());
        return String.join(System.lineSeparator(), entries);
    }
}
