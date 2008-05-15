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
//Para cada coluna, verifica nos últimos AltMinClaro pixels quantos pixels são considerados
// como pixels claros, e quantos são considerados escuros. Os pixels são comparados com ClaroMin e
//EscuroMax. Se o número de pixels claros for maior que NumMinClaro, a iteração é colocada no
//estado CLARO. Analogamente, se o número de pixels escuros for maior que NumMinEscuros, a iteração
//é colocada no estado ESCURO. Quando há transição do estado CLARO para ESCURO, este ponto é
//considerado borda superior da tarja. Quando há transição de ESCURO para CLARO, este ponto
//é considerado a parte de baixo da tarja.
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
    //Inicialização do processamento da coluna. É executado por poucos pixels
    for (y=YIni; y<YIni+ParamsMLT.AltMinClaro; y++)
    {
      if (ImgSrc[y][x]>=ParamsMLT.ClaroMin)
        ContaClaro++;
      if (ImgSrc[y][x]<=ParamsMLT.EscuroMax)
        ContaEscuro++;
    }

    for (y=YIni+ParamsMLT.AltMinClaro; y<ParamsMLT.TCImgSrc->Alt; y++)
    {
      //Atualiza a quantidade de pixels claros e de escuros que estão nos últimos AltMinClaro
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
          //se existe uma quantidade suficiente de pixels claros nos últimos pixels da coluna
          // então entra no estado CLARO
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

            //Se estava no estado CLARO e está no ESCURO então foi localizada a parte de cima
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

              //Se estava no estado ESCURO e está no CLARO então foi localizada a parte de baixo
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
  TMeioBordas MeioBordasTemp;                        
  Cor **ImgDest=ParamsABT.BImgDest->PMCor;
  for (x=0; x<NumColunas; x++)
  {
    BordasTemp=ParamsABT.BordasColunas->Bordas[x];
    for (n=1; n<BordasTemp->size(); n++)
    {
      dif=(*BordasTemp)[n].Y-(*BordasTemp)[n-1].Y;
      if (    ((*BordasTemp)[n].TipoBorda==BORDA_ESCURO_CLARO) &&
              ((*BordasTemp)[n-1].TipoBorda==BORDA_CLARO_ESCURO) &&
              (dif>=ParamsABT.AltMinTarja) && (dif<=ParamsABT.AltMaxTarja) )
      {
        MeioBordasTemp.Inicializa((*BordasTemp)[n-1].Y, (*BordasTemp)[n].Y);
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
    for (m=0; m<ParamsABT.ConjuntoMeioBordas->VectorMeioBordas[x]->size(); m++)
    {
      Adicionou=false;
      MeioBordasTemp=&(ParamsABT.ConjuntoMeioBordas->VectorMeioBordas[x]->at(m));
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
  uint n, nMenorX, m;
  int MenorX;
  double soma, desvio, media;
  PreparaSelecionaTarja(ParamsABT);
  MenorX=0xFFFFFF;
  nMenorX=-1;
  ParamsABT.AchouTarja=false;
  for (n=0; n<ParamsABT.VectorTarja.size(); n++)
  {
    if ((ParamsABT.VectorTarja[n].VetorAlturas.size()<=ParamsABT.LargMaxTarja) &&
            (ParamsABT.VectorTarja[n].VetorAlturas.size()>=ParamsABT.LargMinTarja))
    {
      soma=0;
      //tira a média da altura de cada coluna da possível tarja
      for (m=0; m<ParamsABT.VectorTarja[n].VetorAlturas.size(); m++)
        soma+=ParamsABT.VectorTarja[n].VetorAlturas[m];
      media=soma/ParamsABT.VectorTarja[n].VetorAlturas.size();
      //tira o desvio padrão
      soma=0;
      for (m=0; m<ParamsABT.VectorTarja[n].VetorAlturas.size(); m++)
        soma=fabs(media-ParamsABT.VectorTarja[n].VetorAlturas[m]);
      desvio=soma/ParamsABT.VectorTarja[n].VetorAlturas.size();
      if (desvio<ParamsABT.DesvioMax)
      {
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

//Mostra a linha cyan (azul claro) que representa o ponto de referência da tarja para a localização
//do identificador, e retorna a luminosidade média dos pixels da faixa
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
  int x, y;
  int xIni, xFim, yIni, yFim;
  int YEnc, YEmb, LargLinha, MaiorLargLinha;
  int XEsq, XDir, UltXEnc, UltXEmb;
  int NumLinha, UltYComLinha, NumPixelsIdentificador;
  int *VetorLarguras;
  bool AchouLinha;
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
    Log->Add("Média faixa: "+IntToStr(Media));
  #endif
  byte Limiar=Media-ParamsAI.DifMinMediaFaixaRef;
  YEnc=0;
  YEmb=-1;
  MaiorLargLinha=0;
  NumLinha=0;
  NumPixelsIdentificador=0;
  UltYComLinha=-1;
  for (y=yFim; y>yIni; y--)
  {
    XEsq=-1;
    XDir=0;
    AchouLinha=false;
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
      }
    }
    LargLinha=XDir-XEsq;
    VetorLarguras[y-yIni]=LargLinha;
    if (LargLinha>MaiorLargLinha)
      MaiorLargLinha=LargLinha;
    if (AchouLinha)
    {
      NumLinha++;
      UltYComLinha=y;
    }
    else
    {
      if ((UltYComLinha!=-1) &&
                ((UltYComLinha-y)>=ParamsAI.MaiorDistSemPixelsIdentificador)  &&
              (  NumPixelsIdentificador>ParamsAI.NumMinPixelsIdentificador  )  )
        break;
    }
  }
  ParamsAI.RelacaoMedianasLargurasEncEmb=
              RetornaRelacaoMedianasLargurasEncEmb(VetorLarguras, YEnc-yIni, YEmb-yIni);
  ParamsAI.Alt=YEmb-YEnc;
  ParamsAI.Inclinacao=(UltXEnc-UltXEmb)*1.0/ParamsAI.Alt;
  ParamsAI.MaiorLargLinha=MaiorLargLinha;
  Identifica(ParamsAI);
  delete VetorLarguras;
}
//--------------------------------------------------------------------------- 

float RetornaRelacaoMedianasLargurasEncEmb(int *VetorLarguras, int comeco, int fim)
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
  return Larguras[0]*1.0/Larguras[1];
}     
//---------------------------------------------------------------------------

void Identifica(TParamsAI &ParamsAI)
{
  #ifdef DEBUG
    Log->Add("Inclinação identificador: "+FloatToStr(ParamsAI.Inclinacao));
    Log->Add("Altura identificador: "+IntToStr(ParamsAI.Alt));           
    Log->Add("Relação larguras identificador: "+FloatToStr(ParamsAI.RelacaoMedianasLargurasEncEmb));
  #endif
  if (ParamsAI.Inclinacao>ParamsAI.LimiarInclinacaoidentificador)
  {                       
    #ifdef DEBUG
      Log->Add("Inclinação maior que o limite, é R$2");
    #endif
    ParamsAI.ValorCedula=2;
  }
  else
  {
    #ifdef DEBUG
      Log->Add("Inclinação menor que o limite, não é R$2, pode ser R$5   R$50   R$10");
    #endif
    if (ParamsAI.Alt>ParamsAI.LimiarAlturaIdentificador)
    {               
      #ifdef DEBUG
        Log->Add("Altura maior que o limite, pode ser R$5 ou R$50");
      #endif
      if (ParamsAI.RelacaoMedianasLargurasEncEmb<ParamsAI.LimiarLargLinhasIdentificador)
      {
        #ifdef DEBUG
          Log->Add("Relação medianas larguras menor que o limite, é R$5");
        #endif
        ParamsAI.ValorCedula=5;
      }
      else
      {
        #ifdef DEBUG
          Log->Add("Relação medianas larguras maior que o limite, é R$50");
        #endif
        ParamsAI.ValorCedula=50;
      }
    }
    else
    {      
      #ifdef DEBUG
        Log->Add("Altura menor que o limite, é R$10");
      #endif
      ParamsAI.ValorCedula=10;
    }
  }
}
//---------------------------------------------------------------------------
