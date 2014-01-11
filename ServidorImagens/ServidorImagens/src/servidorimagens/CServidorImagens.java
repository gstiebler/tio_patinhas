/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorimagens;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Guilherme
 */
public class CServidorImagens extends Thread {

    private ServerSocket server;
    private int portServer = 2000;
    private ObjectOutputStream oos;
    CArquivosTeste ArquivosTeste;

    public CServidorImagens(CArquivosTeste arquivosTeste) throws IOException {
        super();
        ArquivosTeste=arquivosTeste;
    }

    @Override
    public void run() {
        try {
            // Cria um Servidor Socket
            server = new ServerSocket(portServer);

            // Loop infinito para receber conexões
            while (true) {
                Socket sock_client = null;
                sock_client = server.accept();
                oos = new ObjectOutputStream(sock_client.getOutputStream());
                oos.writeObject(ArquivosTeste);
            }
        } catch (IOException ex) {
            Logger.getLogger(CServidorImagens.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
