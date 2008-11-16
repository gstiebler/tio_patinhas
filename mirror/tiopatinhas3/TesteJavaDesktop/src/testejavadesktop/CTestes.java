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
class CImagensCorEPB {

    CTonsCinza TonsCinza;
    CBitmap Bitmap;

    public CImagensCorEPB(CTonsCinza tonsCinza, CBitmap bitmap) {
        TonsCinza = tonsCinza;
        Bitmap = bitmap;
    }
}

class CImagensNota extends Vector {

    public int nota;

    public CImagensNota(int Nota, String Pasta) {
        nota = Nota;
        File dir = new File(Pasta);
        String[] children = dir.list();
        if (children == null) {
            // Either dir does not exist or is not a directory
            } else {
            for (int i = 0; i < children.length; i++) {
                // Get filename of file or directory
                String sarq = Pasta + "\\" + children[i];
                ImageProcessor imgProc = new ImageProcessor();
                Image image2 = imgProc.loadbitmap("", sarq);
                Image image = ImageProcessor.toBufferedImage(image2);
                addElement(image);
            }
        }
    }

    public Image Imagens(int indice) {
        return (Image) elementAt(indice);
    }
}

class CArquivosEmMemoria {

    public CImagensNota[] Imagens;

    public CArquivosEmMemoria(String PastaBase) {
        int[] notas = {1, 2, 5, 10, 20, 50, 100};
        Imagens = new CImagensNota[7];
        for (int n = 0; n < notas.length; n++) {
            Imagens[n] = new CImagensNota(notas[n], PastaBase + notas[n]);
        }
    }

    public int NumNotas() {
        return 7;
    }

    public CImagensNota ImagensNota(int indice) {
        return Imagens[indice];
    }
}

public class CTestes {

    public static String ExecutaTestes() {
        long tempo1=System.currentTimeMillis();
        String retorno = "";
        String PastaBase = "P:\\TioPatinhas\\dinheiro\\testes\\";
        CArquivosEmMemoria ArquivosEmMemoria = new CArquivosEmMemoria(PastaBase);
        long tempo2=System.currentTimeMillis();
        int TotalNotas, Acertos;
        INIFile objINI=new INIFile("p:\\TioPatinhas\\ParamsTP.ini");
        for (int n = 0; n < ArquivosEmMemoria.NumNotas(); n++) {
            CImagensNota ImagensNota = ArquivosEmMemoria.ImagensNota(n);
            TotalNotas = 0;
            Acertos = 0;
            for (int i = 0; i < ImagensNota.size(); i++) {
                TParamsRC ParamsRC = new TParamsRC(objINI,
                            new CTonsCinza(ImagensNota.Imagens(i)),
                            new CBitmap(ImagensNota.Imagens(i)));
                //ParamsRC.setTonsCinza(new CTonsCinza(ImagensNota.Imagens(i)));
                //ParamsRC.setBitmap(new CBitmap(ImagensNota.Imagens(i)));
                UTioPatinhas.ReconheceCedula(ParamsRC);
                TotalNotas++;

                if (ImagensNota.nota == ParamsRC.ParamsAI.ValorCedula) {
                    Acertos++;
                }
            }

            retorno += ImagensNota.nota + ": " + Acertos * 100.0 / TotalNotas + "\n";
        }
        retorno += "Carga arquivos: "+new Double((tempo2-tempo1)/1000.0).toString()+"\n";
        retorno += "Reconhecimentos: "+new Double((System.currentTimeMillis()-tempo2)/1000.0).toString()+"\n";
        return retorno;
    }
}
