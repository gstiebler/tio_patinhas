/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorimagens;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Guilherme
 */
public class CServidorImagens {
    private ServerSocket server;
    private int portServer=2000;
    private String hostName;
    private String ip;

    public CServidorImagens() throws IOException {
        super();
        // Cria um Servidor Socket
        server = new ServerSocket(portServer);
        hostName = new String(InetAddress.getLocalHost().getHostName());
        ip = new String(InetAddress.getLocalHost().getHostAddress());

        // Loop infinito para receber conexões
        while (true) {
            Socket sock_client = null;
            sock_client = server.accept();

        // Operacoes de comunicacao
        // .....
        }

    }
}
