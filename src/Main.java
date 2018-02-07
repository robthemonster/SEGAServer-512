import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args){
        try {
            ServerSocket serverSocket = new ServerSocket(6969);
            InputStreamReader inputStreamReader;
            OutputStreamWriter outputStreamWriter;
            BufferedReader bufferedInputStream;
            PrintWriter printWriter;
            Socket socket;
            while (true){
                socket = serverSocket.accept();
                inputStreamReader = new InputStreamReader(socket.getInputStream());
                bufferedInputStream = new BufferedReader(inputStreamReader);
                System.out.println(bufferedInputStream.readLine());
                System.out.println("Remote Address :" + socket.getRemoteSocketAddress());
                Socket client = new Socket(socket.getInetAddress(), 6969);
                outputStreamWriter = new OutputStreamWriter(client.getOutputStream());
                printWriter = new PrintWriter(outputStreamWriter);
                printWriter.print("Message received by server.");
                printWriter.flush();
                printWriter.close();
                bufferedInputStream.close();
                socket.close();
                client.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
