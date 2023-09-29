package concurrente.server.serverfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.io.IOException;
import javafx.scene.Group;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        //Loading a font from local file system
        Font myFont =
                Font.loadFont(getClass()
                        .getResourceAsStream("r/serverfx/upheavtt.ttf"), 20);
        Font font = Font.loadFont("file:resources/concurrente/server/serverfx/upheavtt.ttf", 45);
        Text text = new Text(30.0, 75.0, "ABEJA");
        text.setFont(font);
        Group root = new Group(text);
        Scene sc = new Scene(root, 595, 150);
        stage.setTitle("Custom Font");
        stage.setScene(sc);
        stage.show();

      /*  scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();*/
    }

    public static void main(String[] args) {
        launch();
    }
}