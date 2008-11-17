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
class CNota {

    public String arquivo;
    public Image imagem;

    public CNota(String Arquivo, Image Imagem) {
        arquivo=Arquivo;
        imagem=Imagem;
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
                addElement(new CNota(children[i], image));
            }
        }
    }

    public CNota Imagens(int indice) {
        return (CNota) elementAt(indice);
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
        CDesktopFuncs.WriteOutput("teste");
        String retorno = "";
        String PastaBase = "P:\\TioPatinhas\\dinheiro\\testes\\";
        CArquivosEmMemoria ArquivosEmMemoria = new CArquivosEmMemoria(PastaBase);
        long tempo2=System.currentTimeMillis();
        int TotalNotas, Acertos;
        INIFile objINI=new INIFile("p:\\TioPatinhas\\ParamsTP.ini");
        CNota NotaTemp;
        for (int n = 0; n < ArquivosEmMemoria.NumNotas(); n++) {
            CImagensNota ImagensNota = ArquivosEmMemoria.ImagensNota(n);
            TotalNotas = 0;
            Acertos = 0;
            for (int i = 0; i < ImagensNota.size(); i++) {
                NotaTemp=ImagensNota.Imagens(i);
                TParamsRC ParamsRC = new TParamsRC(objINI,
                            new CTonsCinza(NotaTemp.imagem),
                            new CBitmap(NotaTemp.imagem));
                UTioPatinhas.ReconheceCedula(ParamsRC);
                TotalNotas++;

                if (ImagensNota.nota == ParamsRC.ParamsAI.ValorCedula) {
                    Acertos++;
                }
                else
                {
                    BMPFile bmpFile=new BMPFile();
                    bmpFile.saveBitmap(
                              "P:\\TioPatinhas\\dinheiro\\testes\\erradas\\"+
                              System.currentTimeMillis()+NotaTemp.arquivo, 
                              ParamsRC.ParamsMLT.BImgDest.SaveImage(), 320, 240);
                }
            }

            retorno += ImagensNota.nota + ": " + Acertos * 100.0 / TotalNotas + "\n";
        }
        retorno += "Carga arquivos: "+new Double((tempo2-tempo1)/1000.0).toString()+"\n";
        retorno += "Reconhecimentos: "+new Double((System.currentTimeMillis()-tempo2)/1000.0).toString()+"\n";
        return retorno;
    }
}
