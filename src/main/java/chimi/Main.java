package chimi;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Chimi using FXML.
 */
public class Main extends Application {

    private final Chimi chimi = new Chimi("data/chimi.txt");

    @Override
    public void start(Stage stage) {
        assert chimi != null : "Chimi instance should be initialized before the UI starts";

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setTitle("Chimi");
            fxmlLoader.<MainWindow>getController().setChimi(chimi);  // inject the Chimi instance
            stage.setOnCloseRequest(event -> Platform.exit());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        System.exit(0);
    }
}
