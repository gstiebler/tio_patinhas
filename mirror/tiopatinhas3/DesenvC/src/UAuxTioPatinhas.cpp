//---------------------------------------------------------------------------
#pragma hdrstop
#include "UAuxTioPatinhas.h"
//---------------------------------------------------------------------------
#pragma package(smart_init)

TBordasColunas::TBordasColunas(int numColunas)
{
  NumColunas=numColunas;
  Bordas=new TVectorBorda * [NumColunas];
  for (int n=0; n<NumColunas; n++)
    Bordas[n]=new TVectorBorda;
}
//---------------------------------------------------------------------------

TBordasColunas::~TBordasColunas()
{
  int n;
  for (n=0; n<NumColunas; n++)
    delete Bordas[n];
  delete [] Bordas;
}
//---------------------------------------------------------------------------

TParamsMLT::~TParamsMLT()
{
  delete BordasColunas;
}
//---------------------------------------------------------------------------

void TMeioBordas::Inicializa(int y1, int y2)
{
  Y1=y1;
  Y2=y2;
  yMeio=(Y1+Y2)/2;
  Altura=Y2-Y1;
}
//---------------------------------------------------------------------------

TConjuntoMeioBordas::TConjuntoMeioBordas(int numColunas)
{
  NumColunas=numColunas;
  VectorMeioBordas=new TVectorMeioBordas * [NumColunas];
  for (int n=0; n<NumColunas; n++)
    VectorMeioBordas[n]=new TVectorMeioBordas;
}
//---------------------------------------------------------------------------

TConjuntoMeioBordas::~TConjuntoMeioBordas()
{
  int n;
  for (n=0; n<NumColunas; n++)
    delete VectorMeioBordas[n];
  delete [] VectorMeioBordas;
}
//---------------------------------------------------------------------------

bool TTarja::Ativa(int x)
{
  return (uint)(x-X)==VetorAlturas.size();
}
//---------------------------------------------------------------------------

TParamsABT::~TParamsABT()
{
  delete ConjuntoMeioBordas;
}
//---------------------------------------------------------------------------

void Media2(CTonsCinza *TCImgSrc, CTonsCinza *TCImgDest)
{
  int x, y, soma;
  byte **tcImgSrc=TCImgSrc->TonsCinza;
  byte **tcImgDest=TCImgDest->TonsCinza;
  for (y=0; y<(TCImgSrc->Alt-1); y+=2)
  {
    for (x=0; x<TCImgSrc->Larg; x++)
    {
      soma=tcImgSrc[y][x]+tcImgSrc[y+1][x];
      soma=soma/2;
      tcImgDest[y][x]=(byte)soma;
      tcImgDest[y+1][x]=(byte)soma;
    }
  }
}   
//---------------------------------------------------------------------------
