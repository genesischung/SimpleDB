import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class DBEditor extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("DB Editor");
        primaryStage.setScene(new Scene(root, 960 , 720));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
