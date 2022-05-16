import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class DataSet {

    List<Record> records;

    public DataSet() {
        records = new ArrayList<>();
    }

    public DataSet(List<Record> records) {
        this.records = records;
    }

    private String[] filterByValidFormat(String[] records) {
        String rowFormat = "^(\\d+),(\\d+),([0-9]{3}),((([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4})$";
        return Arrays.stream(records).filter(r -> r.matches(rowFormat)).toArray(String[]::new);
    }

    private String[] getRawRecords(String rawData) {
        String newLine = System.getProperty("line.separator");
        String[] records = rawData.split(newLine);
        return filterByValidFormat(records);
    }

    public int loadData(String rawData) {
        if (rawData.isEmpty())
            return 0;
        else {
            String[] rawRecords = getRawRecords(rawData);
            return rawRecords.length;
        }
    }
}
