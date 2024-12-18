package view.command;

import controller.Controller;
import model.exception.ControllerException;

import java.util.Scanner;

public class GetPrgCommand extends Command {
    private int opt;
    private Controller controller;

    public GetPrgCommand(String key, String description, Controller controller) {
        super(key, description);
        this.controller = controller;
    }

    @Override
    public void execute() {
        Scanner input = new Scanner(System.in);
        int opt = input.nextInt();
        try {
            this.controller.generateInitialState(opt);
        } catch (ControllerException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
}
