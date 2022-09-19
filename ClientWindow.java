import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ClientWindow extends JFrame implements ActionListener,TCPConectionListener {
    private static final String IP_ADDR = "26.25.152.58";
    private static final int PORT = 80;
    private static final int WIDTH =  600;
    private static final int HEIGHT = 400;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientWindow();
            }
        });
    }
    private final JTextArea log = new JTextArea();
    private final JTextField fieldNickname = new JTextField("MMD");
    private final JTextField fieldInput = new JTextField();
    private TCPConection conection;
    private ClientWindow(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH,HEIGHT);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);

        log.setEditable(false);
        log.setLineWrap(true);
        add(log, BorderLayout.CENTER);

        fieldInput.addActionListener(this);
        add(fieldInput,BorderLayout.SOUTH);
        add(fieldNickname,BorderLayout.NORTH);

        setVisible(true);
        try {
            conection = new TCPConection(this,IP_ADDR,PORT );
        } catch (IOException e) {
            printMsg("Connection exception: " + e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = fieldInput.getText();
        if(msg.equals("")) return;
        fieldInput.setText(null);
        conection.sendString(fieldNickname.getText() + ": " + msg);
    }

    @Override
    public void onConnectionReady(TCPConection tcpConection) {
        printMsg("Connaction ready...");
    }

    @Override
    public void onReceiveString(TCPConection tcpConection, String value) {
        printMsg(value);
    }

    @Override
    public void onDisconect(TCPConection tcpConection) {
        printMsg("Connection close");
    }

    @Override
    public void onExeption(TCPConection tcpConection, Exception e) {
        printMsg("Connection exception: " + e);
    }
    private synchronized void printMsg(String msg){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(msg + "\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }
}
