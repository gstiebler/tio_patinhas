//---------------------------------------------------------------------------
#include <vcl.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <math.h>
#pragma hdrstop                

#include "UZoom.h"
//---------------------------------------------------------------------------
#pragma package(smart_init)
#pragma resource "*.dfm"
TZoom *Zoom;
//---------------------------------------------------------------------------
__fastcall TZoom::TZoom(TComponent* Owner)
  : TForm(Owner)
{
}               
//---------------------------------------------------------------------------

void __fastcall TZoom::CarregaBitmap(_Bitmap *Bitmap)
{
  Image1->Picture->Bitmap->Assign(Bitmap);
  BitTemp->Assign(Bitmap);
  Image2->Picture->Bitmap->Assign(Bitmap);
  FatorPropX=Bitmap->Width*1.0/Image2->Width;
  FatorPropY=Bitmap->Height*1.0/Image2->Height;
  AjustaTamanhoFoto();
}
//---------------------------------------------------------------------------

void __fastcall TZoom::Dimensiona(float Fator)
{
  Image1->Width=Image1->Picture->Bitmap->Width*Fator;
  Image1->Height=Image1->Picture->Bitmap->Height*Fator;
  sbFator->Position=int(Fator*10);
  FatorAumento=Fator;
}
//---------------------------------------------------------------------------
void __fastcall TZoom::sbFatorChange(TObject *Sender)
{
  int fator;
  fator=sbFator->Position/10;
  Dimensiona(fator);
  Status("Fator de ampliação: "+FormatFloat("0.00", fator));
}
//---------------------------------------------------------------------------

void __fastcall TZoom::Status(AnsiString Text)
{
  StatusBar1->Panels->Items[0]->Text=Text;
  StatusBar1->Repaint();
}
//---------------------------------------------------------------------------

void __fastcall TZoom::Image1MouseDown(TObject *Sender,
      TMouseButton Button, TShiftState Shift, int X, int Y)
{
  int r, g, b;
  MostraInf(X, Y, r, g, b);
  sbAzul->Position=b;
  sbVerde->Position=g;
  sbVermelho->Position=r;
  UltClickX=X/FatorAumento;
  UltClickY=Y/FatorAumento;
}
//---------------------------------------------------------------------------

void __fastcall TZoom::tbFundoCorChange(TObject *Sender)
{
  edFundoCor->Text=IntToStr(tbFundoCor->Position);
  LumTresh(BitTemp, Image1->Picture->Bitmap, tbFundoCor->Position,
            cbMarcaCorte->Checked, cbOriginal->Checked, rgCor->ItemIndex);
  Image1->Repaint();
  edFundoCor->Repaint();
}
//---------------------------------------------------------------------------

void __fastcall TZoom::FormCreate(TObject *Sender)
{
  BitTemp=new _Bitmap;
  BitCores=new _Bitmap;
  BitCores=imCores->Picture->Bitmap;
  BitCores->Width=imCores->Width;
  BitCores->Height=imCores->Height;
  BitCores->Canvas->Brush->Color=clBlack;
  BitCores->Canvas->FillRect(Rect(0, 0, 1000, 1000));
  edFundoCor->Text=IntToStr(tbFundoCor->Position);
  pressionado=false;
  Edits=NULL;
}
//---------------------------------------------------------------------------

void __fastcall TZoom::FormDestroy(TObject *Sender)
{
  delete BitTemp;  
}
//---------------------------------------------------------------------------

void __fastcall TZoom::MostraCores(int Azul, int Verde, int Vermelho)
{
  TCanvas *Canvas=BitCores->Canvas;
  Canvas->Brush->Color=clBlack;
  Canvas->FillRect(Rect(0, 0, 1000, 1000));
  Canvas->Brush->Color=TColor(0xFF0000);
  Canvas->FillRect(Rect(0, 0, Azul, 9));
  Canvas->Brush->Color=TColor(0xFF00);
  Canvas->FillRect(Rect(0, 10, Verde, 19));
  Canvas->Brush->Color=TColor(0xFF);
  Canvas->FillRect(Rect(0, 20, Vermelho, 29));
  imCores->Repaint();
}
//---------------------------------------------------------------------------

void __fastcall TZoom::Image1MouseMove(TObject *Sender, TShiftState Shift,
      int X, int Y)
{
  /*static int contador=0;
  if (pressionado)
  {
    if (contador)
    {
      ScrollBox1->HorzScrollBar->Position+=UltX-X;
      ScrollBox1->VertScrollBar->Position+=UltY-Y;
    }
    contador++;
    contador%=2;
    Image1->Cursor=crSizeAll;
    UltX=X;
    UltY=Y;
  }
  else */
  {
    int r, g, b;
    MostraInf(X, Y, r, g, b);
  }
}
//---------------------------------------------------------------------------

void __fastcall LumTresh(_Bitmap *ImgSrc, _Bitmap *ImgDest, int Tresh,
                    bool MarcaCorte, bool Original, int IndiceCor)
{
  int x, y;
  byte pixel;
  Cor **pcImgSrc;
  pcImgSrc=MatrizCor(ImgSrc);       
  Cor **pcImgDest=MatrizCor(ImgDest);
  for (y=0; y<ImgSrc->Height; y++)
    for (x=0; x<ImgSrc->Width; x++)
    {
      switch (IndiceCor)
      {
        case 0: pixel=pcImgSrc[y][x].Azul;
          break;
        case 1: pixel=pcImgSrc[y][x].Verde;
          break;
        case 2: pixel=pcImgSrc[y][x].Vermelho;
          break;
      }

      if (pixel<Tresh)
      {
        if (Original)
          pcImgDest[y][x]=pcImgSrc[y][x];
        else
        {
          pcImgDest[y][x].Azul=0;
          pcImgDest[y][x].Verde=0;
          pcImgDest[y][x].Vermelho=0;
        }
      }
      else
      if (pixel==Tresh)
        if (MarcaCorte)
        {
          pcImgDest[y][x].Azul=0x0;
          pcImgDest[y][x].Verde=0x0;
          pcImgDest[y][x].Vermelho=0xFF;
        }
        else
          if (!Original)
          {
            pcImgDest[y][x].Azul=0;
            pcImgDest[y][x].Verde=0;
            pcImgDest[y][x].Vermelho=0;
          }
          else
            pcImgDest[y][x]=pcImgSrc[y][x];
      else
      {
        pcImgDest[y][x].Azul=0xFF;
        pcImgDest[y][x].Verde=0xFF;
        pcImgDest[y][x].Vermelho=0xFF;
      }
    }
  delete [] pcImgSrc;
  delete [] pcImgDest;
}
//---------------------------------------------------------------------------

void __fastcall TZoom::Image2MouseDown(TObject *Sender,
      TMouseButton Button, TShiftState Shift, int X, int Y)
{
  ScrollBox1->HorzScrollBar->Position=X*FatorAumento*FatorPropX-LARG_MOSTRA;
  ScrollBox1->VertScrollBar->Position=Y*FatorAumento*FatorPropY-ALT_MOSTRA;
}
//---------------------------------------------------------------------------

void __fastcall TZoom::Image1MouseUp(TObject *Sender, TMouseButton Button,
      TShiftState Shift, int X, int Y)
{
  pressionado=false;
  Image1->Cursor=crDefault;
}
//---------------------------------------------------------------------------

void __fastcall TZoom::FormMouseMove(TObject *Sender, TShiftState Shift,
      int X, int Y)
{
  /*if (pressionado)
  {
    ScrollBox1->HorzScrollBar->Position=UltXPos+(UltX-X);
    ScrollBox1->VertScrollBar->Position=UltYPos+(UltY-Y);
    Edit1->Text=ScrollBox1->HorzScrollBar->Position;
    Edit2->Text=ScrollBox1->VertScrollBar->Position;
    Status(IntToStr(UltXPos)+"  "+IntToStr(UltX)+"  "+IntToStr(X));
    Image1->Cursor=crSizeAll;
  }*/
}
//---------------------------------------------------------------------------


void __fastcall TZoom::CoresChange(TObject *Sender)
{
  TColor cor=TColor(sbAzul->Position*0x10000+sbVerde->Position*0x100+sbVermelho->Position);
  pnCor->Color=cor;
  lbAzul->Caption="Azul: "+IntToStr(sbAzul->Position);
  lbVerde->Caption="Verde: "+IntToStr(sbVerde->Position);
  lbVermelho->Caption="Vermelho: "+IntToStr(sbVermelho->Position);
}
//---------------------------------------------------------------------------

void __fastcall TZoom::btImgOrigClick(TObject *Sender)
{
  Image1->Picture->Bitmap->Assign(BitTemp);  
}
//---------------------------------------------------------------------------

void __fastcall TZoom::cbStretchClick(TObject *Sender)
{
  Image2->Stretch=cbStretch->Checked;
  if (cbStretch->Checked)
    AjustaTamanhoFoto();
}
//---------------------------------------------------------------------------

void __fastcall TZoom::MostraInf(int X, int Y, int &r, int &g, int &b)
{
  int pixel, x, y, lum;
  float br, gr;
  x=X/(sbFator->Position/10);
  y=Y/(sbFator->Position/10);
  pixel=Image1->Picture->Bitmap->Canvas->Pixels[x][y];
  r=pixel & 0xFF;
  g=(pixel & 0xFF00) >> 0x8;
  b=pixel >> 0x10;
  lum=Round(r*0.3+g*0.59+b*0.11);
  if (r>0)
  {
    br=b*1.0/r;
    gr=g*1.0/r;
  }
  else
  {
    br=1;
    gr=1;
  }
  edX->Text=IntToStr(x);
  edY->Text=IntToStr(y);
  edX2->Text=IntToStr(0xFF-x);
  edBR->Text=FormatFloat("##0.00", br);
  edGR->Text=FormatFloat("##0.00", gr);
  edLum->Text=IntToStr(lum);
  edAzul->Text=IntToStr(b);
  edVerde->Text=IntToStr(g);
  edVermelho->Text=IntToStr(r);

  UltX=X;
  UltY=Y;
  if (cbMostraCores->Checked)
  {
    MostraCores(b, g, r);
  }
}
//---------------------------------------------------------------------------

void __fastcall TZoom::AjustaTamanhoFoto()
{
  float FatorBM=Image2->Picture->Bitmap->Width*1.0/Image2->Picture->Bitmap->Height;
  float FatorP=320.0/240;
  if (FatorBM>FatorP)
    Image2->Height=(320.0/Image2->Picture->Bitmap->Width)*Image2->Picture->Bitmap->Height;
  else
    Image2->Width=(240.0/Image2->Picture->Bitmap->Height)*Image2->Picture->Bitmap->Width;
  FatorPropX=Image2->Picture->Bitmap->Width*1.0/Image2->Width;
  FatorPropY=Image2->Picture->Bitmap->Height*1.0/Image2->Height;
}
//---------------------------------------------------------------------------

void __fastcall TZoom::btTamanhoTelaClick(TObject *Sender)
{
  if (btTamanhoTela->Caption=="Aumentar")
  {
    Panel1->Height=42;
    btTamanhoTela->Caption="Diminuir";
  }
  else
  {
    Panel1->Height=257;
    btTamanhoTela->Caption="Aumentar";
  }
}
//---------------------------------------------------------------------------

void __fastcall TZoom::tbOffsetSigChange(TObject *Sender)
{
  ImgSig(BitTemp, Image1->Picture->Bitmap, tbOffsetSig->Position, tbEsticaSig->Position/1000.0);
  Image1->Repaint();
  edFundoCor->Repaint();
}
//---------------------------------------------------------------------------

void __fastcall ImgSig(_Bitmap *ImgSrc, _Bitmap *ImgDest, int offset, float estica)
{
  int n, x, y;
  Cor **cImgSrc, **cImgDest;
  byte vetor[255]={0};
  for (n=0; n<255; n++)
  {
    vetor[n]=1/(1+exp(-(n-offset)*estica))*255;
  }
  CBitmap *BImgSrc, *BImgDest;
  BImgSrc=new CBitmap(ImgSrc);
  BImgDest=new CBitmap(ImgDest);
  cImgSrc=BImgSrc->PMCor;
  cImgDest=BImgDest->PMCor;
  for (y=0; y<BImgSrc->Alt; y++)
    for (x=0; x<BImgSrc->Larg; x++)
    {
      cImgDest[y][x].Azul=vetor[cImgSrc[y][x].Azul];  
      cImgDest[y][x].Verde=vetor[cImgSrc[y][x].Verde];
      cImgDest[y][x].Vermelho=vetor[cImgSrc[y][x].Vermelho];
    }
  delete BImgSrc;
  delete BImgDest;
}
//---------------------------------------------------------------------------
void __fastcall TZoom::tbEsticaSigChange(TObject *Sender)
{
  ImgSig(BitTemp, Image1->Picture->Bitmap, tbOffsetSig->Position, tbEsticaSig->Position/1000.0);
  Image1->Repaint();
  edFundoCor->Repaint();  
}
//---------------------------------------------------------------------------

