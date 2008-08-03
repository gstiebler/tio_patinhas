/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testejavadesktop;

import java.util.*;
import java.math.*;

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
//---------------------------------------------------------------------------
class TBorda {

    int Y;
    int TipoBorda;
};

class TBordasColunas {

    Vector[] Bordas;
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

    Vector[] VectorMeioBordas;
    int NumColunas;

    TConjuntoMeioBordas(int numColunas) {
        NumColunas = numColunas;
        VectorMeioBordas = new Vector[NumColunas];
        for (int n = 0; n < NumColunas; n++) {
            VectorMeioBordas[n] = new Vector();
        }
    }
}
//---------------------------------------------------------------------------
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
    Vector VectorTarja;
    double MediaAlturaTarja;
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