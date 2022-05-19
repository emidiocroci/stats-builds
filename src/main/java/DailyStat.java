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
}
