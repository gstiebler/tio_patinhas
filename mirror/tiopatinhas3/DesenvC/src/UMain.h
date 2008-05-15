//---------------------------------------------------------------------------
#ifndef UMainH
#define UMainH
//---------------------------------------------------------------------------
#include <Classes.hpp>
#include <Controls.hpp>
#include <StdCtrls.hpp>
#include <Forms.hpp>
#include <ExtCtrls.hpp>
#include <FileCtrl.hpp>
#include <ComCtrls.hpp>
#include <inifiles.hpp>
#include <Mask.hpp>
#include <DB.hpp>
#include <DBTables.hpp>
#include <DBGrids.hpp>
#include <Grids.hpp>
#include "UTioPatinhas.h"
#include "UZoom.h"
#include "UAuxTioPatinhas.h"

//---------------------------------------------------------------------------

class TMain : public TForm
{
__published:	// IDE-managed Components
  TDirectoryListBox *dlb1;
  TDriveComboBox *dcb1;
  TFileListBox *flb1;
  TPanel *Panel;
  TImage *imgProcessada;
  TPanel *Panel1;
  TImage *imgOriginal;
  TPanel *Panel2;
  TImage *imgTemp;
  TStatusBar *StatusBar1;
  TPageControl *PageControl1;
  TTabSheet *tsTioPatinhas;
  TTabSheet *tsLog;
  TMemo *mmLog;
  TButton *btReconheceCedula;
  void __fastcall btAtualizaClick(TObject *Sender);
  void __fastcall FormCreate(TObject *Sender);
  void __fastcall ZoomImagem(TObject *Sender);
  void __fastcall btAtualizaOriginalClick(TObject *Sender);
  void __fastcall btMedia2Click(TObject *Sender);
  void __fastcall btReconheceCedulaClick(TObject *Sender);
  void __fastcall flb1Click(TObject *Sender);
private:
  double PeriodoContador;
  void __fastcall Zoom2(_Bitmap *Bitmap);
  void __fastcall Status(AnsiString Texto);
  void __fastcall Status(AnsiString Texto, int Indice);
  void AcertaEstiloComponentes();
  void PreparaContadorTempo();
  void CriaComponentes();
  void CarregaParametros();
  #define X(a, b) TEdit *edMLT##a;
    PARAMETROS_MLT
  #undef X
  #define X(a, b) TEdit *edABT##a;
    PARAMETROS_ABT
  #undef X
  #define X(a, b) TEdit *edAI##a;
    PARAMETROS_AI
  #undef X
  //---------------------------------------------------------------------------
  void CarregaParamsReconheceCedula(TParamsRC &ParamsRC);
public:		// User declarations
  __fastcall TMain(TComponent* Owner);
};

//---------------------------------------------------------------------------
extern PACKAGE TMain *Main;
//---------------------------------------------------------------------------
#endif


//TEMPLATE--------------------------------------------------------------------------------------------
//  __int64 comeco, fim;
//  CTonsCinza *TCImgSrc, *TCImgDest;
//  TCImgSrc=new CTonsCinza(imgProcessada);
//  TCImgDest=new CTonsCinza(imgProcessada->Picture->Bitmap->Width, imgProcessada->Picture->Bitmap->Height);
//  QueryPerformanceCounter((LARGE_INTEGER *)&comeco);
//  LarguraLinha(TCImgSrc, TCImgDest);
//  QueryPerformanceCounter((LARGE_INTEGER *)&fim);
//  TCImgDest->ExportaBitmap(imgTemp, true);
//  Status("Larg Max Linha: "+FormatFloat("###,##0.00", (fim-comeco)*PeriodoContador));
//  imgTemp->Repaint();
//  delete TCImgSrc;
//  delete TCImgDest;
