//---------------------------------------------------------------------------
#pragma hdrstop
#include "UTioPatinhas.h"
//---------------------------------------------------------------------------
#pragma package(smart_init)

#ifdef DEBUG
  extern TStrings *Log;
#endif

void ReconheceCedula(TParamsRC &ParamsRC)
{
  MostraLimiteTarja(ParamsRC.ParamsMLT);
  ParamsRC.ParamsABT.BImgDest=ParamsRC.ParamsMLT.BImgDest;
  ParamsRC.ParamsABT.BordasColunas=ParamsRC.ParamsMLT.BordasColunas;
  AnalizaBordasTarja(ParamsRC.ParamsABT);
  if (ParamsRC.ParamsABT.AchouTarja)
  {
    ParamsRC.ParamsAI.RefTarja=ParamsRC.ParamsABT.RefTarja;
    ParamsRC.ParamsAI.BImgDest=ParamsRC.ParamsABT.BImgDest;
    ParamsRC.ParamsAI.TCImgSrc=ParamsRC.ParamsMLT.TCImgSrc;
    AnalizaIdentificador(ParamsRC.ParamsAI);
  }
}
//---------------------------------------------------------------------------

//Localiza e mostra a tarja.
//Executa o processamento da esquerda para direita, de cima para baixo.
//Para cada coluna, verifica nos �ltimos AltMinClaro pixels quantos pixels s�o considerados
// como pixels claros, e quantos s�o considerados escuros. Os pixels s�o comparados com ClaroMin e
//EscuroMax. Se o n�mero de pixels claros for maior que NumMinClaro, a itera��o � colocada no
//estado CLARO. Analogamente, se o n�mero de pixels escuros for maior que NumMinEscuros, a itera��o
//� colocada no estado ESCURO. Quando h� transi��o do estado CLARO para ESCURO, este ponto �
//considerado borda superior da tarja. Quando h� transi��o de ESCURO para CLARO, este ponto
//� considerado a parte de baixo da tarja.
void MostraLimiteTarja(TParamsMLT &ParamsMLT)
{
  int x, y;
  int yBorda;
  enum TEstado {NADA, CLARO, ESCURO} estado;
  byte **ImgSrc=ParamsMLT.TCImgSrc->TonsCinza;
  Cor **ImgDest=ParamsMLT.BImgDest->PMCor;
  int YIni=ParamsMLT.TCImgSrc->Alt*ParamsMLT.PropYIni;
  int XFim=ParamsMLT.TCImgSrc->Larg*ParamsMLT.PropXFim;
  int ContaClaro, ContaEscuro;
  TBorda BordaTemp;
  ParamsMLT.BordasColunas=new TBordasColunas(XFim);
  for (x=0; x<XFim; x++)
  {
    estado=NADA;
    ContaClaro=ContaEscuro=0;
    //Inicializa��o do processamento da coluna. � executado por poucos pixels
    for (y=YIni; y<YIni+ParamsMLT.AltMinClaro; y++)
    {
      if (ImgSrc[y][x]>=ParamsMLT.ClaroMin)
        ContaClaro++;
      if (ImgSrc[y][x]<=ParamsMLT.EscuroMax)
        ContaEscuro++;
    }

    for (y=YIni+ParamsMLT.AltMinClaro; y<ParamsMLT.TCImgSrc->Alt; y++)
    {
      //Atualiza a quantidade de pixels claros e de escuros que est�o nos �ltimos AltMinClaro
      //pixels da coluna
      if ((ImgSrc[y-ParamsMLT.AltMinClaro][x]>=ParamsMLT.ClaroMin) && ContaClaro>=1)
        ContaClaro--;
      if (ImgSrc[y][x]>=ParamsMLT.ClaroMin)
        ContaClaro++;

      if ((ImgSrc[y-ParamsMLT.AltMinClaro][x]<=ParamsMLT.EscuroMax) && ContaEscuro>=1)
        ContaEscuro--;
      if (ImgSrc[y][x]<=ParamsMLT.EscuroMax)
        ContaEscuro++;
        
      switch (estado)
      {
        case NADA:
          //se existe uma quantidade suficiente de pixels claros nos �ltimos pixels da coluna
          // ent�o entra no estado CLARO
          if (ContaClaro>=ParamsMLT.NumMinClaro)
          {
            estado=CLARO;
            yBorda=y-ParamsMLT.NumMinEscuro;
            ImgDest[yBorda][x].SetVerde();

            BordaTemp.Y=yBorda;
            BordaTemp.TipoBorda=BORDA_ESCURO_CLARO;
            ParamsMLT.BordasColunas->Bordas[x]->push_back(BordaTemp);
          }
          break;
        case CLARO:
          if (ContaEscuro>=ParamsMLT.NumMinEscuro)
          {
            estado=ESCURO;
            yBorda=y-ParamsMLT.NumMinClaro;
            ImgDest[yBorda][x].SetVermelho();

            //Se estava no estado CLARO e est� no ESCURO ent�o foi localizada a parte de cima
            // da borda desta coluna
            BordaTemp.Y=yBorda;
            BordaTemp.TipoBorda=BORDA_CLARO_ESCURO;
            ParamsMLT.BordasColunas->Bordas[x]->push_back(BordaTemp);
          }
          break;
        case ESCURO:
          if (ContaEscuro<ParamsMLT.NumMinEscuro)
          {
            if (ContaClaro>=ParamsMLT.NumMinClaro)
            {
              estado=CLARO;
              yBorda=y-ParamsMLT.NumMinEscuro;
              ImgDest[yBorda][x].SetVerde();

              //Se estava no estado ESCURO e est� no CLARO ent�o foi localizada a parte de baixo
              // da borda desta coluna
              BordaTemp.Y=yBorda;
              BordaTemp.TipoBorda=BORDA_ESCURO_CLARO;
              ParamsMLT.BordasColunas->Bordas[x]->push_back(BordaTemp);
            }
            else
              estado=NADA;
          }
          break;
      }
    }
  }
}
//---------------------------------------------------------------------------

void AnalizaBordasTarja(TParamsABT &ParamsABT)
{
  uint x, n;
  uint YMedio;
  uint NumColunas=ParamsABT.BordasColunas->NumColunas;
  ParamsABT.ConjuntoMeioBordas=new TConjuntoMeioBordas(NumColunas);
  int dif;
  TVectorBorda *BordasTemp;
  TBorda *BordaEnc, *BordaEmb;
  TMeioBordas MeioBordasTemp;                        
  Cor **ImgDest=ParamsABT.BImgDest->PMCor;
  for (x=0; x<NumColunas; x++)
  {
    BordasTemp=ParamsABT.BordasColunas->Bordas[x];
    //percorre todas as bordas da coluna
    for (n=1; n<BordasTemp->size(); n++)
    {
      BordaEnc=&(*BordasTemp)[n-1];
      BordaEmb=&(*BordasTemp)[n];
      dif=BordaEmb->Y-BordaEnc->Y;
      if (    (BordaEmb->TipoBorda==BORDA_ESCURO_CLARO) &&
              (BordaEnc->TipoBorda==BORDA_CLARO_ESCURO) &&
              (dif>=ParamsABT.AltMinTarja) && (dif<=ParamsABT.AltMaxTarja) )
      {
        MeioBordasTemp.Inicializa(BordaEnc->Y, BordaEmb->Y);
        ParamsABT.ConjuntoMeioBordas->VectorMeioBordas[x]->push_back(MeioBordasTemp);
        ImgDest[MeioBordasTemp.yMeio][x].SetAmarelo();
      }
    }
  }
  SelecionaTarja(ParamsABT);
  if (ParamsABT.AchouTarja)
  {
    ImgDest[ParamsABT.RefTarja.y][ParamsABT.RefTarja.x].SetMagenta();
    ImgDest[ParamsABT.RefTarja.y+1][ParamsABT.RefTarja.x].SetMagenta();
    ImgDest[ParamsABT.RefTarja.y][ParamsABT.RefTarja.x+1].SetMagenta();
    ImgDest[ParamsABT.RefTarja.y+1][ParamsABT.RefTarja.x+1].SetMagenta();
  }
}
//---------------------------------------------------------------------------

void PreparaSelecionaTarja(TParamsABT &ParamsABT)
{
  int x;
  uint n, m;
  bool Adicionou;
  TMeioBordas *MeioBordasTemp;
  for (x=0; x<ParamsABT.ConjuntoMeioBordas->NumColunas; x++)
  {
    //percorre todos os meio de bordas da coluna (pixels amarelos)
    for (m=0; m<ParamsABT.ConjuntoMeioBordas->VectorMeioBordas[x]->size(); m++)
    {
      Adicionou=false;
      MeioBordasTemp=&(ParamsABT.ConjuntoMeioBordas->VectorMeioBordas[x]->at(m));
      //percorre todas as tarjas que est�o ativas na coluna corrente
      for (n=0; n<ParamsABT.VectorTarja.size(); n++)
      {
        if (ParamsABT.VectorTarja[n].Ativa(x))
        {
          if (abs(ParamsABT.VectorTarja[n].UltYMeio-MeioBordasTemp->yMeio)<=ParamsABT.DistMaxTarjas)
          {
            ParamsABT.VectorTarja[n].VetorAlturas.push_back(MeioBordasTemp->Altura);
            ParamsABT.VectorTarja[n].UltYMeio=MeioBordasTemp->yMeio;
            Adicionou=true;
          }
        }
      }
      if (!Adicionou)
      {
        ParamsABT.VectorTarja.push_back();
        ParamsABT.VectorTarja.back().X=x;
        ParamsABT.VectorTarja.back().UltYMeio=MeioBordasTemp->yMeio;
        ParamsABT.VectorTarja.back().PriYEnc=MeioBordasTemp->Y1;
        ParamsABT.VectorTarja.back().VetorAlturas.push_back(MeioBordasTemp->Altura);
      }
    }
  }
}
//---------------------------------------------------------------------------

void SelecionaTarja(TParamsABT &ParamsABT)
{
  uint n, nMenorX, m, LargTarjaCandidata;
  TTarja *TarjaCandidata;
  int MenorX;
  double soma, desvio, media;
  PreparaSelecionaTarja(ParamsABT);
  MenorX=0xFFFFFF;
  nMenorX=-1;
  ParamsABT.AchouTarja=false;
  //percorre vector com todas as tarjas
  for (n=0; n<ParamsABT.VectorTarja.size(); n++)
  {
    TarjaCandidata=&(ParamsABT.VectorTarja[n]);
    LargTarjaCandidata=TarjaCandidata->VetorAlturas.size();
    #ifdef DEBUG
      Log->Add("Tarja candidata "+IntToStr(n)+".\tLargura:\t"+IntToStr(LargTarjaCandidata)+
      "\tX:\t"+IntToStr(TarjaCandidata->X));
    #endif
    if ((LargTarjaCandidata<=ParamsABT.LargMaxTarja) &&
            (LargTarjaCandidata>=ParamsABT.LargMinTarja))
    {
      soma=0;
      //calcula a m�dia da altura de cada coluna da poss�vel tarja
      for (m=0; m<ParamsABT.VectorTarja[n].VetorAlturas.size(); m++)
        soma+=ParamsABT.VectorTarja[n].VetorAlturas[m];
      media=soma/ParamsABT.VectorTarja[n].VetorAlturas.size();
      //calcula o desvio padr�o
      soma=0;
      for (m=0; m<ParamsABT.VectorTarja[n].VetorAlturas.size(); m++)
        soma=fabs(media-ParamsABT.VectorTarja[n].VetorAlturas[m]);
        desvio=soma/ParamsABT.VectorTarja[n].VetorAlturas.size();
      #ifdef DEBUG
        Log->Add("desvio padr�o alturas: "+FloatToStr(desvio));
      #endif
      if (desvio<ParamsABT.DesvioMax)
      {
        #ifdef DEBUG
          Log->Add("menor do que o limite de : "+FloatToStr(ParamsABT.DesvioMax));
        #endif
        //pode ser substitu�do por um c�digo que pega o primeiro. mantido para debug
        if (ParamsABT.VectorTarja[n].X<MenorX)
        {
          MenorX=ParamsABT.VectorTarja[n].X;
          nMenorX=n;
        }
      }
    }
  }
  if (nMenorX!=-1)
  {
    ParamsABT.RefTarja.x=ParamsABT.VectorTarja[nMenorX].X;
    ParamsABT.RefTarja.y=ParamsABT.VectorTarja[nMenorX].PriYEnc;
    ParamsABT.AchouTarja=true;
  }
}
//---------------------------------------------------------------------------

//Mostra a linha cyan (azul claro) que representa o ponto de refer�ncia da tarja para a localiza��o
//do identificador, e retorna a luminosidade m�dia dos pixels da faixa
byte MediaFaixa(TParamsAI &ParamsAI)
{
  int x, y, xIni, xFim, soma;
  byte **ImgSrc=ParamsAI.TCImgSrc->TonsCinza;
  Cor **ImgDest=ParamsAI.BImgDest->PMCor;
  xIni=ParamsAI.RefTarja.x;
  xFim=xIni+ParamsAI.LargFaixaRef;
  y=ParamsAI.RefTarja.y-ParamsAI.DistFaixaRef;
  soma=0;
  for (x=xIni; x<xFim; x++)
  {
    soma+=ImgSrc[y][x];
    ImgDest[y][x].SetCyan();
  }
  return soma*1.0/ParamsAI.LargFaixaRef;
}
//---------------------------------------------------------------------------

void AnalizaIdentificador(TParamsAI &ParamsAI)
{
  enum TEstadoUltPixel {NADA, IDENTIFICADOR, FUNDO};
  const int TAM_HIST=100;
  TEstadoUltPixel EstadoUltPixel;
  int x, y;
  int xIni, xFim, yIni, yFim;
  int YEnc, YEmb, LargLinha, MaiorLargLinha, NumBordasPixelLinha;
  int XEsq, XDir, UltXEnc, UltXEmb, MaiorUltXEnc;
  int NumLinha, UltYComLinha, NumPixelsIdentificador;
  int *VetorLarguras;
  bool AchouLinha;
  int HistogramaNumBordasPixelLinha[TAM_HIST]={0};
  byte **ImgSrc=ParamsAI.TCImgSrc->TonsCinza;
  Cor **ImgDest=ParamsAI.BImgDest->PMCor;
  xIni=ParamsAI.RefTarja.x-ParamsAI.XIniParaRefTarja;
  xFim=xIni+ParamsAI.LargIdentificador;
  yFim=ParamsAI.RefTarja.y-ParamsAI.YIniParaRefTarja;
  yIni=yFim-ParamsAI.AltIdentificador;
  int TamVetLarguras=yFim-yIni+1;
  VetorLarguras=new int [TamVetLarguras];
  memset(VetorLarguras, 0, TamVetLarguras*sizeof(int));
  byte Media=MediaFaixa(ParamsAI);
  #ifdef DEBUG
    Log->Add("M�dia faixa: "+IntToStr(Media));
  #endif
  byte Limiar=Media-ParamsAI.DifMinMediaFaixaRef;
  YEnc=0;
  YEmb=-1;
  MaiorLargLinha=0;
  NumLinha=0;
  MaiorUltXEnc=0;
  NumPixelsIdentificador=0;
  UltYComLinha=-1;
  for (y=yFim; y>yIni; y--)
  {
    XEsq=-1;
    XDir=0;
    AchouLinha=false;
    NumBordasPixelLinha=0;
    EstadoUltPixel=NADA;
    for (x=xIni; x<xFim; x++)
    {
      if (ImgSrc[y][x]<Limiar)
      {
        if (YEmb==-1)
          YEmb=y;
        YEnc=y;

        if (XEsq==-1)
          XEsq=x;
        XDir=x;
        //se for a primeira linha
        if (NumLinha==0)
          UltXEmb=x;
        UltXEnc=x;
        AchouLinha=true;

        ImgDest[y][x].SetCyan();
        NumPixelsIdentificador++;
        if (EstadoUltPixel!=IDENTIFICADOR)
          NumBordasPixelLinha++;
        EstadoUltPixel=IDENTIFICADOR;
      }
      else
        EstadoUltPixel=FUNDO;
    }
    LargLinha=XDir-XEsq;
    VetorLarguras[y-yIni]=LargLinha;
    if (LargLinha>MaiorLargLinha)
      MaiorLargLinha=LargLinha;
    if (UltXEnc>MaiorUltXEnc)
      MaiorUltXEnc=UltXEnc;
    if (AchouLinha)
    {
      NumLinha++;
      UltYComLinha=y;
      HistogramaNumBordasPixelLinha[NumBordasPixelLinha]++;
    }
    else
    {
      if ((UltYComLinha!=-1) &&
                ((UltYComLinha-y)>=ParamsAI.MaiorDistSemPixelsIdentificador)  &&
              (  NumPixelsIdentificador>ParamsAI.NumMinPixelsIdentificador  )  )
        break;
    }
  }
  int soma=0;
  for (int w=0; w<TAM_HIST; w++)
    soma+=HistogramaNumBordasPixelLinha[w]*w;
  ParamsAI.NumMedColunas=soma*1.0/NumLinha;
  int MediaLarguras;
  ParamsAI.RelacaoMedianasLargurasEncEmb=
        RetornaRelacaoMedianasLargurasEncEmb(VetorLarguras, YEnc-yIni, YEmb-yIni, MediaLarguras);
  ParamsAI.Alt=YEmb-YEnc;
  if (MediaLarguras>0)
    ParamsAI.RelacaoLargAlt=ParamsAI.Alt*1.0/MediaLarguras;
  else
    ParamsAI.RelacaoLargAlt=0;
  #ifdef DEBUG
    Log->Add("UltXEnc: "+IntToStr(MaiorUltXEnc)+"\tUltXEmb: "+IntToStr(UltXEmb));
  #endif
  ParamsAI.Inclinacao=(MaiorUltXEnc-UltXEmb)*1.0/ParamsAI.Alt;
  ParamsAI.MaiorLargLinha=MaiorLargLinha;
  Identifica(ParamsAI);
  delete VetorLarguras;
}
//--------------------------------------------------------------------------- 

float RetornaRelacaoMedianasLargurasEncEmb(int *VetorLarguras, int comeco, int fim,
                                                                        int  &MediaLarguras)
{
  int alt=fim-comeco;
  int *vetor=new int [alt+1];
  int MetadeAltura=alt/2;
  int QuartoAltura=Round(alt*1.0/4);
  int Larguras[2];
  for (int n=0; n<2; n++)
  {
    memcpy(vetor, VetorLarguras+comeco + n*MetadeAltura, MetadeAltura*sizeof(int));
    qsort(vetor, MetadeAltura, sizeof(int), ComparaInteiro);
    Larguras[n]=vetor[QuartoAltura];
  }
  delete vetor;
  #ifdef DEBUG
    Log->Add("Largura encima: "+IntToStr(Larguras[0]));
    Log->Add("Largura embaixo: "+IntToStr(Larguras[1]));
  #endif
  MediaLarguras=(Larguras[0]+Larguras[1])/2;
  return Larguras[0]*1.0/Larguras[1];
}     
//---------------------------------------------------------------------------

void Identifica(TParamsAI &ParamsAI)
{
  #ifdef DEBUG
    Log->Add("Inclina��o identificador: "+FloatToStr(ParamsAI.Inclinacao));
    Log->Add("Altura identificador: "+IntToStr(ParamsAI.Alt));           
    Log->Add("Rela��o larguras identificador: "+FloatToStr(ParamsAI.RelacaoMedianasLargurasEncEmb));   
    Log->Add("Rela��o inversa larguras identificador: "+FloatToStr(1.0/ParamsAI.RelacaoMedianasLargurasEncEmb)); 
    Log->Add("Rela��o Altura/Largura: "+FloatToStr(ParamsAI.RelacaoLargAlt));                                     
    Log->Add("N�mero m�dio de colunas: "+FloatToStr(ParamsAI.NumMedColunas));
  #endif
  if (ParamsAI.Inclinacao>ParamsAI.LimiarInclinacaoidentificador)
  {                       
    #ifdef DEBUG
      Log->Add("Inclina��o maior que o limite, pode ser \tR$2\tR$20");
    #endif
    if (ParamsAI.RelacaoLargAlt>ParamsAI.LimiarRelacaoLargAlt)
    {
      #ifdef DEBUG
        Log->Add("Rela��o Altura/Largura maior que o limite, � R$2");
      #endif
      ParamsAI.ValorCedula=2;
    }
    else
    {
      #ifdef DEBUG
        Log->Add("Rela��o Altura/Largura menor que o limite, � R$20");
      #endif
      ParamsAI.ValorCedula=20;
    }
  }
  else
  {
    #ifdef DEBUG
      Log->Add("Inclina��o menor que o limite, pode ser \tR$1\tR$5\tR$10\tR$50\tR$100");
    #endif
    if (ParamsAI.Alt>ParamsAI.LimiarAlturaIdentificador)
    {
      #ifdef DEBUG
        Log->Add("Altura maior que o limite, pode ser \tR$1\tR$5\tR$50\tR$100");
      #endif
      if (ParamsAI.RelacaoMedianasLargurasEncEmb>ParamsAI.LimiarLargLinhasIdentificador)
      {
        #ifdef DEBUG
          Log->Add("Rela��o medianas larguras maior que o limite, � R$50");
        #endif
        ParamsAI.ValorCedula=50;
      }
      else if (1.0/ParamsAI.RelacaoMedianasLargurasEncEmb>ParamsAI.LimiarLargLinhasIdentificador)
      {
        #ifdef DEBUG
          Log->Add("Rela��o inversa medianas larguras maior que o limite, � R$100");
        #endif
        ParamsAI.ValorCedula=100;
      }
      else
      {
        #ifdef DEBUG
          Log->Add("Rela��o medianas larguras menor que o limite, pode ser \tR$1\tR$5");
        #endif
        if (ParamsAI.NumMedColunas<ParamsAI.LimiarNumMedColunas)
        {
          #ifdef DEBUG
            Log->Add("N�mero m�dio de colunas menor que o limite, � R$1");
          #endif
          ParamsAI.ValorCedula=1;
        }
        else
        {
          #ifdef DEBUG
            Log->Add("N�mero m�dio de colunas maior que o limite, � R$5");
          #endif
          ParamsAI.ValorCedula=5;
        }
      }
    }
    else
    {
      #ifdef DEBUG
        Log->Add("Altura menor que o limite, � R$10");
      #endif
      ParamsAI.ValorCedula=10;
    }
  }
}
//---------------------------------------------------------------------------
