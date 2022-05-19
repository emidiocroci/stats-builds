import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataSetTests {
    DataSet dataSet = new DataSet();

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

    @Nested
    @DisplayName("loadData")
    class LoadData {
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

    }

    @Nested
    @DisplayName("byDay")
    class ByDay {
        @Test
        void empty() {
            assertEquals(new DataSet(), dataSet.byDay("20220518"));
        }

        @Test
        void byDay() {
            dataSet = new DataSet(List.of(
                    new Record(1652907838, 200, 876, "10.98.7.2"),
                    new Record(1652378293, 200, 87652, "10.98.7.1"),
                    new Record(1652907838, 200, 87, "10.98.7.2"),
                    new Record(1652378293, 200, 8763, "10.98.7.3"),
                    new Record(1652907838, 200, 8760, "10.98.7.3"),
                    new Record(1652378293, 200, 87655, "10.98.7.4"),
                    new Record(1652907838, 400, 8761, "10.98.7.22"),
                    new Record(1652378293, 400, 8761, "10.98.7.3"),
                    new Record(1652907838, 200, 900, "10.98.7.2"))
            );
            assertEquals(5, dataSet.byDay("20220518").size());
        }
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
