package org.example.client.console;

import java.util.Scanner;

public class ConsoleInputReader {
    private final Scanner scanner = new Scanner(System.in);

    // 必须是 public 方法，名字和 Client 里调用的完全一致
    public String readInput(String prompt) {
        System.out.print(prompt + " > ");
        return scanner.nextLine().trim();
    }
}