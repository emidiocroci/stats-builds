public class Stat {
    public Stat(String remoteAddress, int requests, int byteSize, double requestsPercentage, double dataPercentage) {
        this.remoteAddress = remoteAddress;
        this.requests = requests;
        this.byteSize = byteSize;
        this.requestsPercentage = requestsPercentage;
        this.dataPercentage = dataPercentage;
    }

    String remoteAddress;
    private int requests;
    private int byteSize;
    private double requestsPercentage;
    private double dataPercentage;

    public Stat() {

    }

    public String getRemoteAddress() {
        return remoteAddress;
    }
    public double getRequestsPercentage() {
        return requestsPercentage;
    }

    public double getDataPercentage() {
        return dataPercentage;
    }

    public int getRequests() {
        return requests;
    }

    public int getByteSize() {
        return byteSize;
    }

    public String toString(IFormatter formatter) {
        return formatter.format(this);
    }
}
