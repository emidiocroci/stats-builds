import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReportTests {
    Report report;
    String newLine = System.getProperty("line.separator");

    @BeforeEach
    void setUp() {
        report = new Report();
    }

    @Test
    @DisplayName("Should load data from an empty string")
    void testLoadDataEmpty() {
        assertEquals(0, report.loadData(""));
    }

    @Test
    @DisplayName("should load one record")
    void testLoadOneRecord() {
        String data = "1652377677,8765,200,10.98.7.2";
        assertEquals(1, report.loadData(data));
    }

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
    @DisplayName("should load multiple records")
    void loadMultipleRecords() {
        String data = buildMultilineString();
        assertEquals(7, report.loadData(data));
    }



    @Test
    @DisplayName("should get ip address stats")
    void getStatsIp() {
        String data = buildMultilineString();

    }
}