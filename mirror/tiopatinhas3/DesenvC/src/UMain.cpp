//---------------------------------------------------------------------------
#include <vcl.h>
#include <stdlib.h>
#include <math.h>
#pragma hdrstop

#include "UMain.h"
//---------------------------------------------------------------------------
#pragma package(smart_init)
#pragma resource "*.dfm"
TMain *Main;

#ifdef DEBUG
  TStrings *Log;
#endif

//---------------------------------------------------------------------------
__fastcall TMain::TMain(TComponent* Owner)
  : TForm(Owner) 
{
}
//---------------------------------------------------------------------------

void __fastcall TMain::btAtualizaClick(TObject *Sender)
{
  imgProcessada->Picture->Assign(imgTemp->Picture);
}
//---------------------------------------------------------------------------

void __fastcall TMain::FormCreate(TObject *Sender)
{                    
  #ifdef DEBUG
    Log=mmLog->Lines;
  #endif
  CriaComponentes();
  CarregaParametros();
  AcertaEstiloComponentes();
  PreparaContadorTempo();
  WindowState=wsMaximized;
}
//---------------------------------------------------------------------------

void TMain::CriaComponentes()
{
  int contador, top, deslocamento_horizontal;
  TLabel *LabelTemp;
  deslocamento_horizontal=0;

  #define LEFT_EDIT 104
  #define LEFT_LABEL 8
  #define TOP_OFFSET 8
  #define TOP_MULT 24
  #define DESLOCAMENTO_HORIZONTAL 164
  #define LARGURA_EDIT 49
  #define INCREMENTO_LABEL 4

  contador=0;
  #define X(a, b) top=TOP_OFFSET+TOP_MULT*contador++;  \
                  edMLT##a=new TEdit(tsTioPatinhas);  \
                  edMLT##a->Parent=tsTioPatinhas;     \
                  edMLT##a->Width=LARGURA_EDIT; \
                  edMLT##a->Left=LEFT_EDIT+deslocamento_horizontal;            \
                  edMLT##a->Top=top;  \
                  edMLT##a->Font->Name="Verdana";                      \
                  edMLT##a->Font->Style=TFontStyles()<<fsBold;             \
                  edMLT##a->Font->Color=clLime;                          \
                  edMLT##a->Color=clBlack;                              \
                  LabelTemp=new TLabel(tsTioPatinhas);  \
                  LabelTemp->Caption=(AnsiString)#a; \
                  LabelTemp->Parent=tsTioPatinhas;     \
                  LabelTemp->Left=LEFT_LABEL+deslocamento_horizontal;            \
                  LabelTemp->Top=top+INCREMENTO_LABEL;  \
                  
    PARAMETROS_MLT
  #undef X 
  deslocamento_horizontal+=DESLOCAMENTO_HORIZONTAL;

  contador=0;
  #define X(a, b) top=TOP_OFFSET+TOP_MULT*contador++;  \
                  edABT##a=new TEdit(tsTioPatinhas);  \
                  edABT##a->Parent=tsTioPatinhas;     \
                  edABT##a->Width=LARGURA_EDIT; \
                  edABT##a->Left=LEFT_EDIT+deslocamento_horizontal;            \
                  edABT##a->Top=top;  \
                  edABT##a->Font->Name="Verdana";                      \
                  edABT##a->Font->Style=TFontStyles()<<fsBold;             \
                  edABT##a->Font->Color=clLime;                          \
                  edABT##a->Color=clBlack;                              \
                  LabelTemp=new TLabel(tsTioPatinhas);  \
                  LabelTemp->Caption=(AnsiString)#a; \
                  LabelTemp->Parent=tsTioPatinhas;     \
                  LabelTemp->Left=LEFT_LABEL+deslocamento_horizontal;            \
                  LabelTemp->Top=top+INCREMENTO_LABEL;  \
                  
    PARAMETROS_ABT
  #undef X 
  deslocamento_horizontal+=DESLOCAMENTO_HORIZONTAL;

  contador=0;
  #define X(a, b) top=TOP_OFFSET+TOP_MULT*contador++;  \
                  edAI##a=new TEdit(tsTioPatinhas);  \
                  edAI##a->Parent=tsTioPatinhas;     \
                  edAI##a->Width=LARGURA_EDIT; \
                  edAI##a->Left=LEFT_EDIT+deslocamento_horizontal+56;            \
                  edAI##a->Top=top;  \
                  edAI##a->Font->Name="Verdana";                      \
                  edAI##a->Font->Style=TFontStyles()<<fsBold;             \
                  edAI##a->Font->Color=clLime;                          \
                  edAI##a->Color=clBlack;                              \
                  LabelTemp=new TLabel(tsTioPatinhas);  \
                  LabelTemp->Caption=(AnsiString)#a; \
                  LabelTemp->Parent=tsTioPatinhas;     \
                  LabelTemp->Left=LEFT_LABEL+deslocamento_horizontal;            \
                  LabelTemp->Top=top+INCREMENTO_LABEL;  \
                  
    PARAMETROS_AI
  #undef X 
  deslocamento_horizontal+=DESLOCAMENTO_HORIZONTAL;
}
//---------------------------------------------------------------------------

void TMain::CarregaParametros()
{
  AnsiString temp;
  TIniFile *Params;
  AnsiString FilePath=ExtractFilePath(Application->ExeName)+"\\ParamsTP.ini";
  if (FileExists(FilePath))
  {
    Params=new TIniFile(FilePath);
    #define X(a, b) temp="a";             \
                    if ((AnsiString)#b==(AnsiString)"Int")                                                          \
                      temp=Params->ReadString("Geral", #a, 0);                    \
                    else if ((AnsiString)#b==(AnsiString)"Double")                                                     \
                      temp=FloatToStr(Params->ReadInteger("Geral", #a, 0)/1000.0);   \
                    edMLT##a->Text=temp;

      PARAMETROS_MLT
    #undef X      ;

    #define X(a, b) temp="a";             \
                    if ((AnsiString)#b==(AnsiString)"Int")                                                          \
                      temp=Params->ReadString("Geral", #a, 0);                    \
                    else if ((AnsiString)#b==(AnsiString)"Double")                                                     \
                      temp=FloatToStr(Params->ReadInteger("Geral", #a, 0)/1000.0);   \
                    edABT##a->Text=temp;

      PARAMETROS_ABT
    #undef X;

    #define X(a, b) temp="a";             \
                    if ((AnsiString)#b==(AnsiString)"Int")                                                          \
                      temp=Params->ReadString("Geral", #a, 0);                    \
                    else if ((AnsiString)#b==(AnsiString)"Double")                                                     \
                      temp=FloatToStr(Params->ReadInteger("Geral", #a, 0)/1000.0);   \
                    edAI##a->Text=temp;

      PARAMETROS_AI
    #undef X
  }
  delete Params;
}     
//---------------------------------------------------------------------------

void TMain::PreparaContadorTempo()
{
  __int64 freq;
  QueryPerformanceFrequency((LARGE_INTEGER *)&freq);
  PeriodoContador=1000.0/freq;
}     
//---------------------------------------------------------------------------

void TMain::AcertaEstiloComponentes()
{
  int NumComponentes;
  TComponent *Temp;
  TEdit *edTemp;
  NumComponentes=Main->ComponentCount;
  for (int n=0; n<NumComponentes; n++)
  {
    Temp=Main->Components[n];
    edTemp=dynamic_cast<TEdit *>(Temp);
    if (edTemp!=NULL)
    {
      edTemp->Font->Name="Verdana";
      edTemp->Font->Style=TFontStyles()<<fsBold;
      edTemp->Font->Color=clLime;
      edTemp->Color=clBlack;
    }
  }
}     
//---------------------------------------------------------------------------

void __fastcall TMain::Status(AnsiString Texto)
{
  StatusBar1->Panels->Items[0]->Text=Texto;
  StatusBar1->Repaint();
}
//---------------------------------------------------------------------------

void __fastcall TMain::Zoom2(_Bitmap *Bitmap)
{
  TZoom *Zoom;
  Zoom=new TZoom(Main);
  Zoom->CarregaBitmap(Bitmap);
  Zoom->Dimensiona(10);
  Zoom->WindowState=wsMaximized;
  Zoom->ShowModal();
  delete Zoom;
}
//---------------------------------------------------------------------------

void __fastcall TMain::ZoomImagem(TObject *Sender)
{
  Zoom2((static_cast<TImage *>(Sender))->Picture->Bitmap);
}
//---------------------------------------------------------------------------

void __fastcall TMain::btAtualizaOriginalClick(TObject *Sender)
{
  imgOriginal->Picture->Bitmap->Assign(imgTemp->Picture->Bitmap);  
}
//---------------------------------------------------------------------------

void __fastcall TMain::Status(AnsiString Texto, int Indice)
{
  StatusBar1->Panels->Items[Indice]->Text=Texto;
  StatusBar1->Repaint();
}
//---------------------------------------------------------------------------

void __fastcall TMain::btMedia2Click(TObject *Sender)
{
  __int64 comeco, fim;
  CTonsCinza *TCImgSrc, *TCImgDest;
  TCImgSrc=new CTonsCinza(imgProcessada);
  TCImgDest=new CTonsCinza(imgProcessada->Picture->Bitmap->Width, imgProcessada->Picture->Bitmap->Height);
  QueryPerformanceCounter((LARGE_INTEGER *)&comeco);
  Media2(TCImgSrc, TCImgDest);
  QueryPerformanceCounter((LARGE_INTEGER *)&fim);
  TCImgDest->ExportaBitmap(imgTemp, false);
  Status("Média2: "+FormatFloat("###,##0.00", (fim-comeco)*PeriodoContador)+
          " milisegundos");
  imgTemp->Repaint();
  delete TCImgSrc;
  delete TCImgDest;
}
//---------------------------------------------------------------------------

void TMain::CarregaParamsReconheceCedula(TParamsRC &ParamsRC)
{
  #define X(a, b) ParamsRC.ParamsMLT.##a=edMLT##a->Text.To##b();
    PARAMETROS_MLT
  #undef X
     
  #define X(a, b) ParamsRC.ParamsABT.##a=edABT##a->Text.To##b();
    PARAMETROS_ABT
  #undef X
     
  #define X(a, b) ParamsRC.ParamsAI.##a=edAI##a->Text.To##b();
    PARAMETROS_AI
  #undef X
}
//---------------------------------------------------------------------------

void __fastcall TMain::btReconheceCedulaClick(TObject *Sender)
{
  __int64 comeco, fim;
  #ifdef DEBUG
    Log->Clear();
  #endif
  TParamsRC ParamsRC;
  ParamsRC.ParamsMLT.TCImgSrc=new CTonsCinza(imgTemp);
  ParamsRC.ParamsMLT.BImgDest=new CBitmap(imgProcessada);
  CarregaParamsReconheceCedula(ParamsRC);
  QueryPerformanceCounter((LARGE_INTEGER *)&comeco);
  ReconheceCedula(ParamsRC);
  QueryPerformanceCounter((LARGE_INTEGER *)&fim);
  imgProcessada->Repaint();
  delete ParamsRC.ParamsMLT.TCImgSrc;
  delete ParamsRC.ParamsMLT.BImgDest;
  if (ParamsRC.ParamsABT.AchouTarja)
  {
    Status("Reconhece Cédula: "+FormatFloat("###,##0.00", (fim-comeco)*PeriodoContador)+
          " milisegundos, Cédula: R$ "+IntToStr(ParamsRC.ParamsAI.ValorCedula));
  }
  else
    Status("Não achou tarja");
}
//---------------------------------------------------------------------------

void __fastcall TMain::flb1Click(TObject *Sender)
{
  _Bitmap *Bitmap=new _Bitmap;
  Bitmap->LoadFromFile(flb1->FileName);
  imgOriginal->Picture->Bitmap->Assign(Bitmap);
  imgProcessada->Picture->Bitmap->Assign(Bitmap);
  imgTemp->Picture->Bitmap->Assign(Bitmap);
  delete Bitmap;
}
//---------------------------------------------------------------------------

