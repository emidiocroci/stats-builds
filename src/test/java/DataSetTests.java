import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataSetTests  {
    DataSet dataSet = new DataSet();
    String newLine = System.getProperty("line.separator");

    String buildMultilineString(){
        return "1652377677,8765,200,10.98.7.2"
                .concat(newLine)
                .concat("1652378293,8761,300,11.93.1.2")
                .concat(newLine)
                .concat("1652378293,8761,300,11.93.1.3")
                .concat(newLine)
                .concat("1652378293,8761,300,11.93.1.1")
                .concat(newLine)
                .concat("1652378293,8761,300,11.93.1.1")
                .concat(newLine)
                .concat("1652378293,8761,300,11.93.1.1")
                .concat(newLine)
                .concat("1652378293,8761,300,11.93.1.3");
    }

    @Test
    void loadDataFromEmptyFile() {
        int result = dataSet.loadData("");
        assertEquals(0, result);
    }

    @Test
    @DisplayName("should load one record")
    void testLoadOneRecord() {
        String data = "1652377677,8765,200,10.98.7.2";
        assertEquals(1, dataSet.loadData(data));
    }

    @Test
    @DisplayName("should load multiple records")
    void test() {
        String data = buildMultilineString();
        assertEquals(7, dataSet.loadData(buildMultilineString()));
    }

    @Test
    @DisplayName("should discard lines with wrong format")
    void loadDataDiscardWrongIpAddress() {
        String rawData = "1652377677,8765,200,10.98.7.2"
                .concat(newLine)
                .concat("1652378293,8761,300,blabla")
                .concat(newLine)
                .concat("1652378293,8761,300,11.93.1.3");
        assertEquals(2, dataSet.loadData(rawData));
    }

    @Test
    @DisplayName("should get all the available records")
    void getRecords() {
        List<Record> testRecords = List.of(new Record(1652377677, 8765, 200,"10.98.7.2"));
        dataSet = new DataSet(testRecords);
    }
}
