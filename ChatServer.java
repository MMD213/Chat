import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ChatServer implements TCPConectionListener {
    public static void main(String[] args) {
    new ChatServer();
    }
    private final ArrayList <TCPConection> conections = new ArrayList<>();
    private ChatServer (){
        System.out.println("Server running...");
            try(ServerSocket serverSocket = new ServerSocket(80)){
                while (true){
                    try {
                        new TCPConection(this, serverSocket.accept());
                    } catch (IOException e){
                        System.out.println("TCPConecnnection exception:" + e);
                    }
                }
            } catch (IOException e){
                throw  new RuntimeException(e);
            }
        }

    @Override
    public synchronized void onConnectionReady(TCPConection tcpConection) {
        conections.add(tcpConection);
        sendToAllConnection("Client connected: " + tcpConection);
    }

    @Override
    public  synchronized void onReceiveString(TCPConection tcpConection, String value) {
        sendToAllConnection(value);
    }

    @Override
    public synchronized void onDisconect(TCPConection tcpConection) {
        conections.remove(tcpConection);
        sendToAllConnection("Client disconnected: " + tcpConection);
    }

    @Override
    public synchronized void onExeption(TCPConection tcpConection, Exception e) {
        System.out.println("TCPConection exeption:" + e);
    }
    private void sendToAllConnection(String value){
        System.out.println(value);
        final  int cnt = conections.size();
        for (int i = 0; i< cnt;i++){
            conections.get(i).sendString(value);
        }
    }
}

