//---------------------------------------------------------------------------
#include <vcl.h>
#include <math.h>

#ifndef UCBitmapH
#define UCBitmapH
//---------------------------------------------------------------------------

typedef unsigned char uchar;
enum TIndiceCor {AZUL, VERDE, VERMELHO};

typedef Graphics::TBitmap _Bitmap;

struct Cor
{
  byte Azul, Verde, Vermelho;
  void SetAzul();
  void SetVermelho();
  void SetVerde();
  void SetAmarelo();   
  void SetMagenta();
  void SetCyan();
  void SetRGB(byte R, byte G, byte B);
};
//---------------------------------------------------------------------------

struct TDims
{
  byte Dim1, Dim2;
};
//---------------------------------------------------------------------------

class CBitmap
{
private:
  int __fastcall GetAlt();
  int __fastcall GetLarg();
  void SetLarg(int largura);
  void SetAlt(int altura);
  bool BitProprio;
public:
  CBitmap();
  __fastcall CBitmap(int Larg, int Alt);
  __fastcall CBitmap(_Bitmap *BM);
  __fastcall CBitmap(TImage *Imagem);
  __fastcall ~CBitmap();
  void __fastcall ReScan();
  void __fastcall AlocaTonsCinza();
  void __fastcall GeraTonsCinza();
  void __fastcall MostraTonsCinza(bool invertido);
  void __fastcall ImportaTonsCinza(int larg, int alt, byte **vetor, bool invertido);
  Cor **PMCor;
  byte **TonsCinza;
  _Bitmap *Bitmap;
  int larg;
  int alt;
  __property int Larg = {read=GetLarg, write=SetLarg};
  __property int Alt = {read=GetAlt, write=SetAlt};
};
//---------------------------------------------------------------------------

class CTonsCinza
{
private:
  int LargTotal, AltTotal;
  int LargImg, AltImg;
  void SetLarg(int larg);
  void SetAlt(int alt);
public:
  uchar **TonsCinza;
  uchar **TonsCinzaOrig;
  int IndiceAlocacao;
  CTonsCinza(int larg, int alt);
  CTonsCinza(CTonsCinza *TCImg);
  CTonsCinza(CBitmap *BImgSrc);
  CTonsCinza(CBitmap *BImgSrc, TIndiceCor IndiceCor);
  CTonsCinza(TImage *Imagem);
  CTonsCinza(TImage *Imagem, TIndiceCor IndiceCor);
  CTonsCinza(_Bitmap *Bitmap);
  ~CTonsCinza();
  void ImportaCBitmap(CBitmap *BImgSrc);
  void ImportaCBitmap(CBitmap *BImgSrc, TIndiceCor IndiceCor);
  void Aloca(int larg, int alt);
  void Pinta(uchar lum);
  void PintaBranco();
  void ExportaBitmap(_Bitmap *Bitmap, bool invertido);
  void ExportaBitmap(TImage *Imagem, bool invertido);
  void Assign(CTonsCinza *TCSrc);
  void SaveToFile(AnsiString nome);
  __property int Larg = {read=LargImg, write=SetLarg};
  __property int Alt = {read=AltImg, write=SetAlt};
};
//---------------------------------------------------------------------------

class CMatriz2Dim
{
private:
  int LargTotal, AltTotal;
  int LargImg, AltImg;
  void Aloca(int larg, int alt);
  void SetLarg(int larg);
  void SetAlt(int alt);
public:
  TDims **Matriz;
  int IndiceAlocacao;
  CMatriz2Dim(int larg, int alt);
  ~CMatriz2Dim();
  void Pinta(uchar lum);
  void PintaBranco();
  void ExportaBitmap(_Bitmap *Bitmap);
  void Assign(CMatriz2Dim *MDSrc);
  void Grava(AnsiString NomeArq);
  __property int Larg = {read=LargImg, write=SetLarg};
  __property int Alt = {read=AltImg, write=SetAlt};
};
//---------------------------------------------------------------------------

Cor ** MatrizCor(_Bitmap *Imagem);

#endif
