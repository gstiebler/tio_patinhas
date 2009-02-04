/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testejavadesktop;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Guilherme
 */
public class CClienteSocket {

    private Socket conCliente;
    private int porta = 2000;
    private String hostname = "localhost";
    private PrintStream outStream;
    private DataInputStream inStream;
    private ObjectInputStream ois;

    public CClienteSocket() {

        super();
    }

    public CArquivosTeste RecebeArquivosTeste()
    {
        try {
            conCliente = new Socket(hostname, porta);
            // Pega Os Streams de Leitura e escrita
            // Essa parte deve ter algo parecido no servidor
            // na hora de receber/enviar os dados
            ois = new ObjectInputStream(conCliente.getInputStream());
            return (CArquivosTeste) ois.readObject();
        } catch (UnknownHostException ex) {
            Logger.getLogger(CClienteSocket.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CClienteSocket.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CClienteSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
