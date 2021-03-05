import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class TestSocket {
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Socket socket = new Socket("localhost", 3456);
                        OutputStream out = socket.getOutputStream();
                        out.write(new byte[1000]);
                        out.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
