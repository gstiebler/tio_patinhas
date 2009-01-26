/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testejavadesktop;

import java.awt.Image;
import java.io.File;
import java.util.Vector;

/**
 *
 * @author Administrator
 */
public class ArquivosTeste {
}

//Armazena a imagem e a string com o nome do arquivo
class CNota {

    public String arquivo;
    public Image imagem;

    public CNota(String Arquivo, Image Imagem) {
        arquivo = Arquivo;
        imagem = Imagem;
    }
    
    public static int NotaPorIndice(int indice)
    {
        int[] notas = {1, 2, 5, 10, 20, 50, 100};
        return notas[indice];
    }
}

//interface para a classe que disponibiliza todas as imagens de um determinado
//valor de nota
interface CBaseCImagensNota {
    
    public CNota Imagens(int indice);
    public int NumElementos();
}


abstract class CImagensNota extends Vector implements CBaseCImagensNota{
    public int nota;
}

class CImagensNotaEmMemoria extends CImagensNota {

    public CImagensNotaEmMemoria(int Nota, String Pasta) {
        nota = Nota;
        File dir = new File(Pasta);
        String[] children = dir.list();
        if (children == null) {
            // Either dir does not exist or is not a directory
            } else {
            for (int i = 0; i < children.length; i++) {
                // Get filename of file or directory
                String sarq = Pasta + "/" + children[i];
                ImageProcessor imgProc = new ImageProcessor();
                Image image2 = imgProc.loadbitmap("", sarq);
                Image image = ImageProcessor.toBufferedImage(image2);
                addElement(new CNota(children[i], image));
            }
        }
    }

    @Override
    public CNota Imagens(int indice) {
        return (CNota) elementAt(indice);
    }

    public int NumElementos() {
        return size();
    }
}

class CImagensNotaEmArquivo extends CImagensNota {

    String[] children;
    String pasta;

    public CImagensNotaEmArquivo(int Nota, String Pasta) {
        nota = Nota;
        pasta = Pasta;
        File dir = new File(Pasta);
        children = dir.list();
    }

    @Override
    public CNota Imagens(int indice) {
        if (children != null) {
            String sarq = pasta + "/" + children[indice];
            ImageProcessor imgProc = new ImageProcessor();
            Image image2 = imgProc.loadbitmap("", sarq);
            Image image = ImageProcessor.toBufferedImage(image2);
            return new CNota(children[indice], image);
        }
        return null;
    }

    public int NumElementos() {
        return children.length;
    }
}
class CArquivosTeste {

    public CImagensNota[] Imagens;

    public CArquivosTeste(String PastaBase, boolean EmMemoria) {
        int[] notas = {1, 2, 5, 10, 20, 50, 100};
        Imagens = new CImagensNota[7];
        for (int n = 0; n < notas.length; n++) {
            if (EmMemoria)
                Imagens[n] = new CImagensNotaEmMemoria(notas[n], PastaBase + notas[n]);
            else
                Imagens[n] = new CImagensNotaEmArquivo(notas[n], PastaBase + notas[n]);
        }
    }

    public int NumNotas() {
        return 7;
    }

    public CImagensNota ImagensNota(int indice) {
        return Imagens[indice];
    }
}