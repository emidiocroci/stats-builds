import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StatsCollectorTest {
    StatsCollector statsCollector;

    @BeforeEach
    void setUp() {
        statsCollector = new StatsCollector(new DataSet());
    }

    Map.Entry<Long, String> getYesterday() {
        LocalDate date = LocalDate.now().minusDays(1);
        return Map.entry(Long.valueOf(date.toEpochSecond(LocalTime.MIDNIGHT, ZoneOffset.MIN)),
                date.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
    }

    Map.Entry<Long, String> getToday() {
        LocalDate date = LocalDate.now();
        return Map.entry(Long.valueOf(date.toEpochSecond(LocalTime.MIDNIGHT, ZoneOffset.MIN)),
                date.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
    }

    @Nested
    @DisplayName("collect")
    class Collect {
        @Test
        void emptySet() {
            statsCollector = new StatsCollector(new DataSet());
            assertEquals(0, statsCollector.collect().size());
        }

        @Nested
        @DisplayName("byteSize")
        class ByteSize {
            @Test
            @DisplayName("should return data sent for 1 record")
            void oneRecord() {
                Map.Entry<Long, String> date = getYesterday();
                statsCollector = new StatsCollector(new DataSet(List.of(
                        new Record(date.getKey(),200, 8765, "10.98.7.2"))
                ));
                DailyStat stats = statsCollector.collect();
                assertEquals(1, stats.size());
                assertEquals(date.getValue(), stats.getDay());
                assertEquals(1, stats.size());
                assertEquals("10.98.7.2", stats.get(0).getRemoteAddress());
                assertEquals(8765, stats.get(0).getByteSize());
            }

            @Test
            @DisplayName("should return data sent for multiple records")
            void multipleRecords() {
                Map.Entry<Long, String> date = getYesterday();
                statsCollector = new StatsCollector(new DataSet(List.of(
                        new Record(date.getKey(), 200, 8765, "10.98.7.2"),
                        new Record(date.getKey(), 200, 8761, "10.98.7.2"),
                        new Record(date.getKey(), 200, 900, "10.98.7.2"))
                ));
                DailyStat stats = statsCollector.collect();
                assertEquals(1, stats.size());
                assertEquals(18426, stats.get(0).getByteSize());
            }

            @Test
            @DisplayName("should return data sent filtering for OK response")
            void multipleRecordsOnlyOK() {
                Map.Entry<Long, String> date = getYesterday();
                statsCollector = new StatsCollector(new DataSet(List.of(
                        new Record(date.getKey(), 200, 8765, "10.98.7.2"),
                        new Record(date.getKey(), 400, 8761, "10.98.7.2"),
                        new Record(date.getKey(), 200, 900, "10.98.7.2"))
                ));
                DailyStat stats = statsCollector.collect();
                assertEquals(1, stats.size());
                assertEquals(9665, stats.get(0).getByteSize());
            }

            @Test
            @DisplayName("should return the percentage of total data amount")
            void percentage() {
                Map.Entry<Long, String> date = getYesterday();
                statsCollector = new StatsCollector(new DataSet(List.of(
                        new Record(date.getKey(), 200, 8765, "10.98.7.2"),
                        new Record(date.getKey(), 400, 8761, "10.98.7.2"),
                        new Record(date.getKey(), 200, 900, "10.98.7.2"))
                ));
                DailyStat stats = statsCollector.collect();
                assertEquals(1, stats.size());
                assertEquals(52.45305546510366, stats.get(0).getDataPercentage());
            }
        }

        @Nested
        class Requests {
            @Test
            @DisplayName("should return requests for 1 record")
            void oneRecord() {
                Map.Entry<Long, String> date = getYesterday();
                statsCollector = new StatsCollector(new DataSet(List.of(
                        new Record(date.getKey(),200, 8765, "10.98.7.2"))
                ));
                DailyStat stats = statsCollector.collect();
                assertEquals(1, stats.size());
                assertEquals(1, stats.get(0).getRequests());
            }
            @Test
            @DisplayName("should return requests for multiple records")
            void multipleRecords() {
                Map.Entry<Long, String> date = getYesterday();
                statsCollector = new StatsCollector(new DataSet(List.of(
                        new Record(date.getKey(), 200, 8765, "10.98.7.2"),
                        new Record(date.getKey(), 200, 8761, "10.98.7.2"),
                        new Record(date.getKey(), 200, 900, "10.98.7.2"))
                ));
                DailyStat stats = statsCollector.collect();
                assertEquals(1, stats.size());
                assertEquals(3, stats.get(0).getRequests());
            }

            @Test
            @DisplayName("should return request filtering by OK response")
            void multipleRecordRequestOK() {
                Map.Entry<Long, String> date = getYesterday();
                statsCollector = new StatsCollector(new DataSet(List.of(
                        new Record(date.getKey(), 200, 8765, "10.98.7.2"),
                        new Record(date.getKey(), 400, 8761, "10.98.7.2"),
                        new Record(date.getKey(), 200, 900, "10.98.7.2"))
                ));
                DailyStat stats = statsCollector.collect();
                assertEquals(1, stats.size());
                assertEquals(2, stats.get(0).getRequests());
            }

            @Test
            @DisplayName("should return the percentage of total data amount")
            void percentage() {
                Map.Entry<Long, String> date = getYesterday();
                statsCollector = new StatsCollector(new DataSet(List.of(
                        new Record(date.getKey(), 200, 8765, "10.98.7.2"),
                        new Record(date.getKey(), 400, 8761, "10.98.7.2"),
                        new Record(date.getKey(), 200, 900, "10.98.7.2"))
                ));
                DailyStat stats = statsCollector.collect();
                assertEquals(1, stats.size());
                assertEquals(66.66666666666667, stats.get(0).getRequestsPercentage());
            }
        }
        @Test
        void multipleResult() {
            Map.Entry<Long, String> date = getYesterday();
            statsCollector = new StatsCollector(new DataSet(List.of(
                    new Record(date.getKey(), 200, 876, "10.98.7.2"),
                    new Record(date.getKey(), 200, 87652, "10.98.7.1"),
                    new Record(date.getKey(), 200, 87, "10.98.7.2"),
                    new Record(date.getKey(), 200, 8763, "10.98.7.3"),
                    new Record(date.getKey(), 200, 8760, "10.98.7.3"),
                    new Record(date.getKey(), 200, 87655, "10.98.7.4"),
                    new Record(date.getKey(), 400, 8761, "10.98.7.22"),
                    new Record(date.getKey(), 400, 8761, "10.98.7.3"),
                    new Record(date.getKey(), 200, 900, "10.98.7.2"))
            ));
            DailyStat stats = statsCollector.collect();
            assertEquals(4, stats.size());
            assertEquals("10.98.7.2", stats.get(0).getRemoteAddress());
            assertEquals(1863, stats.get(0).getByteSize());
            assertEquals(33.333333333333336, stats.get(0).getRequestsPercentage());
        }
        @Test
        void multipleResultInMultipleDays() {
            Map.Entry<Long, String> yDate = getYesterday();
            Map.Entry<Long, String> tDate = getToday();
            statsCollector = new StatsCollector(new DataSet(List.of(
                    new Record(yDate.getKey(), 200, 876, "10.98.7.2"),
                    new Record(yDate.getKey(), 200, 87652, "10.98.7.1"),
                    new Record(yDate.getKey(), 200, 87, "10.98.7.2"),
                    new Record(yDate.getKey(), 200, 8763, "10.98.7.3"),
                    new Record(tDate.getKey(), 200, 8760, "10.98.7.3"),
                    new Record(yDate.getKey(), 200, 87655, "10.98.7.4"),
                    new Record(yDate.getKey(), 400, 8761, "10.98.7.22"),
                    new Record(tDate.getKey(), 400, 8761, "10.98.7.3"),
                    new Record(tDate.getKey(), 200, 900, "10.98.7.2"))
            ));
            DailyStat stats = statsCollector.collect();
            assertEquals(4, stats.size());
            assertEquals("10.98.7.2", stats.get(0).getRemoteAddress());
            assertEquals(963, stats.get(0).getByteSize());
            assertEquals(22.22222222222222, stats.get(0).getRequestsPercentage());
        }
    }
}