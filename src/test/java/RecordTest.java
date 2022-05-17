import org.junit.jupiter.api.*;

import java.util.IllegalFormatException;
import static org.junit.jupiter.api.Assertions.*;

class RecordTest {

    @Test
    public void fromNull() {
        Exception thrown = Assertions.assertThrows(Exception.class, () -> {
            Record.fromString(null);
        });
        Assertions.assertEquals("invalid format", thrown.getMessage());
    }
    @Test
    public void fromEmptyString() {
        Exception thrown = Assertions.assertThrows(Exception.class, () -> {
            Record.fromString("");
        });
        Assertions.assertEquals("invalid format", thrown.getMessage());
    }
    @Test
    public void fromWrongIpFormat() {
        Exception thrown = Assertions.assertThrows(Exception.class, () -> {
            Record.fromString("1652378293,8761,300,blabla");
        });
        Assertions.assertEquals("invalid format", thrown.getMessage());
    }
    @Test
    public void fromWrongTimestampFormat() {
        Exception thrown = Assertions.assertThrows(Exception.class, () -> {
            Record.fromString("blabla,8761,300,84.2.2.2");
        });
        Assertions.assertEquals("invalid format", thrown.getMessage());
    }
    @Test
    public void fromWrongSizeFormat() {
        Exception thrown = Assertions.assertThrows(Exception.class, () -> {
            Record.fromString("1652378293,blabla,300,84.2.2.2");
        });
        Assertions.assertEquals("invalid format", thrown.getMessage());
    }
    @Test
    public void fromWrongStatusCodeFormat() {
        Exception thrown = Assertions.assertThrows(Exception.class, () -> {
            Record.fromString("1652378293,8761,900,84.2.2.2");
        });
        Assertions.assertEquals("invalid format", thrown.getMessage());
    }
    @Test
    public void fromString() throws Exception {
        Record record = Record.fromString("1652378293,8761,300,84.2.2.2");
        assertEquals(8761, record.getByteSize());
        assertEquals(300, record.getStatus());
        assertEquals("84.2.2.2", record.getRemoteAddress());
        assertEquals(1652378293, record.getTimeStamp());
    }

    @Test
    public void isOk() {
        Record record = new Record(1652378293, 200, 123, "89.1.1.1");
        assertTrue(record.isOk());
    }

    @Test
    public void is300NotOk() {
        Record record = new Record(1652378293, 300, 123, "89.1.1.1");
        assertFalse(record.isOk());
    }

    @Test
    public void is401NotOk() {
        Record record = new Record(1652378293, 401, 123, "89.1.1.1");
        assertFalse(record.isOk());
    }
}