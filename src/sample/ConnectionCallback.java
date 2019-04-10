package sample;

import javax.microedition.io.StreamConnection;

public interface ConnectionCallback {
    void onConnected(StreamConnection connection);
}
