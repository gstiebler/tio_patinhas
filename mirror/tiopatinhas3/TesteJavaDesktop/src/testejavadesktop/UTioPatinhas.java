/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testejavadesktop;

import java.util.Collections;
import java.util.Vector;

/**
 *
 * @author Administrator
 */
public class UTioPatinhas {

    static int BORDA_CLARO_ESCURO = 0,  BORDA_ESCURO_CLARO = 1;
    static int TAM_VETOR_LIMITES_VERTICAIS_GRUPOS = 300;
    static int PIXEL_ACEITO = 1;
    static int PIXEL_NAO_ACEITO = 2;
    static int DELTA_Y_HISTOGRAM = 5,  DELTA_X_HISTOGRAM = 5;

    UTioPatinhas() {
    }

    static void ReconheceCedula(TParamsRC ParamsRC) {
        ParamsRC.LumMedianaImagem = Histograma(ParamsRC.ParamsMLT.TCImgSrc);
        ParamsRC.ConverteParametrosDependentesLumMediana();
        MostraLimiteTarja(ParamsRC.ParamsMLT);
        ParamsRC.ParamsABT.BImgDest = ParamsRC.ParamsMLT.BImgDest;
        ParamsRC.ParamsABT.BordasColunas = ParamsRC.ParamsMLT.BordasColunas;
        AnalizaBordasTarja(ParamsRC.ParamsABT);
        ParamsRC.ConverteParametrosDependentesAlturaFaixa();
        if (ParamsRC.ParamsABT.AchouTarja) {
            ParamsRC.ParamsAI.RefTarja = ParamsRC.ParamsABT.RefTarja;
            ParamsRC.ParamsAI.BImgDest = ParamsRC.ParamsABT.BImgDest;
            ParamsRC.ParamsAI.TCImgSrc = ParamsRC.ParamsMLT.TCImgSrc;
            AnalizaIdentificador(ParamsRC.ParamsAI);
        }
    //EscreveParametros(ParamsRC);
    }

    static int Histograma(CTonsCinza TCImgSrc) {
        int[] vetor = new int[256];
        int[] vetorCumulativa = new int[256];
        int n, x, y;
        int MetadeTotal;
        int Mediana;
        short[][] ImgSrc = TCImgSrc.TonsCinza;
        for (y = 0; y < TCImgSrc.Alt; y += DELTA_Y_HISTOGRAM) {
            for (x = 0; x < TCImgSrc.Larg; x += DELTA_X_HISTOGRAM) {
                vetor[ImgSrc[y][x]]++;
            }
        }
        vetorCumulativa[0] = vetor[0];
        for (n = 1; n < 256; n++) {
            vetorCumulativa[n] = vetorCumulativa[n - 1] + vetor[n];
        }
        Mediana = 0;
        MetadeTotal = vetorCumulativa[255] / 2;
        for (n = 1; n < 256; n++) {
            if (vetorCumulativa[n] >= MetadeTotal) {
                Mediana = n;
                break;
            }
        }
        return Mediana;
    }

    /**
     * Varre a imagem verticalmente detectando grande variação de contraste entre
     * os pixels. Estas variações são marcadas superiormente com uma faixa vermelha
     * e inferiormente com uma faixa verde.
     */
    static void MostraLimiteTarja(TParamsMLT ParamsMLT) {

        int ALT_COLUNA = 3, DY = 2, DIF_MIN_LUM = 15;

        int x, y, j;
        int yBorda;
        int DifLum;
        int UltYBordaCE, UltYBordaEC;

        int MaisEscuroAtual, MaisClaroAtual, MaisClaroAnterior, MaisEscuroAnterior;
        int lum;
        short[][] ImgSrc = ParamsMLT.TCImgSrc.TonsCinza;
        Cor[][] ImgDest = null;
        if (ParamsMLT.BImgDest != null) {
            ImgDest = ParamsMLT.BImgDest.PMCor;
        }
        int YIni = (int) (ParamsMLT.TCImgSrc.Alt * ParamsMLT.PropYIni);
        int XFim = (int) (ParamsMLT.TCImgSrc.Larg * ParamsMLT.PropXFim);
        TBorda BordaTemp;
        TMaiorBorda MaiorBorda = new TMaiorBorda();
        ParamsMLT.BordasColunas = new TBordasColunas(XFim);
        for (x = 0; x < XFim; x++) {
            UltYBordaCE = UltYBordaEC = -1;
            MaiorBorda.DifLum = 0;
            for (y = YIni + ALT_COLUNA; y < (ParamsMLT.TCImgSrc.Alt - ALT_COLUNA); y++) {
                MaisEscuroAtual = 255;
                MaisEscuroAnterior = 255;
                MaisClaroAtual = 0;
                MaisClaroAnterior = 0;

                //define os mais claros e mais escuros atuais
                for (j = y; j < (y + ALT_COLUNA); j++) {
                    lum = ImgSrc[j][x];
                    if (lum > MaisClaroAtual) {
                        MaisClaroAtual = lum;
                    }
                    if (lum < MaisEscuroAtual) {
                        MaisEscuroAtual = lum;
                    }
                }

                //define os mais claros e os mais escuros anteriores
                for (j = y - (ALT_COLUNA + DY); j < (y - DY); j++) {
                    lum = ImgSrc[j][x];
                    if (lum > MaisClaroAnterior) {
                        MaisClaroAnterior = lum;
                    }
                    if (lum < MaisEscuroAnterior) {
                        MaisEscuroAnterior = lum;
                    }
                }

                //      if (x==25 && y==170)
                //        int w=5;

                //borda claro encima, escuro embaixo
                DifLum = MaisEscuroAnterior - MaisClaroAtual;
                if (DifLum > DIF_MIN_LUM) {
                    if (DifLum > MaiorBorda.DifLum) {
                        MaiorBorda.DifLum = DifLum;
                        MaiorBorda.y = y;
                    }
                    UltYBordaCE = y;
                } else {
                    if (y == (UltYBordaCE + 1)) {
                        yBorda = MaiorBorda.y - ALT_COLUNA;
                        if (ImgDest != null) {
                            ImgDest[yBorda][x].SetVermelho();
                        }
                        BordaTemp = new TBorda();
                        BordaTemp.Y = yBorda;
                        BordaTemp.TipoBorda = BORDA_CLARO_ESCURO;
                        ParamsMLT.BordasColunas.Bordas[x].addElement(BordaTemp);

                        MaiorBorda.DifLum = 0;
                    }
                }

                //borda escuro encima, claro embaixo
                DifLum = MaisEscuroAtual - MaisClaroAnterior;
                if (DifLum > DIF_MIN_LUM) {
                    if (DifLum > MaiorBorda.DifLum) {
                        MaiorBorda.DifLum = DifLum;
                        MaiorBorda.y = y;
                    }
                    UltYBordaEC = y;
                } else {
                    if (y == (UltYBordaEC + 1)) {
                        yBorda = MaiorBorda.y - ALT_COLUNA;
                        if (ImgDest != null) {
                            ImgDest[yBorda][x].SetVerde();
                        }
                        BordaTemp = new TBorda();
                        BordaTemp.Y = yBorda;
                        BordaTemp.TipoBorda = BORDA_ESCURO_CLARO;
                        ParamsMLT.BordasColunas.Bordas[x].addElement(BordaTemp);

                        MaiorBorda.DifLum = 0;
                    }
                }
            }
        }
    }

    /**
     * Analiza os limites vermelhos e verdes de cada uma das tarjas, marcando
     * a faixa amarela na distancia média entre as mesmas.
     */
    static void AnalizaBordasTarja(TParamsABT ParamsABT) {

        int x,
                n;
        int NumColunas = ParamsABT.BordasColunas.NumColunas;
        ParamsABT.ConjuntoMeioBordas = new TConjuntoMeioBordas(NumColunas);
        int dif;
        TVectorBorda BordasTemp;
        TBorda BordaEnc, BordaEmb;
        TMeioBordas MeioBordasTemp;//= new TMeioBordas();
        Cor[][] ImgDest = null;
        if (ParamsABT.BImgDest != null) {
            ImgDest = ParamsABT.BImgDest.PMCor;
        }
        for (x = 0; x < NumColunas; x++) {
            BordasTemp = ParamsABT.BordasColunas.Bordas[x];
            //percorre todas as bordas da coluna
            for (n = 1; n < BordasTemp.size(); n++) {
                BordaEnc = BordasTemp.retornaTBorda(n - 1);
                BordaEmb = BordasTemp.retornaTBorda(n);
                dif = BordaEmb.Y - BordaEnc.Y;
                if ((BordaEmb.TipoBorda == BORDA_ESCURO_CLARO) &&
                        (BordaEnc.TipoBorda == BORDA_CLARO_ESCURO) &&
                        (dif >= ParamsABT.AltMinTarja) && (dif <= ParamsABT.AltMaxTarja)) {
                    MeioBordasTemp = new TMeioBordas(BordaEnc.Y, BordaEmb.Y);
                    ParamsABT.ConjuntoMeioBordas.VectorMeioBordas[x].addElement(MeioBordasTemp);
                    if (ImgDest != null) {
                        ImgDest[MeioBordasTemp.yMeio][x].SetAmarelo();
                    }
                }
            }
        }
        SelecionaTarja(ParamsABT);
        if (ParamsABT.AchouTarja) {
            if (ImgDest != null) {
                ImgDest[ParamsABT.RefTarja.y][ParamsABT.RefTarja.x].SetMagenta();
                ImgDest[ParamsABT.RefTarja.y + 1][ParamsABT.RefTarja.x].SetMagenta();
                ImgDest[ParamsABT.RefTarja.y][ParamsABT.RefTarja.x + 1].SetMagenta();
                ImgDest[ParamsABT.RefTarja.y + 1][ParamsABT.RefTarja.x + 1].SetMagenta();
            }
        }
    }

    /**
     * Descarta as tarjas que estao fora dos limites de largura e com a altura
     * variando muito (analizando os desvios padrão calculados em preparaSelecionaTarja)
     */
    static void SelecionaTarja(TParamsABT ParamsABT) {
        int n, nMenorX, m, LargTarjaCandidata;
        TTarja TarjaCandidata;
        int MenorX, AlturaTarjaMColunaN;
        double soma, desvio, media, MediaTarjaSelecionada = 0;
        PreparaSelecionaTarja(ParamsABT);
        MenorX = 0xFFFFFF;
        nMenorX = -1;
        ParamsABT.AchouTarja = false;
        //percorre vector com todas as tarjas
        for (n = 0; n < ParamsABT.VectorTarja.size(); n++) {
            TarjaCandidata = ParamsABT.VectorTarja.retornaTTarja(n);
            LargTarjaCandidata = TarjaCandidata.VetorAlturas.size();
            //ifdef DEBUG
            COutputDebug.WriteOutput("Tarja candidata " + String.valueOf(n) + ".\tLargura:\t" + String.valueOf(LargTarjaCandidata) +
                    "\tX:\t" + String.valueOf(TarjaCandidata.X) +
                    "\tY:\t" + String.valueOf(TarjaCandidata.PriYEnc));
            //endif
            //continua se a tarja possui a largura dentro dos limites
            if ((LargTarjaCandidata <= ParamsABT.LargMaxTarja) &&
                    (LargTarjaCandidata >= ParamsABT.LargMinTarja)) {
                soma = 0;
                //calcula a média da altura de cada coluna da possível tarja
                for (m = 0; m < ParamsABT.VectorTarja.retornaTTarja(n).VetorAlturas.size(); m++) {
                    soma += ParamsABT.VectorTarja.retornaTTarja(n).VetorAlturas.retornaInteiro(m).intValue();
                }
                media = soma / ParamsABT.VectorTarja.retornaTTarja(n).VetorAlturas.size();
                //calcula o desvio padrão
                soma = 0;
                for (m = 0; m < ParamsABT.VectorTarja.retornaTTarja(n).VetorAlturas.size(); m++) {
                    AlturaTarjaMColunaN = ParamsABT.VectorTarja.retornaTTarja(n).VetorAlturas.retornaInteiro(m).intValue();
                    soma = Math.abs(media - AlturaTarjaMColunaN);
                }
                desvio = soma / ParamsABT.VectorTarja.retornaTTarja(n).VetorAlturas.size();
                // ifdef DEBUG
                COutputDebug.WriteOutput("desvio padrão alturas: " + String.valueOf(desvio));
                //endif

                if (desvio < ParamsABT.DesvioMax) {
                    //ifdef DEBUG
                    COutputDebug.WriteOutput("menor do que o limite de : " + String.valueOf(ParamsABT.DesvioMax));
                    //endif
                    //pode ser substituído por um código que pega o primeiro. mantido para debug
                    if (ParamsABT.VectorTarja.retornaTTarja(n).X < MenorX) {
                        MenorX = ParamsABT.VectorTarja.retornaTTarja(n).X;
                        nMenorX = n;
                        MediaTarjaSelecionada = media;
                    }
                }
            }
        }
        if (nMenorX != -1) {
            ParamsABT.RefTarja.x = ParamsABT.VectorTarja.retornaTTarja(nMenorX).X;
            ParamsABT.RefTarja.y = ParamsABT.VectorTarja.retornaTTarja(nMenorX).PriYEnc;
            ParamsABT.MediaAlturaTarja = MediaTarjaSelecionada;
            ParamsABT.AchouTarja = true;
        }
    }

    /**
     * Calcula os desvios padrões das alturas de cada coluna das tarjas detectadas.
     */
    static void PreparaSelecionaTarja(TParamsABT ParamsABT) {
        int x;

        int n, m;
        boolean Adicionou;
        TMeioBordas MeioBordasTemp;
        for (x = 0; x < ParamsABT.ConjuntoMeioBordas.NumColunas; x++) {
            //percorre todos os meio de bordas da coluna (pixels amarelos)
            for (m = 0; m < ParamsABT.ConjuntoMeioBordas.VectorMeioBordas[x].size(); m++) {
                Adicionou = false;
                MeioBordasTemp = ParamsABT.ConjuntoMeioBordas.VectorMeioBordas[x].retornaTMeioBordas(m);
                //percorre todas as tarjas que estão ativas na coluna corrente
                for (n = 0; n < ParamsABT.VectorTarja.size(); n++) {
                    if (ParamsABT.VectorTarja.retornaTTarja(n).Ativa(x)) {
                        if (Math.abs(ParamsABT.VectorTarja.retornaTTarja(n).UltYMeio - MeioBordasTemp.yMeio) <= ParamsABT.DistMaxTarjas) {
                            ParamsABT.VectorTarja.retornaTTarja(n).VetorAlturas.adicionaInteiro(new Integer(MeioBordasTemp.Altura));
                            ParamsABT.VectorTarja.retornaTTarja(n).UltYMeio = MeioBordasTemp.yMeio;
                            Adicionou = true;
                        }
                    }
                }
                if (!Adicionou) {
                    TTarja TarjaTemp = new TTarja();
                    TarjaTemp.X = x;
                    TarjaTemp.UltYMeio = MeioBordasTemp.yMeio;
                    TarjaTemp.PriYEnc = MeioBordasTemp.Y1;
                    TarjaTemp.VetorAlturas.adicionaInteiro(new Integer(MeioBordasTemp.Altura));
                    ParamsABT.VectorTarja.adicionaTTarja(TarjaTemp);
                }
            }
        }
    }
//Mostra a linha cyan (azul claro) que representa o ponto de referência da tarja para a localização
//do identificador, e retorna a luminosidade média dos pixels da faixa

    static int MediaFaixa(TParamsAI ParamsAI) {
        int x, y, xIni, xFim, soma;
        short[][] ImgSrc = ParamsAI.TCImgSrc.TonsCinza;
        Cor[][] ImgDest = null;
        if (ParamsAI.BImgDest != null) {
            ImgDest = ParamsAI.BImgDest.PMCor;
        }
        xIni = ParamsAI.RefTarja.x;
        xFim = xIni + ParamsAI.LargFaixaRef;
        y = ParamsAI.RefTarja.y - ParamsAI.DistFaixaRef;
        soma = 0;
        for (x = xIni; x < xFim; x++) {
            soma += ImgSrc[y][x];
            if (ImgDest != null) {
                ImgDest[y][x].SetCyan();
            }
        }
        double retorno1 = (soma * 1.0 / ParamsAI.LargFaixaRef);
        int retorno = (int) MathUtils.round(retorno1);
        return retorno;
    }
//---------------------------------------------------------------------------

    /**
     * Processa a imagem na região marcada pelo ARect agrupando os grupos conexos de pixels escuros.
     * O ARect guarda a regiao detectada como o identificador na cedula.
     * 
     * entradas de dados: CTonsCinza tcImgSrc, TRect ARect, int limiar
     * saidas de dados: int[][] MatrizGrupos, TLimitesVerticaisGrupo[] VetorLimitesVerticaisGrupo, int[] PonteiroGrupos
     */
    static void MatrizGruposConexos(CTonsCinza tcImgSrc, TRect ARect, int[][] MatrizGrupos, int limiar,
            TLimitesVerticaisGrupo[] VetorLimitesVerticaisGrupo, int[] PonteiroGrupos) {

        int x, y, i, j, n;
        boolean AnteriorDentroGrupo;//informa se o pixel anteriormente processado na linha possuía um grupo
        boolean AchouGrupoEncima;
        boolean EmGrupoNovo = false;
        int ContadorGrupos, GrupoAtual, GrupoEncima;
        short[][] ImgSrc = tcImgSrc.TonsCinza;
        //inicialmente cada grupo aponta para ele mesmo
        for (n = 0; n < TAM_VETOR_LIMITES_VERTICAIS_GRUPOS; n++) {
            PonteiroGrupos[n] = n;
        }
        ContadorGrupos = 1;
        GrupoAtual = 0;

        for (y = ARect.top + 1; y < ARect.bottom; y++) {
            AnteriorDentroGrupo = false;
            for (x = ARect.left + 1; x < ARect.right - 1; x++) {
                if (ImgSrc[y][x] < limiar) {
                    if (!AnteriorDentroGrupo)//se pixel anterior (da esquerda) não possuia grupo, prossegue
                    {
                        EmGrupoNovo = true;
                        AchouGrupoEncima = false;
                        j = y - ARect.top;
                        //procura encima por pixels com grupo
                        for (n = -1; n <= 1; n++) {
                            i = x - ARect.left;
                            GrupoAtual = PonteiroGrupos[MatrizGrupos[j - 1][i + n]];
                            if (GrupoAtual > 0) {
                                AchouGrupoEncima = true;
                                EmGrupoNovo = false;
                                break;
                            }
                        }
                        //se não achou pixels encima então cria um novo grupo
                        if (!AchouGrupoEncima) {
                            GrupoAtual = ContadorGrupos;
                            VetorLimitesVerticaisGrupo[GrupoAtual].yEmb = 0;
                            VetorLimitesVerticaisGrupo[GrupoAtual].yEnc = 0xFFFF;
                            ContadorGrupos++;
                        }
                    } else //já estamos dentro de um grupo
                    {
                        //devemos sempre ver se encima existe algum grupo antigo
                        j = y - ARect.top - 1;
                        i = x - ARect.left + 1;
                        GrupoEncima = PonteiroGrupos[MatrizGrupos[j][i]];
                        if ((MatrizGrupos[j][i] > 0) && (GrupoEncima != GrupoAtual))//tem grupo antigo encima
                        {
                            if (EmGrupoNovo) {
                                PonteiroGrupos[GrupoAtual] = GrupoEncima;
                                GrupoAtual = GrupoEncima;//o grupo atual agora é o de cima
                                EmGrupoNovo = false;
                            } else {
                                PonteiroGrupos[GrupoEncima] = GrupoAtual;
                            }
                        }
                    }
                    j = y - ARect.top;
                    i = x - ARect.left;
                    MatrizGrupos[j][i] = GrupoAtual;
                    AnteriorDentroGrupo = true;
                    //atualiza limites verticais do grupo caso necessário
                    if (j > VetorLimitesVerticaisGrupo[GrupoAtual].yEmb) {
                        VetorLimitesVerticaisGrupo[GrupoAtual].yEmb = j;
                    }
                    if (j < VetorLimitesVerticaisGrupo[GrupoAtual].yEnc) {
                        VetorLimitesVerticaisGrupo[GrupoAtual].yEnc = j;
                    }
                } else //se pixel é mais claro que o limiar
                {
                    AnteriorDentroGrupo = false;
                }
            }
        }
    }
//---------------------------------------------------------------------------

    /**
     * Corta fora dos grupos conexos os grupos cujas alturas sejam muito pequenas
     * em comparação com a altura da faixa preta de referencia detectada.
     */
    static void SelecionaGruposIdentificador(TLimitesVerticaisGrupo[] VetorLimitesVerticaisGrupo,
            char[] VetGruposValidos, int AltMin, int[] PonteiroGrupos, int yFim, int DifMinEmb) {
        int altura, DifEmb;
        int GrupoReal;
        for (int n = 1; n < TAM_VETOR_LIMITES_VERTICAIS_GRUPOS; n++) {
            GrupoReal = PonteiroGrupos[n];
            altura = VetorLimitesVerticaisGrupo[GrupoReal].yEmb - VetorLimitesVerticaisGrupo[GrupoReal].yEnc;
            DifEmb = yFim - VetorLimitesVerticaisGrupo[GrupoReal].yEmb;

            //ifdef DEBUG
            //  if (altura)
            if (altura > 0) {
                COutputDebug.WriteOutput("Região candidata altura: " + String.valueOf(altura) + " DifEmb: " + String.valueOf(DifEmb));
            //endif
            }
            if (altura >= AltMin && DifEmb < DifMinEmb) {
                VetGruposValidos[n] = (char) PIXEL_ACEITO;
            } else {
                VetGruposValidos[n] = (char) PIXEL_NAO_ACEITO;
            }
        }
    }
//---------------------------------------------------------------------------
//MatrizGrupos é variável de entrada e de saída

    static void CopiaGruposValidos(int[][] MatrizGrupos, TRect ARect, char[] VetGruposValidos) {
        int x, y;
        int larg, alt;
        larg = ARect.Width();
        alt = ARect.Height();
        for (y = 0; y < alt; y++) {
            for (x = 0; x < larg; x++) {
                MatrizGrupos[y][x] = VetGruposValidos[MatrizGrupos[y][x]];
            }
        }
    }
    //---------------------------------------------------------------------------

    static void PintaIdentificador(CBitmap BImgDest, TRect ARect, int[][] MatrizGrupos) {
        int x, y;
        int larg, alt;
        Cor[][] ImgDest = BImgDest.PMCor;
        larg = ARect.Width();
        alt = ARect.Height();
        for (y = 0; y < alt; y++) {
            for (x = 0; x < larg; x++) {
                if (MatrizGrupos[y][x] == PIXEL_ACEITO) {
                    ImgDest[y + ARect.top][x + ARect.left].SetCyan();
                } else if (MatrizGrupos[y][x] == PIXEL_NAO_ACEITO) {
                    ImgDest[y + ARect.top][x + ARect.left].SetAzul();
                }
            }
        }
    }
//---------------------------------------------------------------------------

    static float RetornaRelacaoMedianasLargurasEncEmb(int[] VetorLarguras, int comeco, int fim,
            int[] MediaLarguras) {
        if (fim == -1) {
            return 0;
        }
        int alt = fim - comeco;
        int[] vetor = new int[alt + 1];
        int MetadeAltura = alt / 2;
        int QuartoAltura = (int) MathUtils.round(alt * 1.0 / 4);
        int[] Larguras = new int[2];
        for (int n = 0; n < 2; n++) {
            System.arraycopy(VetorLarguras, comeco + n * MetadeAltura, vetor, 0, MetadeAltura);
            COrdenacao.OrdenaInt(vetor, MetadeAltura);
            Larguras[n] = vetor[QuartoAltura];
        }
        COutputDebug.WriteOutput("Largura encima: " + String.valueOf(Larguras[0]));
        COutputDebug.WriteOutput("Largura embaixo: " + String.valueOf(Larguras[1]));
        MediaLarguras[0] = (Larguras[0] + Larguras[1]) / 2;
        if (Larguras[1] > 0) {
            return (float) (Larguras[0] * 1.0 / Larguras[1]);
        } else {
            return -100;
        }
    }
    //---------------------------------------------------------------------------

    /**
     * Pega as informacoes estraidas da imagem pelas outras funcoes e faz a decisao
     * sobre qual identificador foi detectado.
     */
    static void Identifica(TParamsAI ParamsAI) {
        //ifdef DEBUG
        COutputDebug.WriteOutput("Inclinação identificador: " + String.valueOf(ParamsAI.Inclinacao));
        COutputDebug.WriteOutput("Altura identificador: " + String.valueOf(ParamsAI.Alt));
        COutputDebug.WriteOutput("Relação larguras identificador: " + String.valueOf(ParamsAI.RelacaoMedianasLargurasEncEmb));
        if (ParamsAI.RelacaoMedianasLargurasEncEmb > 0) {
            COutputDebug.WriteOutput("Relação inversa larguras identificador: " + String.valueOf(1.0 / ParamsAI.RelacaoMedianasLargurasEncEmb));
        }
        COutputDebug.WriteOutput("Relação Altura/Largura: " + String.valueOf(ParamsAI.RelacaoLargAlt));
        COutputDebug.WriteOutput("Número médio de colunas: " + String.valueOf(ParamsAI.NumMedColunas));
        //endif
        if (ParamsAI.Inclinacao > ParamsAI.LimiarInclinacaoidentificador) {
            //ifdef DEBUG
            COutputDebug.WriteOutput("Inclinação maior que o limite, pode ser \tR$2\tR$20");
            ///endif
            if (ParamsAI.RelacaoLargAlt > ParamsAI.LimiarRelacaoLargAlt) {
                //ifdef DEBUG
                COutputDebug.WriteOutput("Relação Altura/Largura maior que o limite, é R$2");
                //endif
                ParamsAI.ValorCedula = 2;
            } else {
                //ifdef DEBUG
                COutputDebug.WriteOutput("Relação Altura/Largura menor que o limite, é R$20");
                //endif
                ParamsAI.ValorCedula = 20;
            }
        } else {
            //ifdef DEBUG
            COutputDebug.WriteOutput("Inclinação menor que o limite, pode ser \tR$1\tR$5\tR$10\tR$50\tR$100");
            //endif
            if (ParamsAI.Alt > ParamsAI.LimiarAlturaIdentificador) {
                //ifdef DEBUG
                COutputDebug.WriteOutput("Altura maior que o limite, pode ser \tR$1\tR$5\tR$50\tR$100");
                //endif
                if (ParamsAI.RelacaoMedianasLargurasEncEmb > ParamsAI.LimiarLargLinhasIdentificador) {
                    //ifdef DEBUG
                    COutputDebug.WriteOutput("Relação medianas larguras maior que o limite, é R$50");
                    //endif
                    ParamsAI.ValorCedula = 50;
                } else if (1.0 / ParamsAI.RelacaoMedianasLargurasEncEmb > ParamsAI.LimiarLargLinhasIdentificador) {
                    //ifdef DEBUG
                    COutputDebug.WriteOutput("Relação inversa medianas larguras maior que o limite, é R$100");
                    //endif
                    ParamsAI.ValorCedula = 100;
                } else {
                    //ifdef DEBUG
                    COutputDebug.WriteOutput("Relação medianas larguras menor que o limite, pode ser \tR$1\tR$5");
                    //endif
                    if (ParamsAI.ProfundezaVale < ParamsAI.ProfundezaValeMin) {
                        //ifdef DEBUG
                        COutputDebug.WriteOutput("Número médio de colunas menor que o limite, é R$1");
                        //endif
                        ParamsAI.ValorCedula = 1;
                    } else {
                        //ifdef DEBUG
                        COutputDebug.WriteOutput("Número médio de colunas maior que o limite, é R$5");
                        //endif
                        ParamsAI.ValorCedula = 5;
                    }
                }
            } else {
                //ifdef DEBUG
                COutputDebug.WriteOutput("Altura menor que o limite, é R$10");
                //endif
                ParamsAI.ValorCedula = 10;
            }
        }
    }
//---------------------------------------------------------------------------

    /**
     * Analiza o identificador para detectar o valor das notas.
     */
    static void AnalizaIdentificador(TParamsAI ParamsAI) {

        int NADA = 0, IDENTIFICADOR = 1, FUNDO = 2;
        int TAM_HIST = 200;
        int EstadoUltPixel;
        int x, y;
        int xIni, xFim, yIni, yFim;
        int YEnc, YEmb, LargLinha, MaiorLargLinha, NumBordasPixelLinha;
        //usadas para guardar os limites laterais de uma linha
        int XEsq, XDir;
        int UltXEnc = 0, UltXEmb = 0, MaiorUltXEnc;
        int NumLinha, UltYComLinha, NumPixelsIdentificador;
        short[][] ImgSrc = ParamsAI.TCImgSrc.TonsCinza;
        //vetores que armazenam em cada posição, qual o pixel mais escuro até o momento,
        //processando ea esquerda para a direita e da direita para a esquerda.
        //usados para diferenciar 5 de 1
        int[] VetorMaisEscuroEsqDir, VetorMaisEscuroDirEsq;
        //variável que armazena em cada linha o ponto mais claro entre dois pontos escuros.
        //Usada para diferenciar 5 de 1
        int PontoMaisFundoVale;
        //Usada para diferenciar 5 de 1
        int MaisEscuroAteAgora;
        //Usada para diferenciar 5 de 1. Cada ponto representa o valor do ponto mais
        //fundo do vale de cada linha
        Vector PontosMaisFundosVale = new Vector();
        int yImagemOriginal;
        int[] VetorLarguras;
        char[] VetGruposValidos = new char[TAM_VETOR_LIMITES_VERTICAIS_GRUPOS];
        //usado para indicar grupos conexos que fazem parte de outros grupos
        int[] PonteiroGrupos = new int[TAM_VETOR_LIMITES_VERTICAIS_GRUPOS];
        boolean AchouLinha;
        int[] HistogramaNumBordasPixelLinha = new int[TAM_HIST];
        TRect ARect;
        xIni = ParamsAI.RefTarja.x - ParamsAI.XIniParaRefTarja;
        xFim = xIni + ParamsAI.LargIdentificador;
        yFim = ParamsAI.RefTarja.y - ParamsAI.YIniParaRefTarja;
        yIni = yFim - ParamsAI.AltIdentificador;
        int TamVetLarguras = yFim - yIni + 1;
        VetorLarguras = new int[TamVetLarguras];
        int Media = MediaFaixa(ParamsAI);
        COutputDebug.WriteOutput("Luminosidade média faixa: " + String.valueOf(Media));
        int Limiar = Media - ParamsAI.DifMinMediaFaixaRef;

        CMatrizInteiro MatrizGrupos = new CMatrizInteiro(xFim - xIni + 1, yFim - yIni + 1);
        TLimitesVerticaisGrupo[] VetorLimitesVerticaisGrupo =
                new TLimitesVerticaisGrupo[TAM_VETOR_LIMITES_VERTICAIS_GRUPOS];
        for (int s = 0; s < TAM_VETOR_LIMITES_VERTICAIS_GRUPOS; s++) {
            VetorLimitesVerticaisGrupo[s] = new TLimitesVerticaisGrupo();
        }
        ARect = new TRect(xIni, yIni, xFim, yFim);
        COutros.LimitaTRect(ARect);
        VetorMaisEscuroEsqDir = new int[ARect.Width()];
        VetorMaisEscuroDirEsq = new int[ARect.Width()];
        MatrizGruposConexos(ParamsAI.TCImgSrc, ARect,
                MatrizGrupos.Matriz, Limiar, VetorLimitesVerticaisGrupo, PonteiroGrupos);
        SelecionaGruposIdentificador(VetorLimitesVerticaisGrupo, VetGruposValidos,
                ParamsAI.AltMinGrupoConexoIdentificador, PonteiroGrupos, ARect.Height(),
                ParamsAI.DifMinEmbGrupoEmbRegiaoIdentificador);
        CopiaGruposValidos(MatrizGrupos.Matriz, ARect, VetGruposValidos);
        if (ParamsAI.BImgDest != null) {
            PintaIdentificador(ParamsAI.BImgDest, ARect, MatrizGrupos.Matriz);
        }

        YEnc = 0;
        YEmb = -1;
        MaiorLargLinha = 0;
        NumLinha = 0;
        MaiorUltXEnc = 0;
        NumPixelsIdentificador = 0;
        UltYComLinha = -1;
        COutputDebug.WriteOutput("Área do identificador: X: (" + String.valueOf(xIni) + ", " +
                String.valueOf(xFim) + " Y: (" + String.valueOf(yIni) + ", " + String.valueOf(yFim) + ")");
        //percorre a imagem de baixo até encima
        for (y = ARect.Height(); y > 0; y--) {
            XEsq = -1;
            XDir = 0;
            AchouLinha = false;
            NumBordasPixelLinha = 0;
            EstadoUltPixel = NADA;
            //percorre a linha para determinar os lites da linha e contar pixels
            //do identificador
            for (x = 0; x < ARect.Width(); x++) {
                if (MatrizGrupos.Matriz[y][x] == PIXEL_ACEITO) {
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
                    if (EstadoUltPixel != IDENTIFICADOR) {
                        NumBordasPixelLinha++;
                    }
                    EstadoUltPixel = IDENTIFICADOR;
                } else {
                    EstadoUltPixel = FUNDO;
                }
            }

            int LarguraLinha=XDir-XEsq+1;
            if (LarguraLinha>1)
            {
            yImagemOriginal = y + yIni;
            MaisEscuroAteAgora = 255;
            int contador = 0;
            for (x = xIni + XEsq; x <= (xIni + XDir); x++) {
                if (ImgSrc[yImagemOriginal][x] < MaisEscuroAteAgora) {
                    MaisEscuroAteAgora = ImgSrc[yImagemOriginal][x];
                }
                VetorMaisEscuroEsqDir[contador++] = MaisEscuroAteAgora;
            }

            MaisEscuroAteAgora = 255;
            contador = XDir - XEsq;
            for (x = xIni + XDir; x >= (xIni + XEsq); x--) {
                if (ImgSrc[yImagemOriginal][x] < MaisEscuroAteAgora) {
                    MaisEscuroAteAgora = ImgSrc[yImagemOriginal][x];
                }
                VetorMaisEscuroDirEsq[contador--] = MaisEscuroAteAgora;
            }

            int MaisClaro;
            int ProfundidadeVale;
            contador = 0;
            int MaiorProfundidadeVale = 0;
            for (x = xIni + XEsq; x <= (xIni + XDir); x++) {
                MaisClaro = VetorMaisEscuroDirEsq[contador];
                if (MaisClaro < VetorMaisEscuroEsqDir[contador]) {
                    MaisClaro = VetorMaisEscuroEsqDir[contador];
                }
                ProfundidadeVale = ImgSrc[yImagemOriginal][x] - MaisClaro;
                if (ProfundidadeVale > MaiorProfundidadeVale) {
                    MaiorProfundidadeVale = ProfundidadeVale;
                }
                contador++;
            }
            if ((XDir - XEsq) > 1) {
                PontosMaisFundosVale.addElement(new Integer(MaiorProfundidadeVale));
            }
            

            LargLinha = XDir - XEsq;
            VetorLarguras[y] = LargLinha;
            if (LargLinha > MaiorLargLinha) {
                MaiorLargLinha = LargLinha;
            }
            if (AchouLinha) {
                if (UltXEnc > MaiorUltXEnc) {
                    MaiorUltXEnc = UltXEnc;
                }
                NumLinha++;
                UltYComLinha = y;
                HistogramaNumBordasPixelLinha[NumBordasPixelLinha]++;
            } else {
                if ((UltYComLinha != -1) &&
                        ((UltYComLinha - y) >= ParamsAI.MaiorDistSemPixelsIdentificador) &&
                        (NumPixelsIdentificador > ParamsAI.NumMinPixelsIdentificador)) {
                    break;
                }
            }
        }//for (y = ARect.Height(); y > 0; y--) {
        if (PontosMaisFundosVale.size() > 0) {
            Collections.sort(PontosMaisFundosVale, new ComparaInteiro());
            int IndiceVetor = (int) Math.round(PontosMaisFundosVale.size() * 0.15);
            ParamsAI.ProfundezaVale =
                    ((Integer) PontosMaisFundosVale.elementAt(IndiceVetor)).intValue();
        }
        else
            ParamsAI.ProfundezaVale=0;
        }
        int soma = 0;
        for (int w = 0; w < TAM_HIST; w++) {
            soma += HistogramaNumBordasPixelLinha[w] * w;
        }
        if (NumLinha > 0) {
            ParamsAI.NumMedColunas = (float) (soma * 1.0 / NumLinha);
        } else {
            ParamsAI.NumMedColunas = -100;
        }
        int[] MediaLarguras = new int[1];
        ParamsAI.RelacaoMedianasLargurasEncEmb =
                RetornaRelacaoMedianasLargurasEncEmb(VetorLarguras, YEnc, YEmb, MediaLarguras);
        ParamsAI.Alt = YEmb - YEnc;
        if (MediaLarguras[0] > 0) {
            ParamsAI.RelacaoLargAlt = (float) (ParamsAI.Alt * 1.0 / MediaLarguras[0]);
        } else {
            ParamsAI.RelacaoLargAlt = 0;
            //ifdef DEBUG
            COutputDebug.WriteOutput("UltXEnc: " + String.valueOf(MaiorUltXEnc) + "\tUltXEmb: " + String.valueOf(UltXEmb));
            COutputDebug.WriteOutput("YEmb: " + String.valueOf(YEmb) + "\t\tYEnc: " + String.valueOf(YEnc));
        //endif
        }
        if (ParamsAI.Alt > 0) {
            ParamsAI.Inclinacao = (float) ((MaiorUltXEnc - UltXEmb) * 1.0 / ParamsAI.Alt);
        } else {
            ParamsAI.Inclinacao = -100;
        }
        ParamsAI.MaiorLargLinha = MaiorLargLinha;
        Identifica(ParamsAI);
    //delete [] VetorLarguras;
    //delete ARect;
    //delete MatrizGrupos;
    }
//--------------------------------------------------------------------------- 
}
    