//---------------------------------------------------------------------------

#ifndef UZoomH
#define UZoomH
//---------------------------------------------------------------------------
#include <Classes.hpp>
#include <Controls.hpp>
#include <StdCtrls.hpp>
#include <Forms.hpp>
#include <ExtCtrls.hpp>
#include <ComCtrls.hpp>
#define LARG_MOSTRA 500
#define ALT_MOSTRA 215
#include "UCBitmap.h"
#include "UGeral.h"
//---------------------------------------------------------------------------
class TZoom : public TForm
{
__published:	// IDE-managed Components
  TScrollBox *ScrollBox1;
  TImage *Image1;
  TPanel *Panel1;
  TScrollBar *sbFator;
  TStatusBar *StatusBar1;
  TImage *Image2;
  TLabel *Label1;
  TLabel *Label2;
  TLabel *Label3;
  TLabel *Label4;
  TEdit *edX;
  TEdit *edY;
  TEdit *edX2;
  TEdit *edBR;
  TEdit *edGR;
  TEdit *edLum;
  TLabel *Label5;
  TLabel *Label6;
  TLabel *Label7;
  TLabel *Label8;
  TLabel *Label9;
  TEdit *edAzul;
  TEdit *edVerde;
  TEdit *edVermelho;
  TPanel *pnOriginal;
  TImage *imCores;
  TEdit *edFundoCor;
  TTrackBar *tbFundoCor;
  TCheckBox *cbMarcaCorte;
  TCheckBox *cbOriginal;
  TCheckBox *cbMostraCores;
  TRadioGroup *rgCor;
  TGroupBox *GroupBox1;
  TLabel *lbAzul;
  TLabel *lbVerde;
  TLabel *lbVermelho;
  TScrollBar *sbAzul;
  TScrollBar *sbVerde;
  TScrollBar *sbVermelho;
  TPanel *pnCor;
  TButton *btImgOrig;
  TPanel *pnParams;
  TScrollBox *sbParams;
  TCheckBox *cbStretch;
  TButton *btTamanhoTela;
  TTrackBar *tbOffsetSig;
  TTrackBar *tbEsticaSig;
  void __fastcall sbFatorChange(TObject *Sender);
  void __fastcall Image1MouseDown(TObject *Sender, TMouseButton Button,
          TShiftState Shift, int X, int Y);
  void __fastcall tbFundoCorChange(TObject *Sender);
  void __fastcall FormCreate(TObject *Sender);
  void __fastcall FormDestroy(TObject *Sender);
  void __fastcall Image1MouseMove(TObject *Sender, TShiftState Shift,
          int X, int Y);
  void __fastcall Image2MouseDown(TObject *Sender, TMouseButton Button,
          TShiftState Shift, int X, int Y);
  void __fastcall Image1MouseUp(TObject *Sender, TMouseButton Button,
          TShiftState Shift, int X, int Y);
  void __fastcall FormMouseMove(TObject *Sender, TShiftState Shift, int X,
          int Y);
  void __fastcall CoresChange(TObject *Sender);
  void __fastcall btImgOrigClick(TObject *Sender);
  void __fastcall cbStretchClick(TObject *Sender);
  void __fastcall btTamanhoTelaClick(TObject *Sender);
  void __fastcall tbOffsetSigChange(TObject *Sender);
  void __fastcall tbEsticaSigChange(TObject *Sender);
private:	// User declarations
  int FatorAumento;
  float FatorPropX, FatorPropY;
  bool pressionado;
  int UltX, UltY;
  void __fastcall AjustaTamanhoFoto();
  void __fastcall MostraInf(int X, int Y, int &r, int &g, int &b);
public:		// User declarations
  _Bitmap *BitCores;
  _Bitmap *BitTemp;
  int UltClickX;
  int UltClickY;
  __fastcall TZoom(TComponent* Owner);
  void __fastcall CarregaBitmap(_Bitmap *Bitmap);
  void __fastcall Dimensiona(float Fator);
  void __fastcall Status(AnsiString Text);
  void __fastcall MostraCores(int Azul, int Verde, int Vermelho);

  TEdit **Edits;
  TTrackBar **TrackBars;
  TCheckBox *CheckBox;
};
//---------------------------------------------------------------------------
extern PACKAGE TZoom *Zoom;
//---------------------------------------------------------------------------

void __fastcall LumTresh(_Bitmap *ImgSrc, _Bitmap *ImgDest, int Tresh,
                    bool MarcaCorte, bool Original, int IndiceCor);
                    
void __fastcall ImgSig(_Bitmap *ImgSrc, _Bitmap *ImgDest, int offset, float estica);

#endif
