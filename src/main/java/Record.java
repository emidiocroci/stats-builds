import java.net.http.HttpRequest;
import java.text.ParseException;
import java.util.IllegalFormatException;

public class Record {
    public Record() { }
    public Record(int timeStamp, int status, int byteSize, String remoteAddress) {
        this.timeStamp = timeStamp;
        this.status = status;
        this.byteSize = byteSize;
        this.remoteAddress = remoteAddress;
    }

    int timeStamp;
    int status;
    int byteSize;
    String remoteAddress;

    public int getTimeStamp() {
        return timeStamp;
    }

    public int getStatus() {
        return status;
    }

    public int getByteSize() {
        return byteSize;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    private static Boolean isOfValidFormat(String rawRecord) {
        String rowFormat = "^(\\d+),(\\d+),([1-5]\\d{2}),((([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4})$";
        return rawRecord != null && rawRecord.matches(rowFormat);
    }

    static Record fromString(String rawRecord) throws Exception {
        if (!isOfValidFormat(rawRecord)) {
            throw new Exception("invalid format");
        }
        String[] fields = rawRecord.split(",");
        return new Record(
                Integer.parseInt(fields[0]),
                Integer.parseInt(fields[2]),
                Integer.parseInt(fields[1]),
                fields[3]
        );
    }

    public boolean isOk() {
        return status == 200;
    }

}
