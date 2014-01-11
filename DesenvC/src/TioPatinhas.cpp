//---------------------------------------------------------------------------
#include <vcl.h>
#include "UMain.h"
#pragma hdrstop
USEFORM("UMain.cpp", Main);
//---------------------------------------------------------------------------
TMessageEvent NaMensagem;
WINAPI WinMain(HINSTANCE, HINSTANCE, LPSTR, int)
{
  try
  {
     Application->Initialize();
     Application->Title = "Reconhecedor de Cédulas";
     Application->CreateForm(__classid(TMain), &Main);
     Application->Run();
  }
  catch (Exception &exception)                     
  {
     Application->ShowException(&exception);
  }
  return 0;
}
//---------------------------------------------------------------------------

