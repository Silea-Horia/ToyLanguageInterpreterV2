import controller.Controller;
import repository.IRepository;
import repository.Repository;
import view.TextMenu;
import view.command.ExitCommand;
import view.command.GetPrgCommand;
import view.command.RunExampleCommand;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Interpreter extends Application {
    @Override
    public void start(String[] args) {
        System.out.println("Hello world!");

        IRepository repository = new Repository("src/files/log.out");
        Controller controller = new Controller(repository);

        TextMenu menu = new TextMenu();

        menu.addCommand(new ExitCommand("0", "exit"));
        menu.addCommand(new GetPrgCommand("1", "get predefined program(1,2,3,4,5,6,7,8)", controller));
        menu.addCommand(new RunExampleCommand("2", "run program", controller));

        menu.show();
    }

    public static void main(String[] args) {
        launch();
    }
}