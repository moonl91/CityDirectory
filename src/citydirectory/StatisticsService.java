package citydirectory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class StatisticsService {

    /**
     * @param records 
     * @param timeMs  
     */

    public void printStatistics(List<AddressRecord> records, long timeMs) {
        System.out.println("\n========== СТАТИСТИКА ==========");
        System.out.println("Всего корректных записей: " + records.size());

        printDuplicates(records);
        printFloorsByCity(records);

        System.out.println("\nВремя обработки файла: " + timeMs + " мс");
        System.out.println("================================");
    }

    private void printDuplicates(List<AddressRecord> records) {
        Map<AddressRecord, Integer> counts = new LinkedHashMap<>();
        for (AddressRecord record : records) {
            counts.put(record, counts.getOrDefault(record, 0) + 1);
        }

        System.out.println("\nДублирующиеся записи:");
        boolean found = false;
        for (Map.Entry<AddressRecord, Integer> entry : counts.entrySet()) {
            if (entry.getValue() > 1) {
                System.out.println(entry.getKey() + " - повторений: " + entry.getValue());
                found = true;
            }
        }

        if (!found) {
            System.out.println("Дубликатов нет.");
        }
    }

    private void printFloorsByCity(List<AddressRecord> records) {
        Map<String, int[]> cities = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        for (AddressRecord record : records) {
            int[] floors = cities.computeIfAbsent(record.getCity(), k -> new int[5]);
            int floor = record.getFloor();
            if (floor >= 1 && floor <= 5) {
                floors[floor - 1]++;
            }
        }

        System.out.println("\nКоличество зданий по этажности в каждом городе:");
        for (Map.Entry<String, int[]> entry : cities.entrySet()) {
            int[] n = entry.getValue();
            System.out.println(
                    entry.getKey()
                            + ": 1 этаж - " + n[0]
                            + ", 2 этажа - " + n[1]
                            + ", 3 этажа - " + n[2]
                            + ", 4 этажа - " + n[3]
                            + ", 5 этажей - " + n[4]
            );
        }
    }
}
