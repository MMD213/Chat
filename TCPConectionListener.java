public interface TCPConectionListener {
    void onConnectionReady(TCPConection tcpConection);
    void onReceiveString(TCPConection tcpConection, String value);
    void onDisconect(TCPConection tcpConection);
    void onExeption(TCPConection tcpConection,Exception e);

}
