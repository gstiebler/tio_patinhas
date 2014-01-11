/**
 * @author Igor
 */

import java.util.*;
import javax.microedition.lcdui.*;

class CTonsCinza {
    private int LargTotal, AltTotal,
                LargImg, AltImg;

  byte [][]TonsCinza;
  public int Larg, Alt;

  CTonsCinza(Image img){
      
      int []raw = new int[img.getHeight()*img.getHeight()];
      img.getRGB(raw, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());
      
      // implementar
      Larg = img.getWidth();
      Alt  = img.getHeight();
      TonsCinza = new byte[Larg][Alt];
      
      for (int i=0; i<Alt; i++){
        for (int j=0; j<Alt; j++){
            TonsCinza[i][j] = (byte)(raw[i*Larg+j]& 0x00000FF);
        }
      }
  }
  
  Image SaveImage() {
      byte []raw = new byte[Larg*Alt]; 
      int ponteiro=0;
      for (int i=0; i<Alt; i++){
        for (int j=0; j<Alt; j++){
            raw[ponteiro++]=TonsCinza[i][j];
            raw[ponteiro++]=TonsCinza[i][j];
            raw[ponteiro++]=TonsCinza[i][j];
        }
      }
      Image img = Image.createImage(raw, 0, raw.length);
      return img;
  }
};

class TPonto {
  int x, y;
};

class TParamsAI {
  
  CTonsCinza TCImgSrc;
  
  TPonto RefTarja;
  //Distância da parte de cima da tarja para uma faixa de referência de um pixel de altura,
  //que é usada para ver a média da luminosidade logo acima da tarja
  int DistFaixaRef;
  //largura da faixa de referência
  int LargFaixaRef;
  //Utilizada para determinar a luminosidade máxima de um pixel para que ele seja considerado
  // como um pixel do identificador. Esta luminosidade máxima é:
  // Limiar=Media-ParamsAI.DifMinMediaFaixaRef, onde Media é a média de luminosidade da faixa
  // de referência.
  int DifMinMediaFaixaRef;
  //Largura máxima em pixels do identificador. Utilizada para definir a região de busca de
  // pixels de identificador
  int LargIdentificador;
  //Análogo à LargIdentificador
  int AltIdentificador;
  //deslocamento para à esquerda em pixels do início da região de busca dos pixels de identificador.
  // O início da região de busca fica: xIni=ParamsAI.RefTarja.x-ParamsAI.XIniParaRefTarja
  int XIniParaRefTarja;
  // yIni=ParamsAI.RefTarja.y-ParamsAI.YIniParaRefTarja
  int YIniParaRefTarja;
  //Define o maior gap em número de pixels que pode haver para que não se considere que o
  //identificador está quebrado. Se houve um gap maior que o definido, considera-se que
  //o identificador acabou. Este gap é de altura de pixels, considerando que o processamento
  //é de cima para baixo
  int MaiorDistSemPixelsIdentificador;
  //Usado para determinar o número mínimo de pixels já processados com luminosidade abaixo do limiar
  //para se considerar que já foram processados pixels de identificador
  int NumMinPixelsIdentificador;  
  //Limiar que define se um identificador tem 1 ou 2 "andares"
  int LimiarAlturaIdentificador;
  //Limiar que define se um identificador é inclinado ou não
  int LimiarInclinacaoidentificador;
  //Limiar que define a proporção entre a largura mediana da parte de cima e de baixo
  //do identificador
  float LimiarLargLinhasIdentificador;

  //Retorno
  int Alt;
  float Inclinacao;
  int MaiorLargLinha;
  float RelacaoMedianasLargurasEncEmb;

  int ValorCedula;

    public TParamsAI(Image img) {
       TCImgSrc = new CTonsCinza(img);
    }
};

class TBordasColunas {
 
    Vector [] Bordas;
    int NumColunas;
    
    TBordasColunas(int numColunas) {

        NumColunas = numColunas;
        Bordas = new Vector[NumColunas];

        for (int n = 0; n < NumColunas; n++) {
            Bordas[n] = new Vector();
        }
    }
};

class TConjuntoMeioBordas {
    
    TConjuntoMeioBordas(int numColunas){
        NumColunas = numColunas;
        VectorMeioBordas = new Vector[NumColunas];
        for (int n=0; n<NumColunas; n++)
            VectorMeioBordas[n]=new Vector();
        }
  
    Vector VectorMeioBordas[];
    int NumColunas;
};

class TVectorInt extends Vector{
    TVectorInt(){
        super();
    }
    
    Integer elementoEm(int indice){
        return (Integer)(elementAt(indice));
    }
}

class TTarja {
    
  int X;
  int UltYMeio;
  int PriYEnc;
  
  boolean Ativa(int x){
      return ((x-X)==VetorAlturas.size());
  }
  
  Vector VetorAlturas;
};

class TParamsABT {                 
  
    //Altura mínima que uma faixa escura pode ter para ser considerada tarja
    int AltMinTarja;
    //Altura máxima que uma faixa escura pode ter para ser considerada tarja
    int AltMaxTarja;
    //distância horizontal máxima entre faixas escuras para serem consideradas tarja
    int DistMaxTarjas;
    //Largura mínima de uma faixa escura para ser considerada tarja
    int LargMinTarja;
    //Largura mínima de uma faixa escura para ser considerada tarja
    int LargMaxTarja;
    //Define o desvio médio máximo para que uma faixa seja considerada a tarja.
    //Este desvio é a média da diferença de altura de cada coluna da faixa para a média das alturas
    double DesvioMax;

    boolean AchouTarja;
    TPonto RefTarja;
    //Vetor com locais de bordas que são analizadas para saber se são bordas da tarja
    TBordasColunas BordasColunas;
    TConjuntoMeioBordas ConjuntoMeioBordas;  
    Vector VectorTarja;
    
};

class TParamsMLT{
    
    CTonsCinza TCImgSrc;

    //Menor luminosidade que um pixel acima da tarja pode ter
    int ClaroMin;
    //Maior luminosidade que um pixel da tarja pode assumir
    byte EscuroMax;
    //Fator que define a região de busca da tarja. Multiplica-se a largura da imagem por este fator
    float PropYIni;
    float PropXFim;
    //Altura mínima em pixels da coluna de pixels claros que devem estar na região encima de uma tarja
    int AltMinClaro;
    //Altura mínima em pixels da coluna de pixels claros que devem estar na região dentro da tarja
    int AltMinEscuro;
    //Número mínimo de pixels claros acima do pixel corrente
    //para se considerar que está no estado CLARO
    int NumMinClaro;
    //Número mínimo de pixels escuros acima do pixel corrente
    //para se considerar que está no estado ESCURO
    int NumMinEscuro;
    //retorno
    
    public TBordasColunas BordasColunas;

    public TParamsMLT(Image img) {
        TCImgSrc = new CTonsCinza(img);
    }
    
    public void setBordasColunas (int size){
        BordasColunas = new TBordasColunas(size);    
    }
};


class TParamsRC {
  
    TParamsMLT ParamsMLT;
    TParamsABT ParamsABT;
    TParamsAI ParamsAI;

    public TParamsRC(Image img) {
        ParamsMLT = new TParamsMLT(img);
        ParamsABT = new TParamsABT();
        ParamsAI = new TParamsAI(img);
    }
  
};

class TBorda {
  int Y;
  int TipoBorda;
};


class TMeioBordas {

    int Y1, Y2, yMeio, Altura;

    void Inicializa(int y1, int y2) {
        Y1 = y1;
        Y2 = y2;
        yMeio = (Y1 + Y2) / 2;
        Altura = Y2 - Y1;
    }
};


class ImageProcessor {
    
    static final int BORDA_ESCURO_CLARO = 0,
                     BORDA_CLARO_ESCURO = 1;

    static public TParamsRC ParamsRC;

    static public void initializeImage(Image img) {
        ParamsRC = new TParamsRC(img);
        CarregaParamsReconheceCedula();
    }
    
    static public void CarregaParamsReconheceCedula(){
        
        ParamsRC.ParamsMLT.ClaroMin=130;        
        ParamsRC.ParamsMLT.EscuroMax=95;
        ParamsRC.ParamsMLT.PropYIni=300; 
        ParamsRC.ParamsMLT.PropXFim=600;
        ParamsRC.ParamsMLT.AltMinClaro=4;     
        ParamsRC.ParamsMLT.AltMinEscuro=4;    
        ParamsRC.ParamsMLT.NumMinClaro=3;   
        ParamsRC.ParamsMLT.NumMinEscuro=3;
        
        ParamsRC.ParamsABT.AltMinTarja=15;       
        ParamsRC.ParamsABT.AltMaxTarja=25;     
        ParamsRC.ParamsABT.DistMaxTarjas=1;    
        ParamsRC.ParamsABT.LargMinTarja=15;     
        ParamsRC.ParamsABT.LargMaxTarja=70;      
        ParamsRC.ParamsABT.DesvioMax=30;
        
        ParamsRC.ParamsAI.DistFaixaRef=2;
        ParamsRC.ParamsAI.LargFaixaRef=4;
        ParamsRC.ParamsAI.DifMinMediaFaixaRef=25;
        ParamsRC.ParamsAI.LargIdentificador=20;            
        ParamsRC.ParamsAI.AltIdentificador=32;
        ParamsRC.ParamsAI.XIniParaRefTarja=2;
        ParamsRC.ParamsAI.YIniParaRefTarja=3;
        ParamsRC.ParamsAI.LimiarAlturaIdentificador=13;
        ParamsRC.ParamsAI.LimiarInclinacaoidentificador=1;
        ParamsRC.ParamsAI.MaiorDistSemPixelsIdentificador=3;
        ParamsRC.ParamsAI.NumMinPixelsIdentificador=10;
        ParamsRC.ParamsAI.LimiarLargLinhasIdentificador=2000;
        
    }
    
    
    //---------------------------------------------------------------------------
    //Localiza e mostra a tarja.
    //Executa o processamento da esquerda para direita, de cima para baixo.
    //Para cada coluna, verifica nos últimos AltMinClaro pixels quantos pixels são considerados
    // como pixels claros, e quantos são considerados escuros. Os pixels são comparados com ClaroMin e
    //EscuroMax. Se o número de pixels claros for maior que NumMinClaro, a iteração é colocada no
    //estado CLARO. Analogamente, se o número de pixels escuros for maior que NumMinEscuros, a iteração
    //é colocada no estado ESCURO. Quando há transição do estado CLARO para ESCURO, este ponto é
    //considerado borda superior da tarja. Quando há transição de ESCURO para CLARO, este ponto
    //é considerado a parte de baixo da tarja.
    static void MostraLimiteTarja(TParamsMLT ParamsMLT){
      
        int x, y;
        int yBorda;
        final int NADA = 0,
                  CLARO  = 1,
                  ESCURO = 2;
           
        int estado;
        byte [][]ImgSrc = ParamsMLT.TCImgSrc.TonsCinza;
        
        int YIni=(int)(ParamsMLT.TCImgSrc.Alt*ParamsMLT.PropYIni);
        int XFim=(int)(ParamsMLT.TCImgSrc.Larg*ParamsMLT.PropXFim);
        int ContaClaro, ContaEscuro;
        
        TBorda BordaTemp = new TBorda();
        
        ParamsMLT.BordasColunas=new TBordasColunas(XFim);
        for (x=0; x<XFim; x++) {
            
            estado = NADA;
            
            ContaClaro = ContaEscuro = 0;
            
            //Inicialização do processamento da coluna. É executado por poucos pixels
            for (y = YIni; y < YIni + ParamsMLT.AltMinClaro; y++) {
                if (ImgSrc[y][x] >= ParamsMLT.ClaroMin) {
                    ContaClaro++;
                }
                if (ImgSrc[y][x] <= ParamsMLT.EscuroMax) {
                    ContaEscuro++;
                }
            }

            for (y = YIni + ParamsMLT.AltMinClaro; y < ParamsMLT.TCImgSrc.Alt; y++) {
                //Atualiza a quantidade de pixels claros e de escuros que estão nos últimos AltMinClaro
                //pixels da coluna
                if ((ImgSrc[y - ParamsMLT.AltMinClaro][x] >= ParamsMLT.ClaroMin) && ContaClaro >= 1) {
                    ContaClaro--;
                }
                if (ImgSrc[y][x] >= ParamsMLT.ClaroMin) {
                    ContaClaro++;
                }

                if ((ImgSrc[y - ParamsMLT.AltMinClaro][x] <= ParamsMLT.EscuroMax) && ContaEscuro >= 1) {
                    ContaEscuro--;
                }
                if (ImgSrc[y][x] <= ParamsMLT.EscuroMax) {
                    ContaEscuro++;
                }

                switch (estado) {
                    case NADA:
                        //se existe uma quantidade suficiente de pixels claros nos últimos pixels da coluna
                        // então entra no estado CLARO
                        if (ContaClaro >= ParamsMLT.NumMinClaro) {
                            estado = CLARO;
                            yBorda = y - ParamsMLT.NumMinEscuro;
                            
                            BordaTemp.Y = yBorda;
                            BordaTemp.TipoBorda = BORDA_ESCURO_CLARO;
                            ParamsMLT.BordasColunas.Bordas[x].addElement(BordaTemp);
                        }
                        break;
                    case CLARO:
                        if (ContaEscuro >= ParamsMLT.NumMinEscuro) {
                            estado = ESCURO;
                            yBorda = y - ParamsMLT.NumMinClaro;
                       
                            //Se estava no estado CLARO e está no ESCURO então foi localizada a parte de cima
                            // da borda desta coluna
                            BordaTemp.Y = yBorda;
                            BordaTemp.TipoBorda = ImageProcessor.BORDA_CLARO_ESCURO;
                            ParamsMLT.BordasColunas.Bordas[x].addElement(BordaTemp);
                        }
                        break;
                    case ESCURO:
                        if (ContaEscuro < ParamsMLT.NumMinEscuro) {
                            if (ContaClaro >= ParamsMLT.NumMinClaro) {
                                estado = CLARO;
                                yBorda = y - ParamsMLT.NumMinEscuro;
                                
                                //Se estava no estado ESCURO e está no CLARO então foi localizada a parte de baixo
                                // da borda desta coluna
                                BordaTemp.Y = yBorda;
                                BordaTemp.TipoBorda = BORDA_ESCURO_CLARO;
                                ParamsMLT.BordasColunas.Bordas[x].addElement(BordaTemp);
                            } else {
                                estado = NADA;
                            }
                        }
                        break;
                }
            }
        }
    }
    
    
    //---------------------------------------------------------------------------
    static void PreparaSelecionaTarja(TParamsABT ParamsABT) {
  
        int x;
        int n, m;
        
        boolean Adicionou;
        TMeioBordas MeioBordasTemp;
        
        for (x = 0; x < ParamsABT.ConjuntoMeioBordas.NumColunas; x++) {
                for (m = 0; m < ParamsABT.ConjuntoMeioBordas.VectorMeioBordas[x].size(); m++) {
                    Adicionou = false;
                    MeioBordasTemp = (TMeioBordas)ParamsABT.ConjuntoMeioBordas.VectorMeioBordas[x].elementAt(m);
                    for (n = 0; n < ParamsABT.VectorTarja.size(); n++) {
                        TTarja current = (TTarja)ParamsABT.VectorTarja.elementAt(n);
                        if ( current.Ativa(x) ) {
                            
                            if ( Math.abs(current.UltYMeio-MeioBordasTemp.yMeio) <= ParamsABT.DistMaxTarjas) {
                                current.VetorAlturas.addElement(new Integer(MeioBordasTemp.Altura));
                                current.UltYMeio = MeioBordasTemp.yMeio;
                                Adicionou = true;
                            }
                        }
                    }
                    if (!Adicionou) {
                        TTarja tempTarja = new TTarja();
                        tempTarja.X = x;
                        tempTarja.UltYMeio = MeioBordasTemp.yMeio;
                        tempTarja.PriYEnc = MeioBordasTemp.Y1;
                        tempTarja.VetorAlturas.addElement(new Integer(MeioBordasTemp.Altura));
                        ParamsABT.VectorTarja.addElement(tempTarja);
                    }
                }
            }
        }
    
//---------------------------------------------------------------------------

    
    static void SelecionaTarja(TParamsABT ParamsABT) {
        
        int n, nMenorX, m, MenorX;
        double soma, desvio, media;
        PreparaSelecionaTarja(ParamsABT);
        MenorX = 0xFFFFFF;
        nMenorX = -1;
        ParamsABT.AchouTarja = false;
        for (n = 0; n < ParamsABT.VectorTarja.size(); n++) {
            TTarja currentTarja = (TTarja)(ParamsABT.VectorTarja.elementAt(n));
            if ((currentTarja.VetorAlturas.size() <= ParamsABT.LargMaxTarja) &&
                    (currentTarja.VetorAlturas.size() >= ParamsABT.LargMinTarja)) {
                soma = 0;
                //tira a média da altura de cada coluna da possível tarja
                for (m = 0; m < currentTarja.VetorAlturas.size(); m++) {
                    soma += ((Integer)(currentTarja.VetorAlturas.elementAt(m))).intValue();
                }
                media = soma / currentTarja.VetorAlturas.size();
                //tira o desvio padrão
                soma = 0;
                for (m = 0; m < currentTarja.VetorAlturas.size(); m++) {
                    soma = Math.abs(media - ((Integer)currentTarja.VetorAlturas.elementAt(m)).intValue() );
                }
                desvio = soma / currentTarja.VetorAlturas.size();
                if (desvio < ParamsABT.DesvioMax) {
                    if (currentTarja.X < MenorX) {
                        MenorX = currentTarja.X;
                        nMenorX = n;
                    }
                }
            }
        }
        if (nMenorX != -1) {
            TTarja tarja = ((TTarja)(ParamsABT.VectorTarja.elementAt(nMenorX)));
            ParamsABT.RefTarja.x = tarja.X;
            ParamsABT.RefTarja.y = tarja.PriYEnc;
            ParamsABT.AchouTarja = true;
        }
    }
//---------------------------------------------------------------------------

    
    
    static void AnalizaBordasTarja(TParamsABT ParamsABT) {
        
        int x, n;
        int YMedio;
        int NumColunas = ParamsABT.BordasColunas.NumColunas;

        ParamsABT.ConjuntoMeioBordas = new TConjuntoMeioBordas(NumColunas);
        
        int dif;
        
        Vector BordasTemp = new Vector();
        TMeioBordas MeioBordasTemp = new TMeioBordas();
        
        for (x = 0; x < NumColunas; x++) {
            BordasTemp = ParamsABT.BordasColunas.Bordas[x];
            for (n = 1; n < BordasTemp.size(); n++) {
                
                TBorda bordaEmbaixo = (TBorda)(BordasTemp.elementAt(n)),
                       bordaEncima  = (TBorda)(BordasTemp.elementAt(n-1));
                
                dif = bordaEmbaixo.Y - bordaEncima.Y;
                
                if ((bordaEmbaixo.TipoBorda == ImageProcessor.BORDA_ESCURO_CLARO) &&
                        (bordaEncima.TipoBorda == ImageProcessor.BORDA_CLARO_ESCURO) &&
                        (dif >= ParamsABT.AltMinTarja) && (dif <= ParamsABT.AltMaxTarja)) {
                    MeioBordasTemp.Inicializa(bordaEncima.Y, bordaEmbaixo.Y);
                    ParamsABT.ConjuntoMeioBordas.VectorMeioBordas[x].addElement(MeioBordasTemp);
                }
            }
        }
        
        SelecionaTarja(ParamsABT);
        
        /*
        if (ParamsABT.AchouTarja) {
            ImgDest[ParamsABT.RefTarja.y][ParamsABT.RefTarja.x].SetMagenta();
            ImgDest[ParamsABT.RefTarja.y + 1][ParamsABT.RefTarja.x].SetMagenta();
            ImgDest[ParamsABT.RefTarja.y][ParamsABT.RefTarja.x + 1].SetMagenta();
            ImgDest[ParamsABT.RefTarja.y + 1][ParamsABT.RefTarja.x + 1].SetMagenta();
        }
         */
    }
    
    
    static byte MediaFaixa(TParamsAI ParamsAI) {
        
        int x, y, xIni, xFim, soma;
        byte [][]ImgSrc = ParamsAI.TCImgSrc.TonsCinza;
        
        xIni = ParamsAI.RefTarja.x;
        xFim = xIni + ParamsAI.LargFaixaRef;
        y = ParamsAI.RefTarja.y - ParamsAI.DistFaixaRef;
        soma = 0;
        
        for (x = xIni; x < xFim; x++) {
            soma += ImgSrc[y][x];
        }
        return (byte)(soma*1.0/ParamsAI.LargFaixaRef);
    }
    
    static void sort(int [] numbers){
        
        boolean sorted = false;
        
        while (!sorted){
            sorted = true;
            for (int i=0; i<(numbers.length-1); i++){
                if (numbers[i]>numbers[i+1]){
                    sorted = false;
                    int temp = numbers[i];
                    numbers[i]=numbers[i+1];
                    numbers[i+1]=temp;
                }
            }
        }
    }
    
    static float RetornaRelacaoMedianasLargurasEncEmb(int []VetorLarguras, int comeco, int fim){
        
        int alt = fim - comeco;
        int [] vetor = new int [alt+1];
        int MetadeAltura = alt / 2;
        int QuartoAltura = (int)(alt*1.0/4);
        
        int Larguras[] = new int[2];
        
        for (int n=0; n < 2; n++) {
            
            // novo codigo -----------------------------
            int j=0;
            for (int i=comeco; i<MetadeAltura; i++){
                vetor[j] = VetorLarguras[comeco+n*MetadeAltura+i];
                j++;
            }
            sort(vetor);
            //------------------------------------------
            
            /* codigo original
            memcpy(vetor, VetorLarguras + comeco + n * MetadeAltura, MetadeAltura * sizeof(int));
            qsort(vetor, MetadeAltura, sizeof(int), ComparaInteiro);
            */
            Larguras[n] = vetor[QuartoAltura];
        }
        
        return (float)(Larguras[0] * 1.0 / Larguras[1]);
    }
//---------------------------------------------------------------------------
   

    static void AnalizaIdentificador(TParamsAI ParamsAI) {
        
        int x, y;
        int xIni, xFim, yIni, yFim;
        int YEnc, YEmb, LargLinha, MaiorLargLinha;
        int XEsq, XDir,
                UltXEnc=0, UltXEmb=0; /// Iniciados apenas para tirar o error warning
        int NumLinha, UltYComLinha, NumPixelsIdentificador;
        int [] VetorLarguras;
        boolean AchouLinha;
        byte [][]ImgSrc = ParamsAI.TCImgSrc.TonsCinza;
       
        xIni = ParamsAI.RefTarja.x - ParamsAI.XIniParaRefTarja;
        xFim = xIni + ParamsAI.LargIdentificador;
        yFim = ParamsAI.RefTarja.y - ParamsAI.YIniParaRefTarja;
        yIni = yFim - ParamsAI.AltIdentificador;
        int TamVetLarguras = yFim - yIni + 1;
        
        VetorLarguras = new int[TamVetLarguras];
        for (int i=0; i<VetorLarguras.length; i++)
            VetorLarguras[i]=0;
        
        byte Media = MediaFaixa(ParamsAI);
        byte Limiar = (byte)(Media - ParamsAI.DifMinMediaFaixaRef);
        
        YEnc = 0;
        YEmb = -1;
        
        MaiorLargLinha = 0;
        NumLinha = 0;
        NumPixelsIdentificador = 0;
        UltYComLinha = -1;
        for (y = yFim; y > yIni; y--) {
            XEsq = -1;
            XDir = 0;
            AchouLinha = false;
            for (x = xIni; x < xFim; x++) {
                if (ImgSrc[y][x] < Limiar) {
                    if (YEmb == -1) {
                        YEmb = y;
                    }
                    YEnc = y;

                    if (XEsq == -1) {
                        XEsq = x;
                    }
                    XDir = x;
                    //se for a primeira linha
                    if (NumLinha == 0) {
                        UltXEmb = x;
                    }
                    UltXEnc = x;
                    AchouLinha = true;

                    NumPixelsIdentificador++;
                }
            }
            LargLinha = XDir - XEsq;
            VetorLarguras[y - yIni] = LargLinha;
            if (LargLinha > MaiorLargLinha) {
                MaiorLargLinha = LargLinha;
            }
            if (AchouLinha) {
                NumLinha++;
                UltYComLinha = y;
            } else {
                if ((UltYComLinha != -1) &&
                        ((UltYComLinha - y) >= ParamsAI.MaiorDistSemPixelsIdentificador) &&
                        (NumPixelsIdentificador > ParamsAI.NumMinPixelsIdentificador)) {
                    break;
                }
            }
        }
        ParamsAI.RelacaoMedianasLargurasEncEmb =
                RetornaRelacaoMedianasLargurasEncEmb(VetorLarguras, YEnc - yIni, YEmb - yIni);
        ParamsAI.Alt = YEmb - YEnc;
        ParamsAI.Inclinacao = (float)((UltXEnc - UltXEmb)*1.0/ParamsAI.Alt);
        ParamsAI.MaiorLargLinha = MaiorLargLinha;
        Identifica(ParamsAI);
        
    }

    
    
//---------------------------------------------------------------------------

    
    static void ReconheceCedula(TParamsRC ParamsRC) {

        MostraLimiteTarja(ParamsRC.ParamsMLT);
        ParamsRC.ParamsABT.BordasColunas = ParamsRC.ParamsMLT.BordasColunas;
        AnalizaBordasTarja(ParamsRC.ParamsABT);

        if (ParamsRC.ParamsABT.AchouTarja) {
            ParamsRC.ParamsAI.RefTarja = ParamsRC.ParamsABT.RefTarja;
            ParamsRC.ParamsAI.TCImgSrc = ParamsRC.ParamsMLT.TCImgSrc;
            AnalizaIdentificador(ParamsRC.ParamsAI);
        }
    }
    
    
    public static void process(Image img){
        
        ParamsRC.ParamsMLT.TCImgSrc = new CTonsCinza(img);
        initializeImage(img);
        ReconheceCedula(ParamsRC);
        
        /* output block --------------
        if (ParamsRC.ParamsABT.AchouTarja) {
            Status("Reconhece Cédula: " + FormatFloat("###,##0.00", (fim - comeco) * PeriodoContador) +
                    " milisegundos, Cédula: R$ " + IntToStr(ParamsRC.ParamsAI.ValorCedula));
        } else {
            Status("Não achou tarja");
        }
        */
    }
    
    static void Identifica(TParamsAI ParamsAI){
        if (ParamsAI.Inclinacao>ParamsAI.LimiarInclinacaoidentificador){
            ParamsAI.ValorCedula=2;
        } else {
            if (ParamsAI.Alt>ParamsAI.LimiarAlturaIdentificador){   
                if (ParamsAI.RelacaoMedianasLargurasEncEmb<ParamsAI.LimiarLargLinhasIdentificador){
                    ParamsAI.ValorCedula=5;    
                } else {
                    ParamsAI.ValorCedula=50;
                }
            } else {
                ParamsAI.ValorCedula=10;
            }
        }
    }
    
};
