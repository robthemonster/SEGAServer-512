import java.io.*;
import java.net.Socket;

public class PrintMessage implements Runnable {
    private Socket socket;
    public PrintMessage(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println(bufferedReader.readLine());
            bufferedReader.close();
            Socket client = new Socket(socket.getInetAddress(), 6969);
            socket.close();
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
            printWriter.print("Message received by server");
            printWriter.flush();
            printWriter.close();
            client.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
