import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CsvFormatterTest {
    IFormatter formatter = new CsvFormatter();

    @Test
    public void format() {
        Stat stat = new Stat("8.8.8.8", 45, 2345, 50.00, 50.45);
        assertEquals("8.8.8.8,45,50,2345,50.45", formatter.format(stat));
    }

    @Test
    public void formatRemovingTrailingZero() {
        Stat stat = new Stat("8.8.8.8", 45, 2345, 50.00, 50.45);
        assertEquals("8.8.8.8,45,50,2345,50.45", formatter.format(stat));
    }

    @Test
    public void formatLongBytesizePercentage() {
        Stat stat = new Stat("8.8.8.8", 45, 2345, 50, 50.459);
        assertEquals("8.8.8.8,45,50,2345,50.46", formatter.format(stat));
    }

    @Test
    public void formatLongRequestPercentage() {
        Stat stat = new Stat("8.8.8.8", 45, 2345, 50.1111, 50.46);
        assertEquals("8.8.8.8,45,50.12,2345,50.46", formatter.format(stat));
    }
}