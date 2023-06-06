import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class AirportSearch {

    private NavigableMap<String, LineParameters> airportNamesWithLineInfo;

    public AirportSearch() {
        airportNamesWithLineInfo = new TreeMap<>();
    }

    public static void main(String[] args) {
        AirportSearch airportSearch = new AirportSearch();
        airportSearch.readFile();
        Scanner sc = new Scanner(System.in);
        System.out.println("Введите фильтр: ");
        String filter = sc.nextLine();
        System.out.println("Введите название аэропорта: ");
        String airportName = sc.nextLine();
        while (!airportName.equals("!quit") & !filter.equals("!quit")) {
            long startTime = System.currentTimeMillis();
            List<Airport> correctAirports = airportSearch.searchInFile(airportName);
            if(filter.length() != 0) {
                for (int i = 0; i < correctAirports.size(); i++) {
                    if (!FilterChain.calculate(filter, correctAirports.get(i))) {
                        correctAirports.remove(i);
                        i--;
                    }
                }
            }
            correctAirports.sort(Comparator.comparing(Airport::getName));
            long endTime = System.currentTimeMillis();
            airportSearch.printData(correctAirports, startTime, endTime);
            System.out.println();
            System.out.println("Введите фильтр: ");
            filter = sc.nextLine();
            System.out.println("Введите название аэропорта: ");
            airportName = sc.nextLine();

        }




    }

    public void printData(List<Airport> correctAirports, long startTime, long endTime) {
        for (Airport airport : correctAirports) {
            System.out.println(airport);
        }
        System.out.println("Количество найденных строк = " + correctAirports.size() +
                ". Время, потраченное на поиск = " + (endTime - startTime) + " мс.");
    }

    public List<Airport> searchInFile(String name) {
        name = name.toUpperCase();
        String keyTo;
        if (name.length() == 1) {
            keyTo = String.valueOf((char) (name.charAt(0) + 1));
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(name, 0, name.length() - 1);
            sb.append((char) (name.charAt(name.length() - 1) + 1));
            keyTo = sb.toString();
        }
        var allFoundedAirports = airportNamesWithLineInfo.
                subMap(name, true, keyTo, false);
        SortedMap<Integer, Long> orderedLinesToRead = new TreeMap<>();
        for (String s : allFoundedAirports.keySet()) {
            var lineParameter = allFoundedAirports.get(s);
            orderedLinesToRead.put(lineParameter.getNumber(), lineParameter.getIndex());
        }
        long currentBytes = 0;
        List<Airport> airports = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Path.of("src/main/resources/airports.csv"))) {
            for (Integer lineNumber : orderedLinesToRead.keySet()) {

                currentBytes += reader.skip(orderedLinesToRead.get(lineNumber) - currentBytes);
                reader.mark(512);
                String line = reader.readLine();
                StringTokenizer tokenizer = new StringTokenizer(line, ",");
                List<String> airportFields = new ArrayList<>();
                while (tokenizer.hasMoreTokens()) {
                    airportFields.add(tokenizer.nextToken());
                }
                Airport airport = new Airport(airportFields);
                airports.add(airport);
                reader.reset();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return airports;
    }

    public void readFile() {
        long countBytes = 0;
        int countLines = 0;
        try (BufferedReader reader = Files.newBufferedReader(Path.of("src/main/resources/airports.csv"))) {
            while (reader.ready()) {
                String currentLine = reader.readLine();
                StringTokenizer tokenizer = new StringTokenizer(currentLine, ",");
                tokenizer.nextToken();
                LineParameters parameters = new LineParameters();
                parameters.setIndex(countBytes);
                parameters.setNumber(++countLines);
                countBytes += currentLine.length() + 1;
                String name = tokenizer.nextToken();
                name = name.substring(1, name.length() - 1);
                airportNamesWithLineInfo.put(name.toUpperCase(), parameters);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
