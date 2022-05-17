import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

    @Test
    @DisplayName("should return the size of the dataset")
    void size() {
        List<String> rawData = List.of("1652377677,8765,200,10.98.7.2",
                "1652378293,8761,300,11.93.1.3");
        assertEquals(2, dataSet.loadData(rawData));
        assertEquals(2, dataSet.loadData(rawData));
        assertEquals(4, dataSet.size());
    }

    @Test
    @DisplayName("should return empty stats")
    void emptyStats() {
        dataSet = new DataSet(List.of());
        List<Stat> stats = dataSet.getStatsByRemoteAddress();
        assertEquals(0, stats.size());
    }

    @Nested
    @DisplayName("data sent stats")
    class DataStatsByRemoteAddress {

        @Test
        @DisplayName("should return data sent for 1 record")
        void oneRecordDataSentStats() {
            dataSet = new DataSet(List.of(
                    new Record(1652377677, 200, 8765, "10.98.7.2"))
            );
            List<Stat> stats = dataSet.getStatsByRemoteAddress();
            assertEquals(1, stats.size());
            assertEquals("10.98.7.2", stats.get(0).getRemoteAddress());
            assertEquals(8765, stats.get(0).data);
        }

        @Test
        @DisplayName("should return data sent for multiple records")
        void multipleRecordDataSent() {
            dataSet = new DataSet(List.of(
                    new Record(1652377677, 200, 8765, "10.98.7.2"),
                    new Record(1652377677, 200, 8761, "10.98.7.2"),
                    new Record(1652377677, 200, 900, "10.98.7.2"))
            );
            List<Stat> stats = dataSet.getStatsByRemoteAddress();
            assertEquals(1, stats.size());
            assertEquals("10.98.7.2", stats.get(0).getRemoteAddress());
            assertEquals(18426, stats.get(0).data);
        }

        @Test
        @DisplayName("should return data sent filtering for OK response")
        void multipleRecordDataSentOK() {
            dataSet = new DataSet(List.of(
                    new Record(1652377677, 200, 8765, "10.98.7.2"),
                    new Record(1652377677, 400, 8761, "10.98.7.2"),
                    new Record(1652377677, 200, 900, "10.98.7.2"))
            );
            List<Stat> stats = dataSet.getStatsByRemoteAddress();
            assertEquals(1, stats.size());
            assertEquals("10.98.7.2", stats.get(0).getRemoteAddress());
            assertEquals(9665, stats.get(0).data);
        }
    }
    @Nested
    @DisplayName("data sent stats")
    class RequestStatsByRemoteAddress {

        @Test
        @DisplayName("should return requests for 1 record")
        void oneRecordRequestStats() {
            dataSet = new DataSet(List.of(
                    new Record(1652377677, 200, 8765, "10.98.7.2"))
            );
            List<Stat> stats = dataSet.getStatsByRemoteAddress();
            assertEquals(1, stats.size());
            assertEquals("10.98.7.2", stats.get(0).getRemoteAddress());
            assertEquals(1, stats.get(0).getRequests());
        }

        @Test
        @DisplayName("should return requests for multiple records")
        void multipleRecordRequestStats() {
            dataSet = new DataSet(List.of(
                    new Record(1652377677, 200, 8765, "10.98.7.2"),
                    new Record(1652377677, 200, 8761, "10.98.7.2"))
            );
            List<Stat> stats = dataSet.getStatsByRemoteAddress();
            assertEquals(1, stats.size());
            assertEquals("10.98.7.2", stats.get(0).getRemoteAddress());
            assertEquals(2, stats.get(0).getRequests());
        }

        @Test
        @DisplayName("should return request filtering by OK response")
        void multipleRecordRequestOK() {
            dataSet = new DataSet(List.of(
                    new Record(1652377677, 200, 8765, "10.98.7.2"),
                    new Record(1652377677, 200, 8761, "10.98.7.2"),
                    new Record(1652377677, 201, 8761, "10.98.7.2"))
            );
            List<Stat> stats = dataSet.getStatsByRemoteAddress();
            assertEquals(1, stats.size());
            assertEquals("10.98.7.2", stats.get(0).getRemoteAddress());
            assertEquals(2, stats.get(0).getRequests());
        }
    }
}
