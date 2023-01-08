import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ServerApp serverApp = new ServerApp();
        serverApp.start(6666);
    }
}