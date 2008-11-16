/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package testejavadesktop;

import java.awt.Image;

/**
 *
 * @author Administrator
 */
public class CDesktopFuncs {

    public static int Reconhece(String arquivo) {
        ImageProcessor imgProc = new ImageProcessor();
        Image image2 = imgProc.loadbitmap("", arquivo);
        Image image = ImageProcessor.toBufferedImage(image2);

        TParamsRC ParamsRC = new TParamsRC(new INIFile("p:\\TioPatinhas\\ParamsTP.ini"),
                new CTonsCinza(image), new CBitmap(image));
        UTioPatinhas.ReconheceCedula(ParamsRC);
        System.out.println("Valor cédula: " +
                String.valueOf(ParamsRC.ParamsAI.ValorCedula));
        return ParamsRC.ParamsAI.ValorCedula;
    }
}
