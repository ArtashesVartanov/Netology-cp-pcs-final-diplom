import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
//        System.out.println(engine.search("бизнес"));
        try (ServerSocket serverSocket = new ServerSocket(8989);) { // стартуем сервер один раз
            while (true) { // в цикле принимаем подключения
                try (
                        Socket socket = serverSocket.accept();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter writer = new PrintWriter(socket.getOutputStream());
                ) {
                    // обработка одного подключения
                    String search = reader.readLine();
                    String answerToBooleanSearchEngine = engine.search(search).toString();
                    writer.print(answerToBooleanSearchEngine);
                }
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }
    }
}