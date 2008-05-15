//---------------------------------------------------------------------------


#pragma hdrstop

#include "UGeral.h"

//---------------------------------------------------------------------------

#pragma package(smart_init)


int Round(double valor)
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

int ComparaInteiro(const void *Item1, const void *Item2)
{
  return (  *(int *)Item1-*(int *)Item2  );
}
//---------------------------------------------------------------------------