package view.command;

import controller.Controller;
import model.exception.ControllerException;

public class RunExampleCommand extends Command {
    private Controller controller;

    public RunExampleCommand(String key, String description, Controller controller) {
        super(key, description);
        this.controller = controller;
    }

    @Override
    public void execute() {
        try {
            this.controller.allStep();
        } catch (ControllerException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
}
