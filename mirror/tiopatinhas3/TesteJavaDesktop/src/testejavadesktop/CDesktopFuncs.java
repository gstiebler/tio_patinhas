/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testejavadesktop;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class CDesktopFuncs {

    public static int Reconhece(String arquivo) {
        ImageProcessor imgProc = new ImageProcessor();
        Image image2 = imgProc.loadbitmap(arquivo);
        Image image = ImageProcessor.toBufferedImage(image2);
        TParamsDir ParamsDir=new TParamsDir();
        
        TParamsRC ParamsRC = new TParamsRC(new TParamsIni("ParamsTP.ini"),
                new CTonsCinza(image), new CBitmap(image));
        UTioPatinhas.ReconheceCedula(ParamsRC);
        System.out.println("Valor cédula: " +
                String.valueOf(ParamsRC.ParamsAI.ValorCedula));
        return ParamsRC.ParamsAI.ValorCedula;
    }

    public static void copyFile(String file_in, String file_out)
            throws IOException {
        File in = new File(file_in);
        File out = new File(file_out);
        FileChannel inChannel = new FileInputStream(in).getChannel();
        FileChannel outChannel = new FileOutputStream(out).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(),
                    outChannel);
        } catch (IOException e) {
            throw e;
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }

    public static String FormataPorcentagem(double valor)
    {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return df.format(valor*100.0);
    }

    public static void WriteOutput(String str) {
        System.out.println(str);
    }

    public static void InicializaWriteOutput(String NomeArq) {
    }
}

class COutputDebug
{
    static private PrintWriter out;
    static private String StrLog;
    static private boolean GravaEmArquivo=false;
    public static void InicializaArquivo()
    {
        GravaEmArquivo=true;
        StrLog=new String();
    }

    public static void FechaArquivo(String caminho_arq){
        FileWriter outFile;
        try {
            outFile = new FileWriter(caminho_arq);
            out = new PrintWriter(outFile);
            out.println(StrLog);
            out.close();
        } catch (IOException ex) {
            System.out.println("Erro ao abrir o arquivo "+caminho_arq);
        }
        GravaEmArquivo=false;
    }

    public static void WriteOutput(String str) {
        if (GravaEmArquivo)
            StrLog+=str+"\n";
        else
            System.out.println(str);
    }
}

class DateUtils {

    public static final String DATE_FORMAT_NOW = "dd/MM/yyyy HH:mm:ss";

    public static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());
    }
    
        public static String nowCurto() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(cal.getTime());
    }
}

class TParamsDir{
    INIFile objINI;
    private String DirBase;
    private String DirImagens;
    public TParamsDir()
    {
        objINI = new INIFile("D:\\Projetos\\TioPatinhas\\ParamsDir.ini");
        DirBase=objINI.getStringProperty("Geral", "DiretorioBase");
        DirImagens=objINI.getStringProperty("Geral", "DiretorioImagens");
    }
    
    public  String getDir(String Property)
    {
        return objINI.getStringProperty("Geral", Property);
    }
    
    public String getDirBase()
    {
        return DirBase;
    }
    
    public String getDirImagens()
    {
        return DirImagens;
    }
}