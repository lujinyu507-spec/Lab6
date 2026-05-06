package org.example.client.console;

import org.example.common.model.Coordinates;
import org.example.common.model.Location;
import org.example.common.model.Route;

import java.time.ZonedDateTime;
import java.util.Scanner;

public class ConsoleInputReader {
    private final Scanner scanner;

    public ConsoleInputReader(Scanner scanner) {
        this.scanner = scanner;
    }

    public Route readRoute() {
        String name = readNonEmptyLine("Enter name: ");
        Long x = readLong("Enter coordinate X: ");
        Long y = readLong("Enter coordinate Y: ");
        Coordinates coordinates = new Coordinates(x, y);

        Long fromX = readLong("Enter 'from' X: ");
        Long fromY = readLong("Enter 'from' Y: ");
        String fromName = readNonEmptyLine("Enter 'from' location name: ");
        Location from = new Location(fromX, fromY, fromName);

        Long toX = readLong("Enter 'to' X: ");
        Long toY = readLong("Enter 'to' Y: ");
        String toName = readNonEmptyLine("Enter 'to' location name: ");
        Location to = new Location(toX, toY, toName);

        Float distance = readDistance("Enter distance (>1): ");

        return new Route(-1L, name, coordinates, ZonedDateTime.now(), from, to, distance);
    }

    private String readNonEmptyLine(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
        } while (input.isEmpty());
        return input;
    }

    private Long readLong(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Long.parseLong(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number, try again.");
            }
        }
    }

    private Float readDistance(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                float d = Float.parseFloat(scanner.nextLine().trim());
                if (d > 1) return d;
                System.out.println("Distance must be >1, try again.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid number, try again.");
            }
        }
    }
}