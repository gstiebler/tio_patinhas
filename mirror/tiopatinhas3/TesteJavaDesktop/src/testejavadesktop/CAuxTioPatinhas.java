/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testejavadesktop;

import java.util.*;

/**
 *
 * @author Administrator
 */
public class CAuxTioPatinhas {
}

class TMaiorBorda {

    int y;
    int DifLum;
}

class TRect {

    public int left,  top,  right,  bottom;

    public TRect(int xini, int yini, int xfim, int yfim) {
        left = xini;
        top = yini;
        right = xfim;
        bottom = yfim;
    }

    public int Height() {
        return bottom - top;
    }

    public int Width() {
        return right - left;
    }
}

class TPonto {

    int x;
    int y;
};

class CMatrizInteiro {

    int[][] Matriz;
    int Larg, Alt;

    public CMatrizInteiro(int larg, int alt) {
        Larg = larg;
        Alt = alt;
        Matriz = new int[Alt][Larg];
    }
};
//---------------------------------------------------------------------------
class TLimitesVerticaisGrupo {

    int yEnc, yEmb;

    public TLimitesVerticaisGrupo() {
        yEnc = 0;
        yEmb = 0;
    }
}
//---------------------------------------------------------------------------
class TMeioBordas {

    int Y1, Y2, yMeio, Altura;

    /*public TMeioBordas(TMeioBordas MeioBordas){
    Y1=MeioBordas.Y1;
    Y1=MeioBordas.Y1;
    yMeio=MeioBordas.yMeio;
    Altura=MeioBordas.Altura;
    }*/
    public TMeioBordas(int y1, int y2) {
        Inicializa(y1, y2);
    }

    void Inicializa(int y1, int y2) {
        Y1 = y1;
        Y2 = y2;
        yMeio = (Y1 + Y2) / 2;
        Altura = Y2 - Y1;
    }
};

class TBorda {

    int Y;
    int TipoBorda;
};

class TBordasColunas {

    TVectorBorda[] Bordas;
    int NumColunas;

    TBordasColunas(int numColunas) {

        NumColunas = numColunas;
        Bordas = new TVectorBorda[NumColunas];

        for (int n = 0; n < NumColunas; n++) {
            Bordas[n] = new TVectorBorda();
        }
    }
};

class TVectorMeioBordas extends Vector {

    TVectorMeioBordas() {
        super();
    }

    void adicionaTMeioBordas(TMeioBordas elemento) {
        addElement(elemento);
    }

    TMeioBordas retornaTMeioBordas(int indice) {
        return (TMeioBordas) (elementAt(indice));
    }
}

class TConjuntoMeioBordas {

    TVectorMeioBordas[] VectorMeioBordas;
    int NumColunas;

    TConjuntoMeioBordas(int numColunas) {
        NumColunas = numColunas;
        VectorMeioBordas = new TVectorMeioBordas[NumColunas];
        for (int n = 0; n < NumColunas; n++) {
            VectorMeioBordas[n] = new TVectorMeioBordas();
        }
    }
}
//---------------------------------------------------------------------------
class TVectorInt extends Vector {

    TVectorInt() {
        super();
    }

    void adicionaInteiro(Integer elemento) {
        addElement(elemento);
    }

    Integer retornaInteiro(int indice) {
        return (Integer) (elementAt(indice));
    }
}

class TTarja {

    int X;
    int UltYMeio;
    int PriYEnc;

    TTarja() {
        VetorAlturas = new TVectorInt();
    }

    boolean Ativa(int x) {
        return ((x - X) == VetorAlturas.size());
    }
    TVectorInt VetorAlturas;
};

class TVectorTarja extends Vector {

    TVectorTarja() {
        super();
    }

    void adicionaTTarja(TTarja Tarja) {
        addElement(Tarja);
    }

    TTarja retornaTTarja(int indice) {
        return (TTarja) (elementAt(indice));
    }
}

class TVectorBorda extends Vector {

    TVectorBorda() {
        super();
    }

    void adicionaTBorda(TBorda Borda) {
        addElement(Borda);
    }

    TBorda retornaTBorda(int indice) {
        return (TBorda) (elementAt(indice));
    }
}

class COrdenacao {

    static public void OrdenaInt(int[] Vetor, int TamVet) {
        boolean mudou = true;
        int temp;
        while (mudou) {
            mudou = false;
            for (int n = 1; n < TamVet; n++) {
                if (Vetor[n] < Vetor[n - 1]) {
                    temp = Vetor[n];
                    Vetor[n] = Vetor[n - 1];
                    Vetor[n - 1] = temp;
                    mudou = true;
                }
            }
        }
    }
}

class TParamsMLT {

    CTonsCinza TCImgSrc;
    CBitmap BImgDest;
    //Fator que define a região de busca da tarja. Multiplica-se a largura da imagem por este fator
    float PropYIni;
    float PropXFim;
    //retorno
    TBordasColunas BordasColunas;
};
//---------------------------------------------------------------------------
class TParamsABT {

    CBitmap BImgDest;
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
    TVectorTarja VectorTarja;
    double MediaAlturaTarja;

    TParamsABT() {
        VectorTarja = new TVectorTarja();
        RefTarja = new TPonto();
    }
};
//---------------------------------------------------------------------------
class COutros {

    static void LimitaTRect(TRect ARect) {
        if (ARect.left <= 0) {
            ARect.left = 0;
        }
        if (ARect.top <= 0) {
            ARect.top = 0;
        }
    }
}

class TParamsAI {

    CTonsCinza TCImgSrc;
    CBitmap BImgDest;
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
    //define a altura mínima para que um grupo conexo de pixels possivelmente de identificador
    //seja considerado de fato do identificador
    int AltMinGrupoConexoIdentificador;
    //Limiar que define se um identificador tem 1 ou 2 "andares"
    int LimiarAlturaIdentificador;
    //Limiar que define se um identificador é inclinado ou não
    float LimiarInclinacaoidentificador;
    //Limiar que define a proporção entre a largura mediana da parte de cima e de baixo
    //do identificador
    float LimiarLargLinhasIdentificador;
    //limiar que define a maior relação entre largura e altura do identificador
    float LimiarRelacaoLargAlt;
    float LimiarNumMedColunas;
    //Diferença máxima entre entre a parte de baixo da região do identificador e a parte de baixo.
    //de cada região candidata à região de identificador. Multiplicado pela altura da tarja
    int DifMinEmbGrupoEmbRegiaoIdentificador;    //Retorno
    int Alt;
    float Inclinacao;
    int MaiorLargLinha;
    float RelacaoMedianasLargurasEncEmb;
    float RelacaoLargAlt;
    //número médio de vezes que percorrendo-se da esquerda para direita a região do identificador
    //ocorre do pixel anterior não ser de identificador, e o corrente ser de identificador
    float NumMedColunas;
    int ValorCedula;
};
//---------------------------------------------------------------------------
class TParamsIni {

    int PropYIni;
    int PropXFim;
    int AltMinTarja;
    int AltMaxTarja;
    int DistMaxTarjas;
    int LargMinTarja;
    int LargMaxTarja;
    int DesvioMax;
    int DistFaixaRef;
    int LargFaixaRef;
    int DifMinMediaFaixaRef;
    int LargIdentificador;
    int AltIdentificador;
    int XIniParaRefTarja;
    int YIniParaRefTarja;
    int LimiarAlturaIdentificador;
    int LimiarInclinacaoidentificador;
    int MaiorDistSemPixelsIdentificador;
    int NumMinPixelsIdentificador;
    int AltMinGrupoConexoIdentificador;
    int LimiarLargLinhasIdentificador;
    int LimiarRelacaoLargAlt;
    int LimiarNumMedColunas;
    int DifMinEmbGrupoEmbRegiaoIdentificador;

    public TParamsIni() {
    }
    
    public TParamsIni(TParamsIni outro) {
        copia(outro);
    }
    
    public void copia(TParamsIni outro) {
        PropYIni = outro.PropYIni;
        PropXFim = outro.PropXFim;

        AltMinTarja = outro.AltMinTarja;
        AltMaxTarja = outro.AltMaxTarja;
        DistMaxTarjas = outro.DistMaxTarjas;
        LargMinTarja = outro.LargMinTarja;
        LargMaxTarja = outro.LargMaxTarja;
        DesvioMax = outro.DesvioMax;

        DistFaixaRef = outro.DistFaixaRef;
        LargFaixaRef = outro.LargFaixaRef;
        DifMinMediaFaixaRef = outro.DifMinMediaFaixaRef;
        LargIdentificador = outro.LargIdentificador;
        AltIdentificador = outro.AltIdentificador;
        XIniParaRefTarja = outro.XIniParaRefTarja;
        YIniParaRefTarja = outro.YIniParaRefTarja;
        LimiarAlturaIdentificador = outro.LimiarAlturaIdentificador;
        LimiarInclinacaoidentificador = outro.LimiarInclinacaoidentificador;
        MaiorDistSemPixelsIdentificador = outro.MaiorDistSemPixelsIdentificador;
        NumMinPixelsIdentificador = outro.NumMinPixelsIdentificador;
        AltMinGrupoConexoIdentificador = outro.AltMinGrupoConexoIdentificador;
        LimiarLargLinhasIdentificador = outro.LimiarLargLinhasIdentificador;
        LimiarRelacaoLargAlt = outro.LimiarRelacaoLargAlt;
        LimiarNumMedColunas = outro.LimiarNumMedColunas;
        DifMinEmbGrupoEmbRegiaoIdentificador = outro.DifMinEmbGrupoEmbRegiaoIdentificador;  
    }    

    public TParamsIni(String NomeArquivo) {
        INIFile objINI = new INIFile(NomeArquivo);
        PropYIni = objINI.getIntegerProperty("Geral", "PropYIni");
        PropXFim = objINI.getIntegerProperty("Geral", "PropXFim");

        AltMinTarja = objINI.getIntegerProperty("Geral", "AltMinTarja");
        AltMaxTarja = objINI.getIntegerProperty("Geral", "AltMaxTarja");
        DistMaxTarjas = objINI.getIntegerProperty("Geral", "DistMaxTarjas");
        LargMinTarja = objINI.getIntegerProperty("Geral", "LargMinTarja");
        LargMaxTarja = objINI.getIntegerProperty("Geral", "LargMaxTarja");
        DesvioMax = objINI.getIntegerProperty("Geral", "DesvioMax");

        DistFaixaRef = objINI.getIntegerProperty("Geral", "DistFaixaRef");
        LargFaixaRef = objINI.getIntegerProperty("Geral", "LargFaixaRef");
        DifMinMediaFaixaRef = objINI.getIntegerProperty("Geral", "DifMinMediaFaixaRef");
        LargIdentificador = objINI.getIntegerProperty("Geral", "LargIdentificador");
        AltIdentificador = objINI.getIntegerProperty("Geral", "AltIdentificador");
        XIniParaRefTarja = objINI.getIntegerProperty("Geral", "XIniParaRefTarja");
        YIniParaRefTarja = objINI.getIntegerProperty("Geral", "YIniParaRefTarja");
        LimiarAlturaIdentificador = objINI.getIntegerProperty("Geral", "LimiarAlturaIdentificador");
        LimiarInclinacaoidentificador = objINI.getIntegerProperty("Geral", "LimiarInclinacaoidentificador");
        MaiorDistSemPixelsIdentificador = objINI.getIntegerProperty("Geral", "MaiorDistSemPixelsIdentificador");
        NumMinPixelsIdentificador = objINI.getIntegerProperty("Geral", "NumMinPixelsIdentificador");
        AltMinGrupoConexoIdentificador = objINI.getIntegerProperty("Geral", "AltMinGrupoConexoIdentificador");
        LimiarLargLinhasIdentificador = objINI.getIntegerProperty("Geral", "LimiarLargLinhasIdentificador");
        LimiarRelacaoLargAlt = objINI.getIntegerProperty("Geral", "LimiarRelacaoLargAlt");
        LimiarNumMedColunas = objINI.getIntegerProperty("Geral", "LimiarNumMedColunas");
        DifMinEmbGrupoEmbRegiaoIdentificador = objINI.getIntegerProperty("Geral", "DifMinEmbGrupoEmbRegiaoIdentificador");
    }

    public String dump() {
        return "";
    }
}

class TParamsRC {

    TParamsMLT ParamsMLT;
    TParamsABT ParamsABT;
    TParamsAI ParamsAI;
    int LumMedianaImagem;

    TParamsRC(TParamsIni ParamsIni, CTonsCinza TonsCinza, CBitmap Bitmap) {
        ParamsMLT = new TParamsMLT();
        ParamsABT = new TParamsABT();
        ParamsAI = new TParamsAI();
        CarregaParametros(ParamsIni);
        ParamsMLT.TCImgSrc = TonsCinza;
        ParamsMLT.BImgDest = Bitmap;

        ConverteParametrosDependentesLargura();
    }

    public void CarregaParametros(TParamsIni ParamsIni) {
        ParamsMLT.PropYIni = (float) (ParamsIni.PropYIni / 1000.0);
        ParamsMLT.PropXFim = (float) (ParamsIni.PropXFim / 1000.0);

        ParamsABT.AltMinTarja = ParamsIni.AltMinTarja;
        ParamsABT.AltMaxTarja = ParamsIni.AltMaxTarja;
        ParamsABT.DistMaxTarjas = ParamsIni.DistMaxTarjas;
        ParamsABT.LargMinTarja = ParamsIni.LargMinTarja;
        ParamsABT.LargMaxTarja = ParamsIni.LargMaxTarja;
        ParamsABT.DesvioMax = (float) (ParamsIni.DesvioMax / 1000.0);

        ParamsAI.DistFaixaRef = ParamsIni.DistFaixaRef;
        ParamsAI.LargFaixaRef = ParamsIni.LargFaixaRef;
        ParamsAI.DifMinMediaFaixaRef = ParamsIni.DifMinMediaFaixaRef;
        ParamsAI.LargIdentificador = ParamsIni.LargIdentificador;
        ParamsAI.AltIdentificador = ParamsIni.AltIdentificador;
        ParamsAI.XIniParaRefTarja = ParamsIni.XIniParaRefTarja;
        ParamsAI.YIniParaRefTarja = ParamsIni.YIniParaRefTarja;
        ParamsAI.LimiarAlturaIdentificador = ParamsIni.LimiarAlturaIdentificador;
        ParamsAI.LimiarInclinacaoidentificador = (float) (ParamsIni.LimiarInclinacaoidentificador / 1000.0);
        ParamsAI.MaiorDistSemPixelsIdentificador = ParamsIni.MaiorDistSemPixelsIdentificador;
        ParamsAI.NumMinPixelsIdentificador = ParamsIni.NumMinPixelsIdentificador;
        ParamsAI.AltMinGrupoConexoIdentificador = ParamsIni.AltMinGrupoConexoIdentificador;
        ParamsAI.LimiarLargLinhasIdentificador = (float) (ParamsIni.LimiarLargLinhasIdentificador / 1000.0);
        ParamsAI.LimiarRelacaoLargAlt = (float) (ParamsIni.LimiarRelacaoLargAlt / 1000.0);
        ParamsAI.LimiarNumMedColunas = (float) (ParamsIni.LimiarNumMedColunas / 1000.0);
        ParamsAI.DifMinEmbGrupoEmbRegiaoIdentificador = ParamsIni.DifMinEmbGrupoEmbRegiaoIdentificador;
    }
    //pega os fatores e multiplica para pegar valores absolutos
    public void ConverteParametrosDependentesLargura() {
        ParamsABT.AltMinTarja = (int) MathUtils.round((ParamsABT.AltMinTarja / 1000.0) * ParamsMLT.TCImgSrc.Larg);
        ParamsABT.AltMaxTarja = (int) MathUtils.round((ParamsABT.AltMaxTarja / 1000.0) * ParamsMLT.TCImgSrc.Larg);
        ParamsABT.DistMaxTarjas = (int) MathUtils.round((ParamsABT.DistMaxTarjas / 1000.0) * ParamsMLT.TCImgSrc.Larg);
        ParamsABT.LargMinTarja = (int) MathUtils.round((ParamsABT.LargMinTarja / 1000.0) * ParamsMLT.TCImgSrc.Larg);
        ParamsABT.LargMaxTarja = (int) MathUtils.round((ParamsABT.LargMaxTarja / 1000.0) * ParamsMLT.TCImgSrc.Larg);
    }
    //pega os fatores e multiplica para pegar valores absolutos
    public void ConverteParametrosDependentesAlturaFaixa() {
        ParamsAI.DistFaixaRef = (int) MathUtils.round((ParamsAI.DistFaixaRef / 1000.0) * ParamsABT.MediaAlturaTarja);
        ParamsAI.LargFaixaRef = (int) MathUtils.round((ParamsAI.LargFaixaRef / 1000.0) * ParamsABT.MediaAlturaTarja);
        ParamsAI.LargIdentificador = (int) MathUtils.round((ParamsAI.LargIdentificador / 1000.0) * ParamsABT.MediaAlturaTarja);
        ParamsAI.AltIdentificador = (int) MathUtils.round((ParamsAI.AltIdentificador / 1000.0) * ParamsABT.MediaAlturaTarja);
        ParamsAI.XIniParaRefTarja = (int) MathUtils.round((ParamsAI.XIniParaRefTarja / 1000.0) * ParamsABT.MediaAlturaTarja);
        ParamsAI.YIniParaRefTarja = (int) MathUtils.round((ParamsAI.YIniParaRefTarja / 1000.0) * ParamsABT.MediaAlturaTarja);
        ParamsAI.LimiarAlturaIdentificador = (int) MathUtils.round((ParamsAI.LimiarAlturaIdentificador / 1000.0) * ParamsABT.MediaAlturaTarja);
        ParamsAI.MaiorDistSemPixelsIdentificador = (int) MathUtils.round((ParamsAI.MaiorDistSemPixelsIdentificador / 1000.0) * ParamsABT.MediaAlturaTarja);
        ParamsAI.NumMinPixelsIdentificador = (int) MathUtils.round((ParamsAI.NumMinPixelsIdentificador / 1000.0) * ParamsABT.MediaAlturaTarja);
        ParamsAI.AltMinGrupoConexoIdentificador = (int) MathUtils.round((ParamsAI.AltMinGrupoConexoIdentificador / 1000.0) * ParamsABT.MediaAlturaTarja);
        ParamsAI.DifMinEmbGrupoEmbRegiaoIdentificador = (int) MathUtils.round((ParamsAI.DifMinEmbGrupoEmbRegiaoIdentificador / 1000.0) * ParamsABT.MediaAlturaTarja);
    }
    //pega os fatores e multiplica para pegar valores absolutos
    public void ConverteParametrosDependentesLumMediana() {
        ParamsAI.DifMinMediaFaixaRef = (int) MathUtils.round((ParamsAI.DifMinMediaFaixaRef / 1000.0) * LumMedianaImagem);
    }
};
//---------------------------------------------------------------------------