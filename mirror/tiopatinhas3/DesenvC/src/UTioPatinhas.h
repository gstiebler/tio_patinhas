//---------------------------------------------------------------------------

#ifndef UTioPatinhasH
#define UTioPatinhasH

#include "UAuxTioPatinhas.h"
#include "UGeral.h"

//---------------------------------------------------------------------------

void ReconheceCedula(TParamsRC &ParamsRC);
void MostraLimiteTarja(TParamsMLT &ParamsMLT);
void AnalizaBordasTarja(TParamsABT &ParamsABT);  
void SelecionaTarja(TParamsABT &ParamsABT);
void PreparaSelecionaTarja(TParamsABT &ParamsABT);
void AnalizaIdentificador(TParamsAI &ParamsAI);
byte MediaFaixa(TParamsAI &ParamsAI);
void Identifica(TParamsAI &ParamsAI);
float RetornaRelacaoMedianasLargurasEncEmb(int *VetorLarguras, int comeco, int fim);

#endif
