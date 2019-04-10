package sample;

import sun.misc.IOUtils;

import javax.microedition.io.StreamConnection;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.*;

import javax.microedition.io.StreamConnection;

public class ProcessConnectionThread implements Runnable {

    private StreamConnection mConnection;


    public ProcessConnectionThread(StreamConnection connection) {
        mConnection = connection;
    }

    @Override
    public void run() {
        try {
            // prepare to receive data
            InputStream inputStream = mConnection.openInputStream();

            System.out.println("waiting for input");

            while (true) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                //int temp = inputStream.read();
                String command;
                while ((command = reader.readLine()) != null){
                    processCommand(command);
                    System.out.println(command);
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Process the command from client
     *
     * @param command the command code
     */
    private void processCommand(String command) {
        try {
            Robot robot = new Robot();

            if(command.equals("CLICK")){
                robot.mousePress(InputEvent.BUTTON1_MASK);
                robot.delay(50);
                robot.mouseRelease(InputEvent.BUTTON1_MASK);
            } else {
                char symbol = '$';
                int index = command.indexOf(symbol);
                double first = Double.valueOf(command.substring(0, index));
                double second = Double.valueOf(command.substring(index + 1));

                Point mousePosition = MouseInfo.getPointerInfo().getLocation();

                int x = (int)(mousePosition.getX() + first);
                int y = (int)( mousePosition.getY() + second);

                robot.mouseMove(x, y);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
