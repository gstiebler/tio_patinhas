//---------------------------------------------------------------------------

#ifndef UTioPatinhasH
#define UTioPatinhasH

#include "UAuxTioPatinhas.h"
#include "UGeral.h"

#define TAM_VETOR_LIMITES_VERTICAIS_GRUPOS 300
//---------------------------------------------------------------------------

void ReconheceCedula(TParamsRC &ParamsRC);
void MostraLimiteTarja(TParamsMLT &ParamsMLT);
void AnalizaBordasTarja(TParamsABT &ParamsABT);  
void SelecionaTarja(TParamsABT &ParamsABT);
void PreparaSelecionaTarja(TParamsABT &ParamsABT);
void AnalizaIdentificador(TParamsAI &ParamsAI);
byte MediaFaixa(TParamsAI &ParamsAI);
void Identifica(TParamsAI &ParamsAI);
float RetornaRelacaoMedianasLargurasEncEmb(int *VetorLarguras, int comeco, int fim,
                                                                        int  &MediaLarguras);
void MatrizGruposConexos(CTonsCinza *tcImgSrc, TRect ARect,
            int **MatrizGrupos, byte limiar, TLimitesVerticaisGrupo *VetorLimitesVerticaisGrupo);
void SelecionaGruposIdentificador(TLimitesVerticaisGrupo *VetorLimitesVerticaisGrupo,
                bool *VetBoolGruposValidos, int AltMin);

#endif
