/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package testejavadesktop;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 *
 * @author Administrator
 */
public class CDesktopFuncs {

    public static int Reconhece(String arquivo) {
        ImageProcessor imgProc = new ImageProcessor();
        Image image2 = imgProc.loadbitmap("", arquivo);
        Image image = ImageProcessor.toBufferedImage(image2);

        TParamsRC ParamsRC = new TParamsRC(new TParamsIni("p:\\TioPatinhas\\ParamsTP.ini"),
                new CTonsCinza(image), new CBitmap(image));
        UTioPatinhas.ReconheceCedula(ParamsRC);
        System.out.println("Valor cédula: " +
                String.valueOf(ParamsRC.ParamsAI.ValorCedula));
        return ParamsRC.ParamsAI.ValorCedula;
    }
    
    public static void copyFile(String file_in, String file_out) 
        throws IOException 
    {
        File in=new File(file_in);
        File out=new File(file_out);
        FileChannel inChannel = new
            FileInputStream(in).getChannel();
        FileChannel outChannel = new
            FileOutputStream(out).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(),
                    outChannel);
        } 
        catch (IOException e) {
            throw e;
        }
        finally {
            if (inChannel != null) inChannel.close();
            if (outChannel != null) outChannel.close();
        }
    }
 
    public static void WriteOutput(String str)
    {
        //System.out.println(str);
    }
    
    public static void InicializaWriteOutput(String NomeArq)
    {
        
    }
}