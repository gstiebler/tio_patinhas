//---------------------------------------------------------------------------

#ifndef UTioPatinhasH
#define UTioPatinhasH

#include "UAuxTioPatinhas.h"
#include "UGeral.h"
#include "UImgFuncs.h"

#define TAM_VETOR_LIMITES_VERTICAIS_GRUPOS 300
#define PIXEL_ACEITO 1
#define PIXEL_NAO_ACEITO 2
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
void MatrizGruposConexos(CTonsCinza *tcImgSrc, TRect ARect, int **MatrizGrupos, byte limiar,
                          TLimitesVerticaisGrupo *VetorLimitesVerticaisGrupo, int *PonteiroGrupos);
void SelecionaGruposIdentificador(TLimitesVerticaisGrupo *VetorLimitesVerticaisGrupo,
        char *VetGruposValidos, int AltMin, int *PonteiroGrupos, int yFim, int DifMinEmb);
void CopiaGruposValidos(int **MatrizGrupos, TRect &ARect, char *VetGruposValidos);
void PintaIdentificador(CBitmap *BImgDest, TRect &ARect, int **MatrizGrupos);
void EscreveParametros(TParamsRC &ParamsRC);

#endif
