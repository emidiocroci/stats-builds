public class Stat {
    public Stat(String remoteAddress, int requests, int data, double requestsPercentage, double dataPercentage) {
        this.remoteAddress = remoteAddress;
        this.requests = requests;
        this.data = data;
        this.requestsPercentage = requestsPercentage;
        this.dataPercentage = dataPercentage;
    }

    String remoteAddress;
    int requests;
    int data;
    double requestsPercentage;
    double dataPercentage;

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
}
