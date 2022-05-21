import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class CsvFormatter implements IFormatter {
    private String formatPercentage(double percentage) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(percentage);
    }
    @Override
    public String format(Stat stat) {
        return String.format(
                "%s;%d;%s;%d;%s",
                stat.getRemoteAddress(),
                stat.getRequests(),
                formatPercentage(stat.getRequestsPercentage()),
                stat.getByteSize(),
                formatPercentage(stat.getDataPercentage()));
    }
}
