//---------------------------------------------------------------------------
#include <vcl.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <math.h>
#pragma hdrstop

#include "UCBitmap.h"

//---------------------------------------------------------------------------

void Cor::SetAzul()
{
  Azul=0xFF;
  Verde=0;
  Vermelho=0;
}
//---------------------------------------------------------------------------  

void Cor::SetVerde()
{
  Azul=0;
  Verde=0xFF;
  Vermelho=0;
}
//---------------------------------------------------------------------------

void Cor::SetVermelho()
{
  Azul=0;
  Verde=0;
  Vermelho=0xFF;
}
//---------------------------------------------------------------------------    

void Cor::SetAmarelo()
{
  Azul=0;
  Verde=0xFF;
  Vermelho=0xFF;
}
//---------------------------------------------------------------------------     

void Cor::SetMagenta()
{
  Azul=0xFF;
  Verde=0;
  Vermelho=0xFF;
}
//---------------------------------------------------------------------------

void Cor::SetCyan()
{
  Azul=0xFF;
  Verde=0xFF;
  Vermelho=0;
}
//---------------------------------------------------------------------------

void Cor::SetRGB(byte R, byte G, byte B)
{
  Azul=B;
  Verde=G;
  Vermelho=R;
}
//---------------------------------------------------------------------------

int inline Round(double valor)
{
  int ipart;
  double fract;
  ipart=valor;
  fract=valor-ipart;
  if (valor>0)
  {
    if(fract >= 0.5)
      ipart++;
  }
  else
  {
    if(fract <= -0.5)
      ipart--;
  }

  return ipart;
}
//---------------------------------------------------------------------------

Cor ** MatrizCor(_Bitmap *Imagem)//!!!ALOCA MEMÓRIA!!!
{
  Imagem->PixelFormat=pf24bit;
  int Altura=Imagem->Height;
  Cor **ImagemCor=new Cor * [Altura];

  for (int y=0; y<Altura; y++)
    ImagemCor[y]=static_cast<Cor *>(Imagem->ScanLine[y]);
  return ImagemCor;
}
//---------------------------------------------------------------------------

CBitmap::CBitmap()
{

}
//---------------------------------------------------------------------------

__fastcall CBitmap::CBitmap(int Larg, int Alt)
{
  Bitmap=new _Bitmap;
  Bitmap->Width=Larg;
  Bitmap->Height=Alt;
  PMCor=MatrizCor(Bitmap);
  BitProprio=true;
  TonsCinza=NULL;
  larg=Larg;
  alt=Alt;
}
//---------------------------------------------------------------------------

__fastcall CBitmap::CBitmap(_Bitmap *BM)
{
  Bitmap=BM;
  PMCor=MatrizCor(Bitmap);
  BitProprio=false;
  TonsCinza=NULL;
  larg=BM->Width;
  alt=BM->Height;
}
//---------------------------------------------------------------------------

__fastcall CBitmap::CBitmap(TImage *Imagem)
{
  Bitmap=Imagem->Picture->Bitmap;
  PMCor=MatrizCor(Bitmap);
  BitProprio=false;
  TonsCinza=NULL;
  larg=Imagem->Picture->Bitmap->Width;
  alt=Imagem->Picture->Bitmap->Height;
}
//---------------------------------------------------------------------------

__fastcall CBitmap::~CBitmap()
{
  delete [] PMCor;
  if (TonsCinza)
  {
    int y;
    for (y=0; y<Alt; y++)
      delete [] TonsCinza[y];
    delete [] TonsCinza;
  }
  if (BitProprio)
    delete Bitmap;
}
//---------------------------------------------------------------------------

void __fastcall CBitmap::ReScan()
{
  delete [] PMCor;
  PMCor=MatrizCor(Bitmap);
}
//---------------------------------------------------------------------------

int __fastcall CBitmap::GetLarg()
{
  return larg;
}
//---------------------------------------------------------------------------

int __fastcall CBitmap::GetAlt()
{
  return alt;
}
//---------------------------------------------------------------------------

void CBitmap::SetLarg(int largura)
{
  if (largura<larg)
    larg=largura;
  else
    ShowMessage("erro");
}
//---------------------------------------------------------------------------

void CBitmap::SetAlt(int altura)
{
  if (altura<alt)
    alt=altura;
  else
    ShowMessage("erro");
}
//---------------------------------------------------------------------------

void __fastcall CBitmap::GeraTonsCinza()
{
  if (TonsCinza)
  {
    for (int y=0; y<Alt; y++)
      delete [] TonsCinza[y];
    delete [] TonsCinza;
  }
  int x, y;
  int altura, largura;
  float lum;
  int inteiro;
  byte *pixelB;
  Cor *pixelC;
  altura=Alt;
  largura=Larg;
  TonsCinza=new byte * [altura];
  for (y=0; y<altura; y++)
  {
    TonsCinza[y]=new byte [largura];
    pixelB=TonsCinza[y];
    pixelC=PMCor[y];
    for (x=0; x<largura; x++)
    {
      inteiro=pixelC->Azul*11+pixelC->Verde*59+pixelC->Vermelho*30;
      lum=Round(inteiro*0.01);
      *pixelB=lum;
      pixelB++;
      pixelC++;
    }
  }
}
//---------------------------------------------------------------------------

void __fastcall CBitmap::AlocaTonsCinza()
{
  if (!TonsCinza)
  {
    int x, y;
    int altura, largura;
    altura=Alt;
    largura=Larg;
    TonsCinza=new byte * [altura];
    for (y=0; y<altura; y++)
    {
      TonsCinza[y]=new byte [largura];
      memset(TonsCinza[y], 0xFF, largura*sizeof(byte));
    }
  }
}
//---------------------------------------------------------------------------

void __fastcall CBitmap::MostraTonsCinza(bool invertido)
{
  if (TonsCinza)
  {
    int x, y;
    int altura, largura;
    Cor *pixel;
    byte *pixelCinza;
    altura=Alt;
    largura=Larg;
    if (invertido)
    {
      for (y=0; y<altura; y++)
      {
        pixel=PMCor[y];
        pixelCinza=TonsCinza[y];
        for (x=0; x<largura; x++)
        {
          pixel->Azul=0xFF-*pixelCinza;
          pixel->Verde=0xFF-*pixelCinza;
          pixel->Vermelho=0xFF-*pixelCinza;
          pixel++;
          pixelCinza++;
        }
      }
    }
    else
    {
      for (y=0; y<altura; y++)
      {
        pixel=PMCor[y];
        pixelCinza=TonsCinza[y];
        for (x=0; x<largura; x++)
        {
          pixel->Azul=*pixelCinza;
          pixel->Verde=*pixelCinza;
          pixel->Vermelho=*pixelCinza;
          pixel++;
          pixelCinza++;
        }
      }
    }
  }
}
//---------------------------------------------------------------------------

void __fastcall CBitmap::ImportaTonsCinza(int larg, int alt, byte **vetor, bool invertido)
{
  int x, y;
  Bitmap->Height=alt;
  Bitmap->Width=larg;
  ReScan();
  TonsCinza=vetor;
  MostraTonsCinza(invertido);
  TonsCinza=NULL;
}
//---------------------------------------------------------------------------

//CTONSCINZA-----------------------------------------------------------------
//---------------------------------------------------------------------------
//---------------------------------------------------------------------------
//---------------------------------------------------------------------------

CTonsCinza::CTonsCinza(int larg, int alt)
{
  Aloca(larg, alt);
}
//---------------------------------------------------------------------------

CTonsCinza::CTonsCinza(CTonsCinza *TCImg)
{
  Aloca(TCImg->Larg, TCImg->Alt);
  Assign(TCImg);
}
//---------------------------------------------------------------------------

CTonsCinza::CTonsCinza(CBitmap *BImgSrc)
{
  TonsCinza=NULL;
  Aloca(BImgSrc->Larg, BImgSrc->Alt);
  ImportaCBitmap(BImgSrc);
}
//---------------------------------------------------------------------------

CTonsCinza::CTonsCinza(CBitmap *BImgSrc, TIndiceCor IndiceCor)
{
  TonsCinza=NULL;
  Aloca(BImgSrc->Larg, BImgSrc->Alt);
  ImportaCBitmap(BImgSrc, IndiceCor);               }
//---------------------------------------------------------------------------

CTonsCinza::CTonsCinza(TImage *Imagem)
{
  CBitmap *BImgSrc=new CBitmap(Imagem);
  TonsCinza=NULL;
  Aloca(BImgSrc->Larg, BImgSrc->Alt);
  ImportaCBitmap(BImgSrc);
  delete BImgSrc;
}
//---------------------------------------------------------------------------

CTonsCinza::CTonsCinza(TImage *Imagem, TIndiceCor IndiceCor)
{
  CBitmap *BImgSrc=new CBitmap(Imagem);
  TonsCinza=NULL;
  Aloca(BImgSrc->Larg, BImgSrc->Alt);
  ImportaCBitmap(BImgSrc, IndiceCor);
  delete BImgSrc;
}
//---------------------------------------------------------------------------

CTonsCinza::CTonsCinza(_Bitmap *Bitmap)
{
  CBitmap *BImgSrc=new CBitmap(Bitmap);
  TonsCinza=NULL;
  Aloca(BImgSrc->Larg, BImgSrc->Alt);
  ImportaCBitmap(BImgSrc);
  delete BImgSrc;
}
//---------------------------------------------------------------------------

void CTonsCinza::ImportaCBitmap(CBitmap *BImgSrc)
{
  int x, y;
  int inteiro, AltCBitmap;
  float lum;
  byte *pixelB;
  Cor *pixelC;
  Cor **PMCor;
  LargImg=BImgSrc->Larg;
  AltImg=BImgSrc->Alt;
  PMCor=BImgSrc->PMCor;
  for (y=0; y<AltImg; y++)
  {
    pixelB=TonsCinza[y];
    pixelC=PMCor[y];
    for (x=0; x<LargImg; x++)
    {
      inteiro=pixelC->Azul*11+pixelC->Verde*59+pixelC->Vermelho*30;
      lum=Round(inteiro*0.01);
      *pixelB=lum;
      pixelB++;
      pixelC++;
    }
  }
}
//---------------------------------------------------------------------------

void CTonsCinza::ImportaCBitmap(CBitmap *BImgSrc, TIndiceCor IndiceCor)
{
  int x, y;
  int AltCBitmap;
  float lum;
  byte *pixelB;
  Cor *pixelC;
  Cor **PMCor;
  LargImg=BImgSrc->Larg;
  AltImg=BImgSrc->Alt;
  PMCor=BImgSrc->PMCor;
  for (y=0; y<AltImg; y++)
  {
    pixelB=TonsCinza[y];
    pixelC=PMCor[y]+IndiceCor;
    for (x=0; x<LargImg; x++)
    {
      *pixelB=pixelC->Azul;
      pixelB++;
      pixelC++;
    }
  }
}
//---------------------------------------------------------------------------

CTonsCinza::~CTonsCinza()
{
  int y;
  for (y=0; y<AltTotal; y++)
    //delete [] TonsCinza[y];
    delete [] TonsCinzaOrig[y];
  delete [] TonsCinza;
  delete [] TonsCinzaOrig;
}
//---------------------------------------------------------------------------

void CTonsCinza::Aloca(int larg, int alt)
{
  int y;
  uchar *tmp;
  LargTotal=larg;
  AltTotal=alt;
  LargImg=larg;
  AltImg=alt;
  TonsCinza=new uchar * [AltTotal];
  TonsCinzaOrig=new uchar * [AltTotal];
  for (y=0; y<Alt; y++)
  {
    tmp=new uchar [LargTotal+16];
    TonsCinzaOrig[y]=tmp;
    tmp+=12;
    TonsCinza[y]=(char *)( (int)(tmp)-((int)(tmp) % 16) );
    //TonsCinza[y]=new char [LargTotal];
    memset(TonsCinza[y], 0xFF, LargTotal*sizeof(uchar));
  }
}
//---------------------------------------------------------------------------

void CTonsCinza::ExportaBitmap(_Bitmap *Bitmap, bool invertido)
{
  int x, y;
  CBitmap *BImgTemp;
  Cor *pixel;
  Cor **PMCor;
  byte *pixelCinza;
  Bitmap->Width=LargImg;
  Bitmap->Height=AltImg;
  BImgTemp=new CBitmap(Bitmap);
  PMCor=BImgTemp->PMCor;
  if (invertido)
  {
    for (y=0; y<AltImg; y++)
    {
      pixel=PMCor[y];
      pixelCinza=TonsCinza[y];
      for (x=0; x<LargImg; x++)
      {
        pixel->Azul=0xFF-*pixelCinza;
        pixel->Verde=0xFF-*pixelCinza;
        pixel->Vermelho=0xFF-*pixelCinza;
        pixel++;
        pixelCinza++;
      }
    }
  }
  else
  {
    for (y=0; y<AltImg; y++)
    {
      pixel=PMCor[y];
      pixelCinza=TonsCinza[y];
      for (x=0; x<LargImg; x++)
      {
        pixel->Azul=*pixelCinza;
        pixel->Verde=*pixelCinza;
        pixel->Vermelho=*pixelCinza;
        pixel++;
        pixelCinza++;
      }
    }
  }
  delete BImgTemp;
}
//---------------------------------------------------------------------------

void CTonsCinza::ExportaBitmap(TImage *Imagem, bool invertido)
{
  ExportaBitmap(Imagem->Picture->Bitmap, invertido);
  Imagem->Repaint();
}
//---------------------------------------------------------------------------

void CTonsCinza::Pinta(uchar lum)
{
  int y;
  uchar **linha;
  linha=TonsCinza;
  for (y=0; y<AltImg; y++)
  {
    memset(*linha, lum, LargImg*sizeof(uchar));
    linha++;
  }
}
//---------------------------------------------------------------------------

void CTonsCinza::PintaBranco()
{
  Pinta(0xFF);
}
//---------------------------------------------------------------------------

void CTonsCinza::SetLarg(int larg)
{
  if (larg<=LargTotal)
    LargImg=larg;
  else
  {
    int y;
    LargTotal=larg;
    LargImg=larg;
    for (y=0; y<AltTotal; y++)
    {
      delete TonsCinza[y];
      TonsCinza[y]=new uchar [LargTotal];
    }
  }
}
//---------------------------------------------------------------------------

void CTonsCinza::SetAlt(int alt)
{
  if (alt<=AltTotal)
    AltImg=alt;
  else
  {
    int y;
    uchar **VetTemp;
    VetTemp=new byte * [alt];
    memcpy(VetTemp, TonsCinza, AltTotal*sizeof(uchar));
    delete [] TonsCinza;
    TonsCinza=VetTemp;
    for (y=AltTotal; y<alt; y++)
      TonsCinza[y]=new uchar [LargTotal];
    AltTotal=alt;
    AltImg=alt;
  }
}
//---------------------------------------------------------------------------

void CTonsCinza::Assign(CTonsCinza *TCSrc)
{
  int y;
  uchar **tcSrc;
  SetLarg(TCSrc->Larg);
  SetAlt(TCSrc->Alt);
  tcSrc=TCSrc->TonsCinza;
  for (y=0; y<AltImg; y++)
    memcpy(TonsCinza[y], tcSrc[y], LargImg*sizeof(uchar));
}
//---------------------------------------------------------------------------

void CTonsCinza::SaveToFile(AnsiString nome)
{
  CBitmap *BImgTmp=new CBitmap(LargImg, AltImg);
  BImgTmp->ImportaTonsCinza(LargImg, AltImg, TonsCinza, false);
  BImgTmp->Bitmap->SaveToFile(nome);
  delete BImgTmp;
}
//---------------------------------------------------------------------------

//CMATRIZ2DIM----------------------------------------------------------------
//---------------------------------------------------------------------------
//---------------------------------------------------------------------------
//---------------------------------------------------------------------------

CMatriz2Dim::CMatriz2Dim(int larg, int alt)
{
  Aloca(larg, alt);
}
//---------------------------------------------------------------------------

void CMatriz2Dim::SetLarg(int larg)
{
  if (larg<=LargTotal)
    LargImg=larg;
  else
  {
    int y;
    LargTotal=larg;
    LargImg=larg;
    for (y=0; y<AltTotal; y++)
    {
      delete Matriz[y];
      Matriz[y]=new TDims [LargTotal];
    }
  }
}
//---------------------------------------------------------------------------

void CMatriz2Dim::SetAlt(int alt)
{
  if (alt<=AltTotal)
    AltImg=alt;
  else
  {
    int y;
    TDims **VetTemp;
    VetTemp=new TDims * [alt];
    memcpy(VetTemp, Matriz, AltTotal*sizeof(TDims));
    delete Matriz;
    Matriz=VetTemp;
    for (y=AltTotal; y<alt; y++)
      Matriz[y]=new TDims [LargTotal];
    AltTotal=alt;
    AltImg=alt;
  }
}
//---------------------------------------------------------------------------

CMatriz2Dim::~CMatriz2Dim()
{
  int y;
  for (y=0; y<AltTotal; y++)
    delete [] Matriz[y];
  delete [] Matriz;
}
//---------------------------------------------------------------------------

void CMatriz2Dim::Pinta(uchar lum)
{
  int y;
  TDims **linha;
  linha=Matriz;
  for (y=0; y<AltImg; y++)
  {
    memset(*linha, lum, LargImg*sizeof(TDims));
    linha++;
  }
}
//---------------------------------------------------------------------------

void CMatriz2Dim::PintaBranco()
{
  Pinta(0xFF);
}
//---------------------------------------------------------------------------

void CMatriz2Dim::Assign(CMatriz2Dim *MDSrc)
{
  int y;
  TDims **mdSrc;
  SetLarg(MDSrc->Larg);
  SetAlt(MDSrc->Alt);
  mdSrc=MDSrc->Matriz;
  for (y=0; y<AltImg; y++)
    memcpy(Matriz[y], mdSrc[y], LargImg*sizeof(TDims));
}
//---------------------------------------------------------------------------

void CMatriz2Dim::Aloca(int larg, int alt)
{
  int y;
  LargTotal=larg;
  AltTotal=alt;
  LargImg=larg;
  AltImg=alt;
  Matriz=new TDims * [AltTotal];
  for (y=0; y<Alt; y++)
  {
    Matriz[y]=new TDims [LargTotal];
    memset(Matriz[y], 0xFF, LargTotal*sizeof(TDims));
  }
}
//---------------------------------------------------------------------------

void CMatriz2Dim::ExportaBitmap(_Bitmap *Bitmap)
{
  int x, y;
  CBitmap *BImgTemp;
  Cor *pixel;
  Cor **PMCor;
  TDims *pixelDim;
  Bitmap->Width=LargImg;
  Bitmap->Height=AltImg;
  BImgTemp=new CBitmap(Bitmap);
  PMCor=BImgTemp->PMCor;
  for (y=0; y<AltImg; y++)
  {
    pixel=PMCor[y];
    pixelDim=Matriz[y];
    for (x=0; x<LargImg; x++)
    {
      pixel->Azul=0xFF;
      pixel->Verde=pixelDim->Dim1;
      pixel->Vermelho=pixelDim->Dim2;
      pixel++;
      pixelDim++;
    }
  }
  delete BImgTemp;
}
//---------------------------------------------------------------------------

void CMatriz2Dim::Grava(AnsiString NomeArq)
{
  _Bitmap *Bitmap=new _Bitmap;
  ExportaBitmap(Bitmap);
  Bitmap->SaveToFile(NomeArq);
  delete Bitmap;
}
//---------------------------------------------------------------------------

#pragma package(smart_init)
 