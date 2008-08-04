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

class TPonto {

    int x;
    int y;
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

class TBorda {

    int Y;
    int TipoBorda;
};

class TBordasColunas {

    TVectorBorda [] Bordas;
    int NumColunas;

    TBordasColunas(int numColunas) {

        NumColunas = numColunas;
        Bordas = new TVectorBorda[NumColunas];

        for (int n = 0; n < NumColunas; n++) {
            Bordas[n] = new TVectorBorda();
        }
    }
};

class TVectorMeioBordas extends Vector{

    TVectorMeioBordas() {
        super();
    }
    
    void adicionaTMeioBordas(TMeioBordas elemento){
        addElement(elemento);
    }

    TMeioBordas retornaTMeioBordas(int indice) {
        return (TMeioBordas) (elementAt(indice));
    }    
}

class TConjuntoMeioBordas {

    TVectorMeioBordas [] VectorMeioBordas;
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
    
    void adicionaInteiro(Integer elemento){
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
    
    TTarja(){
        VetorAlturas=new TVectorInt();
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
    
    void adicionaTTarja(TTarja Tarja){
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
    
    void adicionaTBorda(TBorda Borda){
        addElement(Borda);
    }

    TBorda retornaTBorda(int indice) {
        return (TBorda) (elementAt(indice));
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
    
    TParamsABT(){
        VectorTarja=new TVectorTarja();
        RefTarja=new TPonto();
    }
};
//---------------------------------------------------------------------------
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
class TParamsRC {

    TParamsMLT ParamsMLT;
    TParamsABT ParamsABT;
    TParamsAI ParamsAI;
    int LumMedianaImagem;

    TParamsRC(String NomeArq, CTonsCinza TonsCinza, CBitmap Bitmap) {
        ParamsMLT = new TParamsMLT();
        ParamsABT = new TParamsABT();
        ParamsAI = new TParamsAI();
        CarregaParametros(NomeArq);
        ParamsMLT.TCImgSrc = TonsCinza;
        ParamsMLT.BImgDest = Bitmap;
       
        ConverteParametrosDependentesLargura();
    }
    
    void CarregaParametros(String NomeArq){
        INIFile objINI = null;
        objINI = new INIFile(NomeArq);
        ParamsMLT.PropYIni = (float) (objINI.getIntegerProperty("Geral", "PropYIni") / 1000.0);
        ParamsMLT.PropXFim = (float) (objINI.getIntegerProperty("Geral", "PropXFim") / 1000.0);

        ParamsABT.AltMinTarja = objINI.getIntegerProperty("Geral", "AltMinTarja");
        ParamsABT.AltMaxTarja = objINI.getIntegerProperty("Geral", "AltMaxTarja");
        ParamsABT.DistMaxTarjas = objINI.getIntegerProperty("Geral", "DistMaxTarjas");
        ParamsABT.LargMinTarja = objINI.getIntegerProperty("Geral", "LargMinTarja");
        ParamsABT.LargMaxTarja = objINI.getIntegerProperty("Geral", "LargMaxTarja");
        ParamsABT.DesvioMax = objINI.getIntegerProperty("Geral", "DesvioMax");
        
        ParamsAI.DistFaixaRef = objINI.getIntegerProperty("Geral", "DistFaixaRef");
        ParamsAI.LargFaixaRef = objINI.getIntegerProperty("Geral", "LargFaixaRef");
        ParamsAI.DifMinMediaFaixaRef = objINI.getIntegerProperty("Geral", "DifMinMediaFaixaRef");
        ParamsAI.LargIdentificador = objINI.getIntegerProperty("Geral", "LargIdentificador");
        ParamsAI.AltIdentificador = objINI.getIntegerProperty("Geral", "AltIdentificador");
        ParamsAI.XIniParaRefTarja = objINI.getIntegerProperty("Geral", "XIniParaRefTarja");
        ParamsAI.YIniParaRefTarja = objINI.getIntegerProperty("Geral", "YIniParaRefTarja");
        ParamsAI.LimiarAlturaIdentificador = objINI.getIntegerProperty("Geral", "LimiarAlturaIdentificador");
        ParamsAI.LimiarInclinacaoidentificador = objINI.getIntegerProperty("Geral", "LimiarInclinacaoidentificador");
        ParamsAI.MaiorDistSemPixelsIdentificador = objINI.getIntegerProperty("Geral", "MaiorDistSemPixelsIdentificador");
        ParamsAI.NumMinPixelsIdentificador = objINI.getIntegerProperty("Geral", "NumMinPixelsIdentificador");
        ParamsAI.AltMinGrupoConexoIdentificador = objINI.getIntegerProperty("Geral", "AltMinGrupoConexoIdentificador");
        ParamsAI.LimiarLargLinhasIdentificador = objINI.getIntegerProperty("Geral", "LimiarLargLinhasIdentificador");
        ParamsAI.LimiarRelacaoLargAlt = objINI.getIntegerProperty("Geral", "LimiarRelacaoLargAlt");
        ParamsAI.LimiarNumMedColunas = objINI.getIntegerProperty("Geral", "LimiarNumMedColunas");
        ParamsAI.DifMinEmbGrupoEmbRegiaoIdentificador = objINI.getIntegerProperty("Geral", "DifMinEmbGrupoEmbRegiaoIdentificador");

        objINI = null;
    }
    
    //pega os fatores e multiplica para pegar valores absolutos
    void ConverteParametrosDependentesLargura() {
        ParamsABT.AltMinTarja = (int) Math.round((ParamsABT.AltMinTarja / 1000.0) * ParamsMLT.TCImgSrc.Larg);
        ParamsABT.AltMaxTarja = (int) Math.round((ParamsABT.AltMaxTarja / 1000.0) * ParamsMLT.TCImgSrc.Larg);
        ParamsABT.DistMaxTarjas = (int) Math.round((ParamsABT.DistMaxTarjas / 1000.0) * ParamsMLT.TCImgSrc.Larg);
        ParamsABT.LargMinTarja = (int) Math.round((ParamsABT.LargMinTarja / 1000.0) * ParamsMLT.TCImgSrc.Larg);
        ParamsABT.LargMaxTarja = (int) Math.round((ParamsABT.LargMaxTarja / 1000.0) * ParamsMLT.TCImgSrc.Larg);
    }
    //pega os fatores e multiplica para pegar valores absolutos
    void ConverteParametrosDependentesAlturaFaixa() {
        ParamsAI.DistFaixaRef = (int) Math.round((ParamsAI.DistFaixaRef / 1000.0) * ParamsABT.MediaAlturaTarja);
        ParamsAI.LargFaixaRef = (int) Math.round((ParamsAI.LargFaixaRef / 1000.0) * ParamsABT.MediaAlturaTarja);
        ParamsAI.LargIdentificador = (int) Math.round((ParamsAI.LargIdentificador / 1000.0) * ParamsABT.MediaAlturaTarja);
        ParamsAI.AltIdentificador = (int) Math.round((ParamsAI.AltIdentificador / 1000.0) * ParamsABT.MediaAlturaTarja);
        ParamsAI.XIniParaRefTarja = (int) Math.round((ParamsAI.XIniParaRefTarja / 1000.0) * ParamsABT.MediaAlturaTarja);
        ParamsAI.YIniParaRefTarja = (int) Math.round((ParamsAI.YIniParaRefTarja / 1000.0) * ParamsABT.MediaAlturaTarja);
        ParamsAI.LimiarAlturaIdentificador = (int) Math.round((ParamsAI.LimiarAlturaIdentificador / 1000.0) * ParamsABT.MediaAlturaTarja);
        ParamsAI.MaiorDistSemPixelsIdentificador = (int) Math.round((ParamsAI.MaiorDistSemPixelsIdentificador / 1000.0) * ParamsABT.MediaAlturaTarja);
        ParamsAI.NumMinPixelsIdentificador = (int) Math.round((ParamsAI.NumMinPixelsIdentificador / 1000.0) * ParamsABT.MediaAlturaTarja);
        ParamsAI.AltMinGrupoConexoIdentificador = (int) Math.round((ParamsAI.AltMinGrupoConexoIdentificador / 1000.0) * ParamsABT.MediaAlturaTarja);
        ParamsAI.DifMinEmbGrupoEmbRegiaoIdentificador = (int) Math.round((ParamsAI.DifMinEmbGrupoEmbRegiaoIdentificador / 1000.0) * ParamsABT.MediaAlturaTarja);
    }
    //pega os fatores e multiplica para pegar valores absolutos
    void ConverteParametrosDependentesLumMediana() {
        ParamsAI.DifMinMediaFaixaRef = (int) Math.round((ParamsAI.DifMinMediaFaixaRef / 1000.0) * LumMedianaImagem);
    }
};
//---------------------------------------------------------------------------