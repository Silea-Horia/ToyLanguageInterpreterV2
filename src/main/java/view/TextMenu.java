package view;

import view.command.Command;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TextMenu {
    private Map<String, Command> commands;

    public TextMenu() {
        this.commands = new HashMap<>();
    }

    public void addCommand(Command c) {
        this.commands.put(c.getKey(), c);
    }

    private void printMenu() {
        for (Command c : this.commands.values()) {
            String line = String.format("%4s : %s", c.getKey(), c.getDescription());
            System.out.println(line);
        }
    }

    public void show() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            this.printMenu();
            System.out.print(">>");
            String key = scanner.nextLine();
            Command c = this.commands.get(key);
            if (c == null) {
                System.out.println("Command not found");
                continue;
            }
            c.execute();
        }
    }
}
