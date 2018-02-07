import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args){
        try {
            ServerSocket serverSocket = new ServerSocket(6969);
            InputStreamReader inputStreamReader;
            BufferedReader bufferedInputStream;
            Socket socket;
            while (true){
                socket = serverSocket.accept();
                inputStreamReader = new InputStreamReader(socket.getInputStream());
                bufferedInputStream = new BufferedReader(inputStreamReader);
                System.out.println(bufferedInputStream.readLine());
                bufferedInputStream.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
