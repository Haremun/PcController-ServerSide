package sample;

import javax.imageio.ImageIO;
import javax.microedition.io.StreamConnection;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ScreenCaptureThread extends Thread {

    private Robot robot;
    private BufferedImage bufferedImage;
    private Rectangle screenRectangle;
    private StreamConnection connection;

    public ScreenCaptureThread() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenRectangle = new Rectangle(screenSize);
    }

    @Override
    public synchronized void start() {

        if (connection != null){
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                bufferedImage = robot.createScreenCapture(screenRectangle);
                ImageIO.write(bufferedImage, "png", byteArrayOutputStream);

                OutputStream outputStream = connection.openOutputStream();
                byteArrayOutputStream.writeTo(outputStream);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void setConnection(StreamConnection connection) {
        this.connection = connection;
    }
}
