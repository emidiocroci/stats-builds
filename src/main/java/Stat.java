public class Stat {
    public Stat(String remoteAddress, int requests, int data, double requestsPercentage, double dataPercentage) {
        this.remoteAddress = remoteAddress;
        this.requests = requests;
        this.data = data;
        this.requestsPercentage = requestsPercentage;
        this.dataPercentage = dataPercentage;
    }

    String remoteAddress;
    private int requests;
    private int data;
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

    public int getData() {
        return data;
    }

    public String toString(IFormatter formatter) {
        return formatter.format(this);
    }
}
