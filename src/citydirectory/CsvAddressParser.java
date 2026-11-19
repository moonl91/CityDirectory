package citydirectory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CsvAddressParser implements AddressParser {

    @Override
    public List<AddressRecord> parse(Path path) throws IOException {
        List<AddressRecord> result = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            int lineNo = 0;

            while ((line = reader.readLine()) != null) {
                lineNo++;
                if (line.isBlank()) {
                    continue;
                }

                List<String> columns = split(line);
                if (lineNo == 1 && !columns.isEmpty() && columns.get(0).equalsIgnoreCase("city")) {
                    continue;
                }

                if (columns.size() != 4) {
                    System.out.println("Пропущена строка " + lineNo + ": ожидалось 4 столбца.");
                    continue;
                }

                try {
                    String city = columns.get(0).trim();
                    String street = columns.get(1).trim();
                    String house = columns.get(2).trim();
                    int floor = Integer.parseInt(columns.get(3).trim());

                    if (city.isEmpty() || street.isEmpty() || house.isEmpty()) {
                        System.out.println("Пропущена строка " + lineNo + ": пустое поле.");
                        continue;
                    }

                    result.add(new AddressRecord(city, street, house, floor));
                } catch (NumberFormatException e) {
                    System.out.println("Пропущена строка " + lineNo + ": этажность должна быть числом.");
                }
            }
        }

        return result;
    }

    private List<String> split(String line) {
        List<String> out = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean quoted = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (ch == '"') {
                if (quoted && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    quoted = !quoted;
                }
            } else if (ch == ';' && !quoted) {
                out.add(current.toString().trim());
                current.setLength(0);
            } else {
                current.append(ch);
            }
        }

        out.add(current.toString().trim());
        return out;
    }
}
