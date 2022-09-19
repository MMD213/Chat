import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

public class TCPConection {
    private  final Socket socket;
    private  final  Thread rxThread;
    private final TCPConectionListener eventListener;
    private final BufferedReader in;
    private final BufferedWriter out;
    public TCPConection(TCPConectionListener eventListener,String ipAddr,int port)throws IOException{
        this(eventListener,new Socket(ipAddr,port));

    }
    public TCPConection(TCPConectionListener eventListener,Socket socket) throws IOException {
        this.eventListener = eventListener;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),Charset.forName("UTF-8")));
        rxThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    eventListener.onConnectionReady(TCPConection.this);
                    while (!rxThread.isInterrupted()) {
                        eventListener.onReceiveString(TCPConection.this, in.readLine());
                    }
                } catch (IOException e) {
                    eventListener.onExeption(TCPConection.this,e);
                }finally {
                    eventListener.onDisconect(TCPConection.this);
                }
            }


        });
        rxThread.start();
    }
    public synchronized void  sendString(String value){
        try {
            out.write(value+ "\r\n");
            out.flush();
        } catch (IOException e) {
            eventListener.onExeption(TCPConection.this,e);
            disconnect();
        }
    }
    public synchronized void  disconnect(){
        rxThread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
         eventListener.onExeption(TCPConection.this,e);
        }
    }

    @Override
    public String toString() {
        return "TCPConnection:" + socket.getInetAddress() + " : " + socket.getPort();
    }
}
