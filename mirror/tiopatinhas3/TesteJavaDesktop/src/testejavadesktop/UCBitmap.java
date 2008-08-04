/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package testejavadesktop;

import java.awt.*;
import java.awt.image.*;

/**
 *
 * @author Administrator
 */
public class UCBitmap {

}

class CTonsCinza {

    int[][] TonsCinza;
    public int Larg,  Alt;
    int pixel, azul, verde, vermelho;
    CTonsCinza(Image img) {
        Larg = img.getWidth(null);
        Alt = img.getHeight(null);
        BufferedImage bi = ImageProcessor.toBufferedImage(img);

        TonsCinza = new int[Alt][Larg];

        for (int y = 0; y < Alt; y++) {
            for (int x = 0; x < Larg; x++) {
                pixel=bi.getRGB(x, y);
                azul=pixel & 0x0000FF;
                verde=(pixel & 0xFF00) >> 8;
                vermelho=(pixel & 0xFF0000) >> 16;
                pixel=azul*11+verde*59+vermelho*30;
                pixel*=0.01;
                TonsCinza[y][x] = pixel;
            }
        }
    }

    Image SaveImage() {
        int[] raw = new int[Larg * Alt];
        int ponteiro = 0;
        for (int y = 0; y < Alt; y++) {
            for (int x = 0; x < Larg; x++) {
                raw[ponteiro++] = TonsCinza[y][x];
                raw[ponteiro++] = TonsCinza[y][x];
                raw[ponteiro++] = TonsCinza[y][x];
            }
        }
        BufferedImage img = new BufferedImage(Larg, Alt, BufferedImage.TYPE_3BYTE_BGR);
        img.setRGB(0, 0, Larg, Alt, raw, 0, Larg);
        return img;
    }
};

class Cor {

    int Azul, Verde, Vermelho;
    
    Cor(int pixel) {
        Azul=pixel & 0xFF;
        Verde=(pixel & 0xFF00) >> 8;
        Vermelho=(pixel & 0xFF00000) >> 16;
    }

    void SetAzul() {
        Azul = 0xFF;
        Verde = 0;
        Vermelho = 0;
    }

    void SetVermelho() {
        Azul = 0;
        Verde = 0;
        Vermelho = 0xFF;
    }

    void SetVerde() {
        Azul = 0;
        Verde = 0xFF;
        Vermelho = 0;
    }

    void SetAmarelo() {
        Azul = 0;
        Verde = 0xFF;
        Vermelho = 0xFF;
    }

    void SetMagenta() {
        Azul = 0xFF;
        Verde = 0;
        Vermelho = 0xFF;
    }

    void SetCyan() {
        Azul = 0xFF;
        Verde = 0xFF;
        Vermelho = 0;
    }

    void SetRGB(byte R, byte G, byte B) {
        Azul = B;
        Verde = G;
        Vermelho = R;
    }
};
//---------------------------------------------------------------------------

class CBitmap {

    Cor [][] PMCor;
    public int Larg,  Alt;

    CBitmap(Image img) {
        Larg = img.getWidth(null);
        Alt = img.getHeight(null);
        BufferedImage bi = ImageProcessor.toBufferedImage(img);

        PMCor = new Cor[Alt][Larg];

        for (int y = 0; y < Alt; y++) {
            for (int x = 0; x < Larg; x++) {
                PMCor[y][x] = new Cor(bi.getRGB(x, y));
            }
        }
    }

    Image SaveImage() {
        int[] raw = new int[Larg * Alt];
        int ponteiro = 0;
        for (int y = 0; y < Alt; y++) {
            for (int x = 0; x < Larg; x++) {
                raw[ponteiro] = PMCor[y][x].Azul;
                raw[ponteiro] += PMCor[y][x].Verde << 8;
                raw[ponteiro++] += PMCor[y][x].Vermelho << 16;
                
            }
        }
        BufferedImage img = new BufferedImage(Larg, Alt, BufferedImage.TYPE_3BYTE_BGR);
        img.setRGB(0, 0, Larg, Alt, raw, 0, Larg);
        return img;
    }
};