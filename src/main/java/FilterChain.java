import java.util.ArrayList;
import java.util.List;

public class FilterChain {


    public static boolean calculate(String filter, Airport airport) {
        boolean result1;
        boolean result2;
        if(filter.contains("||"))  {
            result1 = calculate(filter.substring(0, filter.indexOf("||")), airport);
            result2 = calculate(filter.substring(filter.indexOf("||") + 2), airport);
            return result1 || result2;
        }
        else if(filter.contains("&")) {
            result1 = calculate(filter.substring(0, filter.indexOf("&")), airport);
            result2 = calculate(filter.substring(filter.indexOf("&") + 1), airport);
            return result1 & result2;
        }
        else {
            Filter simpleFilter = new Filter(filter);
            simpleFilter.setValueFromFile(airport.getAllData().get(simpleFilter.getColumnNumber() - 1));
            try {
                return simpleFilter.calculate();
            }
            catch (RuntimeException e) {
                return false;
            }
        }
    }
}
