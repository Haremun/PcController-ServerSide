import javax.microedition.io.StreamConnection;

public interface ConnectionCallback {
    void onConnected(StreamConnection connection);
    void onConnectionLost();
}
