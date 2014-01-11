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
  dlb1->Directory=CaminhoExecutavel()+"..\\..\\Imagens\\Erradas";
  AbriuCaptura=false;
  TIniFile *IniFile=new TIniFile("..\\..\\ParamsDir.ini");
  CaminhoSelecionadas=IniFile->ReadString("Geral", "DiretorioSelecionadas", "");
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
  AnsiString FilePath=CaminhoExecutavel()+"\\ParamsTP.ini";
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
  ParamsRC.ConverteParametrosDependentesLargura();
}
//---------------------------------------------------------------------------

void __fastcall TMain::btReconheceCedulaClick(TObject *Sender)
{
  ReconheceCedulaForm();
}
//---------------------------------------------------------------------------


void __fastcall TMain::btIniciarCapturaClick(TObject *Sender)
{
  if (!AbriuCaptura)
  {
    AbriuCaptura = IniciaCaptura(pnlCaptura->Handle, edPlaca->Text.ToInt(), edCanal->Text.ToInt(), LARG, ALT);
    if (AbriuCaptura)
      Status("Abriu Captura");
    else
      Status("erro ao abrir a captura");
  }
}
//---------------------------------------------------------------------------

void __fastcall TMain::btCapturarClick(TObject *Sender)
{
  _Bitmap *Bitmap;
  Bitmap=CapturaBitmap();
  if (Bitmap)
  {                  
    if (cbSalvarAoCapturar)
      Bitmap->SaveToFile(CaminhoExecutavel()+"..\\..\\dinheiro\\Capturadas\\"+IntToStr(GetTickCount())+".bmp");
    imgTemp->Picture->Bitmap->Assign(Bitmap);
    imgProcessada->Picture->Bitmap->Assign(Bitmap);
    ReconheceCedulaForm();
  }
  delete Bitmap;
}
//---------------------------------------------------------------------------

void TMain::TocaSom(int ValorCedula)
{
  MediaPlayer->FileName=CaminhoExecutavel()+FormatFloat("000", ValorCedula)+".wav";
  MediaPlayer->Open();
  MediaPlayer->Play();
}     
//---------------------------------------------------------------------------

void __fastcall TMain::btSalvarClick(TObject *Sender)
{
  AnsiString FileName=CaminhoExecutavel()+IntToStr(GetTickCount())+".bmp";
  imgTemp->Picture->Bitmap->SaveToFile(FileName);
}
//---------------------------------------------------------------------------

_Bitmap * TMain::CapturaBitmap()
{
  _Bitmap *Bitmap=new _Bitmap;
  int y;
  int ImageSize, SizeCaptured;
  byte *vetor, *v2;
  Bitmap->Width=LARG;
  Bitmap->Height=ALT;
  SizeCaptured=Captura(&vetor);
  ImageSize=sizeof(BITMAPINFOHEADER)+LARG*ALT*4;
  Bitmap->PixelFormat=pf32bit;
  v2=vetor+sizeof(BITMAPINFOHEADER);
  Status(IntToStr(SizeCaptured)+" - "+IntToStr(ImageSize));  
  if (SizeCaptured>=ImageSize)
  {
    for (y=ALT-1; y>=0; y--)
      memcpy(Bitmap->ScanLine[ALT-1-y], &v2[y*(LARG*4+128)], LARG*4);
    return Bitmap;
  }
  else
  {
    delete Bitmap;     
    Status("Erro ao capturar "+IntToStr(SizeCaptured)+" - "+IntToStr(ImageSize));
    return NULL;
  }
}     
//---------------------------------------------------------------------------

void TMain::ReconheceCedulaForm()
{
  __int64 comeco, fim;
  #ifdef DEBUG
    Log->Clear();
  #endif                                                 
  TParamsRC ParamsRC;
  imgProcessada->Picture->Bitmap->Assign(imgTemp->Picture->Bitmap);
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
    if (cbTocaSom->Checked)
      TocaSom(ParamsRC.ParamsAI.ValorCedula);
  }
  else
    Status("Não achou tarja");
}     
//---------------------------------------------------------------------------

AnsiString CaminhoExecutavel()
{
  return ExtractFilePath(Application->ExeName);
}     
//---------------------------------------------------------------------------

void __fastcall TMain::btHistogramaClick(TObject *Sender)
{
  __int64 comeco, fim;
  int Mediana;
  CTonsCinza *TCImgSrc;
  TCImgSrc=new CTonsCinza(imgProcessada);
  QueryPerformanceCounter((LARGE_INTEGER *)&comeco);
  Histograma(TCImgSrc, imgHistograma->Picture->Bitmap, imgHistCumulativa->Picture->Bitmap, Mediana);  
  QueryPerformanceCounter((LARGE_INTEGER *)&fim);
  Status("Função histograma "+FormatFloat("###,##0.00", (fim-comeco)*PeriodoContador)+
        " milisegundos, Mediana: "+IntToStr(Mediana));
  delete TCImgSrc;
}
//---------------------------------------------------------------------------

void __fastcall TMain::btReconheceProximaClick(TObject *Sender)
{
  flb1->ItemIndex++;
  CarregaImagens();
  ReconheceCedulaForm();
}
//---------------------------------------------------------------------------

void TMain::CarregaImagens()
{
  _Bitmap *Bitmap=new _Bitmap;
  AnsiString CaminhoImagemOriginal;
  AnsiString NomeArq=flb1->FileName;
  Bitmap->LoadFromFile(NomeArq);
  imgProcessada->Picture->Bitmap->Assign(Bitmap);
  imgTemp->Picture->Bitmap->Assign(Bitmap);
  AnsiString caminho_txt=NomeArq.SubString(1, NomeArq.Length()-4)+".txt";
  if (FileExists(caminho_txt))
    mmLog->Lines->LoadFromFile(caminho_txt);
  //AnsiString diretorio=ExtractFilePath(NomeArq);
  AnsiString Arquivo=ExtractFileName(NomeArq);
  int pos=Arquivo.LastDelimiter("_");

  //pega valor da nota
  AnsiString nota;
  int pos2=Arquivo.AnsiPos("_");
  nota=Arquivo.SubString(1, pos2-1);

  Arquivo=Arquivo.SubString(pos+1, Arquivo.Length()-pos);
  CaminhoImagemOriginal=CaminhoSelecionadas+nota+"\\"+Arquivo;
  edCaminho->Text=CaminhoImagemOriginal;
  _Bitmap *BitOriginal=new _Bitmap;
  BitOriginal->LoadFromFile(CaminhoImagemOriginal);
  imgTemp->Picture->Bitmap->Assign(BitOriginal);
  delete Bitmap;
}     
//---------------------------------------------------------------------------

void __fastcall TMain::flb1Click(TObject *Sender)
{
  CarregaImagens();  
}
//---------------------------------------------------------------------------

void __fastcall TMain::Button1Click(TObject *Sender)
{
  imgProcessada->Picture->Bitmap->SaveToFile("c:\\tiopatinhas.bmp");  
}
//---------------------------------------------------------------------------

