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

CMatrizInteiro::CMatrizInteiro(int larg, int alt)
{
  int y;
  Larg=larg;
  Alt=alt;
  Matriz=new int * [Alt];
  for (y=0; y<alt; y++)
    Matriz[y]=new int [Larg];
}
//---------------------------------------------------------------------------

CMatrizInteiro::~CMatrizInteiro()
{
  int y;
  for (y=0; y<Alt; y++)
    delete [] Matriz[y];
  delete [] Matriz;
}
//---------------------------------------------------------------------------     

void TParamsRC::ConverteParametrosDependentesLumMediana()
{
  ParamsAI.DifMinMediaFaixaRef=Round((ParamsAI.DifMinMediaFaixaRef/1000.0)*LumMedianaImagem);
}     
//---------------------------------------------------------------------------

void TParamsRC::ConverteParametrosDependentesLargura()
{
  ParamsABT.AltMinTarja=Round((ParamsABT.AltMinTarja/1000.0)*ParamsMLT.TCImgSrc->Larg);
  ParamsABT.AltMaxTarja=Round((ParamsABT.AltMaxTarja/1000.0)*ParamsMLT.TCImgSrc->Larg);  
  ParamsABT.DistMaxTarjas=Round((ParamsABT.DistMaxTarjas/1000.0)*ParamsMLT.TCImgSrc->Larg);  
  ParamsABT.LargMinTarja=Round((ParamsABT.LargMinTarja/1000.0)*ParamsMLT.TCImgSrc->Larg);
  ParamsABT.LargMaxTarja=Round((ParamsABT.LargMaxTarja/1000.0)*ParamsMLT.TCImgSrc->Larg);
}     
//---------------------------------------------------------------------------         

void TParamsRC::ConverteParametrosDependentesAlturaFaixa()
{
  ParamsAI.DistFaixaRef=Round((ParamsAI.DistFaixaRef/1000.0)*ParamsABT.MediaAlturaTarja); 
  ParamsAI.LargFaixaRef=Round((ParamsAI.LargFaixaRef/1000.0)*ParamsABT.MediaAlturaTarja);
  ParamsAI.LargIdentificador=Round((ParamsAI.LargIdentificador/1000.0)*ParamsABT.MediaAlturaTarja);
  ParamsAI.AltIdentificador=Round((ParamsAI.AltIdentificador/1000.0)*ParamsABT.MediaAlturaTarja);  
  ParamsAI.XIniParaRefTarja=Round((ParamsAI.XIniParaRefTarja/1000.0)*ParamsABT.MediaAlturaTarja);   
  ParamsAI.YIniParaRefTarja=Round((ParamsAI.YIniParaRefTarja/1000.0)*ParamsABT.MediaAlturaTarja);   
  ParamsAI.LimiarAlturaIdentificador=Round((ParamsAI.LimiarAlturaIdentificador/1000.0)*ParamsABT.MediaAlturaTarja);
  ParamsAI.MaiorDistSemPixelsIdentificador=Round((ParamsAI.MaiorDistSemPixelsIdentificador/1000.0)*ParamsABT.MediaAlturaTarja);
  ParamsAI.NumMinPixelsIdentificador=Round((ParamsAI.NumMinPixelsIdentificador/1000.0)*ParamsABT.MediaAlturaTarja);
  ParamsAI.AltMinGrupoConexoIdentificador=Round((ParamsAI.AltMinGrupoConexoIdentificador/1000.0)*ParamsABT.MediaAlturaTarja); 
  ParamsAI.DifMinEmbGrupoEmbRegiaoIdentificador=Round((ParamsAI.DifMinEmbGrupoEmbRegiaoIdentificador/1000.0)*ParamsABT.MediaAlturaTarja);
}     
//---------------------------------------------------------------------------
