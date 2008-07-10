//---------------------------------------------------------------------------
#pragma hdrstop
#include "UImgFuncs.h"
//---------------------------------------------------------------------------
#pragma package(smart_init)

void PintaVetor(int *vetor, int tam, _Bitmap *BitDest, TColor cor)
{
  int n;
  float Fator;
  BitDest->Width=tam+2;
  BitDest->Canvas->Brush->Color=clBlack;
  BitDest->Canvas->FillRect(Rect(0, 0, tam+2, 100));
  BitDest->Canvas->Pen->Color=cor;
  BitDest->Height=100;

  int Maior=0;
  for (n=0; n<tam; n++)
    if (vetor[n]>Maior)
      Maior=vetor[n];
  Fator=100.0/Maior;

  for (n=0; n<tam; n++)
  {
    BitDest->Canvas->MoveTo(n, 101);
    BitDest->Canvas->LineTo(n, 101-vetor[n]*Fator);
  }
}     
//---------------------------------------------------------------------------

void Histograma(CTonsCinza *TCImgSrc, _Bitmap *BitHist, _Bitmap *BitCumulativa, int &Mediana)
{
  int vetor[256], vetorCumulativa[256];
  int n, x, y;
  int MetadeTotal;
  byte **ImgSrc=TCImgSrc->TonsCinza;
  memset(vetor, 0, 256*sizeof(int)); 
  memset(vetorCumulativa, 0, 256*sizeof(int));
  for (y=0; y<TCImgSrc->Alt; y++)
    for (x=0; x<TCImgSrc->Larg; x++)
      vetor[ImgSrc[y][x]]++;
  if (BitHist)
    PintaVetor(vetor, 256, BitHist, clBlue);

  vetorCumulativa[0]=vetor[0];
  for (n=1; n<256; n++)
    vetorCumulativa[n]=vetorCumulativa[n-1]+vetor[n];
  if (BitCumulativa)
    PintaVetor(vetorCumulativa, 256, BitCumulativa, clLime);

  MetadeTotal=vetorCumulativa[255]/2;
  for (n=1; n<256; n++)
    if (vetorCumulativa[n]>=MetadeTotal)
    {
      Mediana=n;
      break;
    }
}
//---------------------------------------------------------------------------
