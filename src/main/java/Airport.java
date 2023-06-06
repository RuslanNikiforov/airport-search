import java.util.List;

public class Airport {
    private static final int namePosition = 2;
    private String name;
    private List<String> allData;

    public Airport(List<String> allData) {
        this.name = allData.get(namePosition - 1);
        this.allData = allData;
    }

    public Airport() {
    }

    public String getName() {
        return name;
    }

    public List<String> getAllData() {
        return allData;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder =  new StringBuilder();
        stringBuilder.append(name).append("[");
        for(String field : this.allData) {
            stringBuilder.append(field).append(", ");
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
