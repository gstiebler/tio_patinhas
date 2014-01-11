
import javax.microedition.lcdui.Image;

/**
 *
 * @author Administrator
 */
public class UCBitmap {

}

/**
 * 
 */
class CTonsCinza {

    int[][] TonsCinza;
    public int Larg,  Alt;
    int pixel, azul, verde, vermelho, raw[];
    
    /**
     * Constrói um CTonsCinza a partir de uma Image
     */
    CTonsCinza(Image img) {
       
        Larg = img.getWidth();
        Alt = img.getHeight();
        
        TonsCinza = new int[Alt][Larg];
        raw = new int[Larg*Alt];

        img.getRGB(raw, 0, Larg, 0, 0, Larg, Alt);
        
        for (int y = 0; y < Alt; y++) {
            for (int x = 0; x < Larg; x++) {
                pixel=raw[x+y*Larg];
                
                azul=pixel & 0x0000FF;
                verde=(pixel & 0xFF00) >> 8;
                vermelho=(pixel & 0xFF0000) >> 16;
                pixel=azul*11+verde*59+vermelho*30;
                pixel*=0.01;
                TonsCinza[y][x] = pixel;
            }
        }
    }

    /**
     * Retorna uma Image gerada a partir do CTonsCinza
     */
    Image SaveImage() {
        
        int[] rawRGB = new int[Larg*Alt];
      
        int ponteiro = 0;
        
        for (int y = 0; y < Alt; y++) {
            for (int x = 0; x < Larg; x++) {
                rawRGB[ponteiro] = TonsCinza[y][x];
                rawRGB[ponteiro] = TonsCinza[y][x];
                rawRGB[ponteiro++] = TonsCinza[y][x];
            }
        }
        
        Image img = Image.createRGBImage(rawRGB, Larg, Alt, false);
       
        return img;
    }
};

class Cor {

    int Azul, Verde, Vermelho;
    
    Cor(int pixel) {
        Azul=pixel & 0xFF;
        Verde=(pixel & 0xFF00) >> 8;
        Vermelho=(pixel & 0xFF0000) >> 16;
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
    int raw[];

    CBitmap(Image img) {
        
        Larg = img.getWidth();
        Alt = img.getHeight();

        raw = new int[Larg*Alt];

        img.getRGB(raw, 0, Larg, 0, 0, Larg, Alt);
        
        PMCor = new Cor[Alt][Larg];

        for (int y = 0; y < Alt; y++) {
            for (int x = 0; x < Larg; x++) {
                PMCor[y][x] = new Cor(raw[x+y*Larg]);
            }
        }
    }

    Image SaveImage() {
        
        int[] rawRGB = new int[Larg*Alt];
      
        int ponteiro = 0;
        
        for (int y = 0; y < Alt; y++) {
            for (int x = 0; x < Larg; x++) {
                rawRGB[ponteiro] = PMCor[y][x].Azul;
                rawRGB[ponteiro] += PMCor[y][x].Verde << 8;
                rawRGB[ponteiro] += PMCor[y][x].Vermelho << 16;
                rawRGB[ponteiro++] += 0xFF000000;
            }
        }
        
        Image img = Image.createRGBImage(rawRGB, Larg, Alt, false);
       
        return img;
        
    }
};