import java.util.ArrayList;
import java.util.List;

public class Report {
    List<Record> records = new ArrayList<>();

    public Report() {
    }

    int loadData(String content) {
        String newLine = System.getProperty("line.separator");

        if (content.isEmpty())
            return 0;
        else {
            String[] records = content.split(newLine);
            return records.length;
        }
    }

}
