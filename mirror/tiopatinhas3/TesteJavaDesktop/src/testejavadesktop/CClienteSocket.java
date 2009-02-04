/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testejavadesktop;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author Guilherme
 */
public class CClienteSocket {
    private Socket conCliente;
    private int porta=2000;
    private String hostname="localhost";
    private PrintStream outStream;
    private DataInputStream inStream;

    public CClienteSocket() throws UnknownHostException, IOException {
        conCliente = new Socket(hostname, porta);

        // Pega Os Streams de Leitura e escrita
        // Essa parte deve ter algo parecido no servidor
        // na hora de receber/enviar os dados
        outStream = new PrintStream(conCliente.getOutputStream());
        inStream = new DataInputStream(conCliente.getInputStream());

    }
}
