package java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.microedition.io.StreamConnection;
import java.io.IOException;
import java.io.InputStream;

public class Main extends Application implements ConnectionCallback {

    private ScreenCaptureThread screenCaptureThread;
    private ProcessConnectionThread processConnectionThread;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Pc pilot");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

        BluetoothConnect();

        //screenCaptureThread = new ScreenCaptureThread(this);
        //screenCaptureThread.start();

    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void onConnected(StreamConnection connection) {
        try {
            InputStream inputStream = connection.openInputStream();
            //screenCaptureThread.setConnection(connection);
            //screenCaptureThread.setInputStream(inputStream);
            processConnectionThread = new ProcessConnectionThread(connection, inputStream);
            Thread thread = new Thread(processConnectionThread);
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onConnectionLost() {
        BluetoothConnect();
    }

    private void BluetoothConnect() {
        Thread connectionThread = new Thread(new BluetoothConnectionThread(this));
        connectionThread.start();
    }
}
