package citydirectory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class ConsoleApp {

    private final Scanner scanner = new Scanner(System.in, "UTF-8");
    private final StatisticsService stats = new StatisticsService();

    public void start() {
        System.out.println("=== СПРАВОЧНИК ГОРОДОВ: CSV / XML ===");
        System.out.println("Введите путь к файлу или exit для выхода.");

        while (true) {
            System.out.print("\nПуть к файлу: ");
            if (!scanner.hasNextLine()) {
                break;
            }

            String input = scanner.nextLine().trim();
            if (isExitCommand(input)) {
                System.out.println("Программа завершена.");
                return;
            }

            if (input.isEmpty()) {
                System.out.println("Путь не должен быть пустым.");
                continue;
            }

            processFile(stripQuotes(input));
        }
    }

    private boolean isExitCommand(String input) {
        return input.equalsIgnoreCase("exit")
                || input.equalsIgnoreCase("quit")
                || input.equalsIgnoreCase("выход");
    }

    private String stripQuotes(String value) {
        if (value.length() >= 2 && value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1).trim();
        }
        return value;
    }

    private void processFile(String rawPath) {
        Path path;
        try {
            path = Path.of(rawPath);
        } catch (InvalidPathException e) {
            System.out.println("Некорректный путь к файлу: " + e.getMessage());
            return;
        }

        if (!Files.isRegularFile(path)) {
            System.out.println("Файл не найден. Проверьте путь.");
            return;
        }

        String fileName = path.getFileName().toString().toLowerCase(Locale.ROOT);
        AddressParser parser;

        if (fileName.endsWith(".csv")) {
            parser = new CsvAddressParser();
        } else if (fileName.endsWith(".xml")) {
            parser = new XmlAddressParser();
        } else {
            System.out.println("Поддерживаются только CSV и XML.");
            return;
        }

        long start = System.nanoTime();
        try {
            List<AddressRecord> records = parser.parse(path);
            long elapsedMs = (System.nanoTime() - start) / 1_000_000;
            stats.printStatistics(records, elapsedMs);
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Не удалось обработать файл: " + e.getMessage());
        }
    }
}
