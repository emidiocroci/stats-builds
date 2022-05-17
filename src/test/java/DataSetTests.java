import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataSetTests {
    DataSet dataSet = new DataSet();
    String newLine = System.getProperty("line.separator");

    List<String> buildMultilineString() {
        return List.of(
                "1652377677,8765,200,10.98.7.2",
                "1652378293,8761,300,11.93.1.2",
                "1652378293,8761,300,11.93.1.3",
                "1652378293,8761,300,11.93.1.1",
                "1652378293,8761,300,11.93.1.1",
                "1652378293,8761,300,11.93.1.1",
                "1652378293,8761,300,11.93.1.3");
    }

    @Test
    void loadDataFromEmptyFile() {
        int result = dataSet.loadData(List.of());
        assertEquals(0, result);
    }

    @Test
    @DisplayName("should load one record")
    void testLoadOneRecord() {
        assertEquals(1, dataSet.loadData(List.of("1652377677,8765,200,10.98.7.2")));
    }

    @Test
    @DisplayName("should load multiple records")
    void test() {
        assertEquals(7, dataSet.loadData(buildMultilineString()));
    }

    @Test
    @DisplayName("should discard lines with wrong format")
    void loadDataDiscardWrongIpAddress() {
        List<String> rawData = List.of("1652377677,8765,200,10.98.7.2",
                "1652378293,8761,300,blabla",
                "1652378293,8761,300,11.93.1.3");
        assertEquals(2, dataSet.loadData(rawData));
    }

    @Test
    @DisplayName("should return the size of the dataset")
    void size() {
        List<String> rawData = List.of("1652377677,8765,200,10.98.7.2",
                "1652378293,8761,300,11.93.1.3");
        assertEquals(2, dataSet.loadData(rawData));
        assertEquals(2, dataSet.loadData(rawData));
        assertEquals(4, dataSet.size());
    }
}
