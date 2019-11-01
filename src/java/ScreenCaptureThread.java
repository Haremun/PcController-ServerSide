package java;

import javax.imageio.ImageIO;
import javax.microedition.io.StreamConnection;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;

public class ScreenCaptureThread extends Thread {

    private Robot robot;
    private BufferedImage bufferedImage;
    private Rectangle screenRectangle;
    private StreamConnection connection;
    private boolean play = true;

    private ConnectionCallback callback;

    private OutputStream outputStream;
    private InputStream inputStream;

    public ScreenCaptureThread(ConnectionCallback callback) {

        this.callback = callback;


        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenRectangle = new Rectangle(screenSize);
    }


    @Override
    public void run() {
        while (play) {
            if (connection != null) {
                System.out.println("connected");
                try {
                    outputStream = connection.openOutputStream();
                    //inputStream = connection.openInputStream();
                    while (play) {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                        bufferedImage = robot.createScreenCapture(screenRectangle);
                        ImageIO.write(scaleImage(bufferedImage, 0.5), "png", byteArrayOutputStream); //scaling img and write to baos

                        byte[] screen = byteArrayOutputStream.toByteArray();
                        System.out.println("Screen transformed");
                        byte[] size = (screen.length + "\n").getBytes();
                        outputStream.write(size);
                        System.out.println("Size send");


                        String temp = reader.readLine();

                        if (temp != null) {
                            System.out.println(temp);
                            if (temp.equals("OK")) {
                                outputStream.write(screen);
                                System.out.println("Screen send");
                            }
                        }
                        temp = reader.readLine();
                        if (temp != null) {
                            System.out.println(temp);
                        }

                        /*ByteArrayInputStream bis = new ByteArrayInputStream(screen);
                        BufferedImage image = ImageIO.read(bis);
                        ImageIO.write(image, "png", new File("img.png"));*/

                        //play = false;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    callback.onConnectionLost();
                    connection = null;

                    try {
                        outputStream.close();
                        inputStream.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                }


            }
            System.out.println("no connected");
        }


    }

    public void setConnection(StreamConnection connection) {
        this.connection = connection;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    private BufferedImage scaleImage(BufferedImage bufferedImage, double scale) {

        BufferedImage newImage = new BufferedImage(
                (int) (bufferedImage.getWidth() * scale),
                (int) (bufferedImage.getHeight() * scale),
                BufferedImage.TYPE_INT_ARGB);

        AffineTransform at = new AffineTransform();
        at.scale(scale, scale);
        AffineTransformOp scaleOp =
                new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        newImage = scaleOp.filter(bufferedImage, newImage);
        return newImage;
    }
}
