//---------------------------------------------------------------------------

#ifndef UImgFuncsH
#define UImgFuncsH
//---------------------------------------------------------------------------
#endif

#include "UCBitmap.h"

void PintaVetor(int *vetor, int tam, _Bitmap *BitDest, TColor cor);
void Histograma(CTonsCinza *TCImgSrc, _Bitmap *BitHist, _Bitmap *BitCumulativa, int &Mediana);
