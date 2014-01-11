//---------------------------------------------------------------------------

#ifndef UAuxTioPatinhasH
#define UAuxTioPatinhasH
//---------------------------------------------------------------------------

#include "UCBitmap.h"
#include "UGeral.h"
#include <vector>
#include <Math.hpp>
//#define DEBUG

#define PARAMETROS_MLT  \
X(PropYIni, Double)     \
X(PropXFim, Double)

#define PARAMETROS_ABT    \
X(AltMinTarja, Int)       \
X(AltMaxTarja, Int)       \
X(DistMaxTarjas, Int)     \
X(LargMinTarja, Int)      \
X(LargMaxTarja, Int)      \
X(DesvioMax, Double)

#define PARAMETROS_AI                     \
X(DistFaixaRef, Int)                      \
X(LargFaixaRef, Int)                      \
X(DifMinMediaFaixaRef, Int)               \
X(LargIdentificador, Int)                 \
X(AltIdentificador, Int)                  \
X(XIniParaRefTarja, Int)                  \
X(YIniParaRefTarja, Int)                  \
X(MaiorDistSemPixelsIdentificador, Int)   \
X(NumMinPixelsIdentificador, Int)         \
X(AltMinGrupoConexoIdentificador, Int)    \
X(LimiarAlturaIdentificador, Int)         \
X(LimiarInclinacaoidentificador, Double)  \
X(LimiarLargLinhasIdentificador, Double)  \
X(LimiarRelacaoLargAlt, Double)           \
X(LimiarNumMedColunas, Double)            \
X(DifMinEmbGrupoEmbRegiaoIdentificador, Int)

#define PREFIXOS_ESTRUTURAS \
Y(MLT, z)   \
Y(ABT, z)   \
Y(AI, z)    \

using namespace std;
typedef unsigned int uint;
enum TTipoBorda {BORDA_CLARO_ESCURO, BORDA_ESCURO_CLARO};

struct TPonto
{
  unsigned short int x;
  unsigned short int y;
};
//---------------------------------------------------------------------------

struct TBorda
{
  int Y;
  TTipoBorda TipoBorda;
};
//---------------------------------------------------------------------------

struct TLimitesVerticaisGrupo
{
  int yEnc, yEmb;
};       
//---------------------------------------------------------------------------

typedef vector<TBorda> TVectorBorda;

struct TBordasColunas
{
  TBordasColunas(int numColunas);
  ~TBordasColunas();
  
  TVectorBorda **Bordas;
  int NumColunas;
};
//---------------------------------------------------------------------------

struct TMeioBordas
{
  int Y1, Y2, yMeio, Altura;
  void Inicializa(int y1, int y2);
};
//---------------------------------------------------------------------------

typedef vector<TMeioBordas> TVectorMeioBordas;

struct TConjuntoMeioBordas
{
  TConjuntoMeioBordas(int numColunas);
  ~TConjuntoMeioBordas();
  TVectorMeioBordas **VectorMeioBordas;
  int NumColunas;
};
//---------------------------------------------------------------------------

typedef vector<int> TVectorInt;

struct TTarja
{
  int X;
  int UltYMeio;
  int PriYEnc;
  bool Ativa(int x);
  TVectorInt VetorAlturas;
};
//---------------------------------------------------------------------------

typedef vector<TTarja> TVectorTarja;

struct TParamsMLT
{
  CTonsCinza *TCImgSrc;
  CBitmap *BImgDest;
  //Fator que define a regi�o de busca da tarja. Multiplica-se a largura da imagem por este fator
  float PropYIni;
  float PropXFim;
  //retorno
  TBordasColunas *BordasColunas;
  
  ~TParamsMLT();
};
//---------------------------------------------------------------------------

class CMatrizInteiro
{
public:
  CMatrizInteiro(int larg, int alt);
  ~CMatrizInteiro();
  int **Matriz;
  int Larg, Alt;
};
//---------------------------------------------------------------------------

struct TParamsABT
{                 
  CBitmap *BImgDest;
  //Altura m�nima que uma faixa escura pode ter para ser considerada tarja
  int AltMinTarja;
  //Altura m�xima que uma faixa escura pode ter para ser considerada tarja
  int AltMaxTarja;
  //dist�ncia horizontal m�xima entre faixas escuras para serem consideradas tarja
  int DistMaxTarjas;
  //Largura m�nima de uma faixa escura para ser considerada tarja
  uint LargMinTarja;
  //Largura m�nima de uma faixa escura para ser considerada tarja
  uint LargMaxTarja;
  //Define o desvio m�dio m�ximo para que uma faixa seja considerada a tarja.
  //Este desvio � a m�dia da diferen�a de altura de cada coluna da faixa para a m�dia das alturas
  double DesvioMax;

  bool AchouTarja;
  TPonto RefTarja;
  //Vetor com locais de bordas que s�o analizadas para saber se s�o bordas da tarja
  TBordasColunas *BordasColunas;
  TConjuntoMeioBordas *ConjuntoMeioBordas;  
  TVectorTarja VectorTarja;
  double MediaAlturaTarja;
  ~TParamsABT();
};
//---------------------------------------------------------------------------

struct TParamsAI
{            
  CTonsCinza *TCImgSrc;
  CBitmap *BImgDest;
  TPonto RefTarja;
  //Dist�ncia da parte de cima da tarja para uma faixa de refer�ncia de um pixel de altura,
  //que � usada para ver a m�dia da luminosidade logo acima da tarja
  int DistFaixaRef;
  //largura da faixa de refer�ncia
  int LargFaixaRef;
  //Utilizada para determinar a luminosidade m�xima de um pixel para que ele seja considerado
  // como um pixel do identificador. Esta luminosidade m�xima �:
  // Limiar=Media-ParamsAI.DifMinMediaFaixaRef, onde Media � a m�dia de luminosidade da faixa
  // de refer�ncia.
  int DifMinMediaFaixaRef;
  //Largura m�xima em pixels do identificador. Utilizada para definir a regi�o de busca de
  // pixels de identificador
  int LargIdentificador;
  //An�logo � LargIdentificador
  int AltIdentificador;
  //deslocamento para � esquerda em pixels do in�cio da regi�o de busca dos pixels de identificador.
  // O in�cio da regi�o de busca fica: xIni=ParamsAI.RefTarja.x-ParamsAI.XIniParaRefTarja
  int XIniParaRefTarja;
  // yIni=ParamsAI.RefTarja.y-ParamsAI.YIniParaRefTarja
  int YIniParaRefTarja;
  //Define o maior gap em n�mero de pixels que pode haver para que n�o se considere que o
  //identificador est� quebrado. Se houve um gap maior que o definido, considera-se que
  //o identificador acabou. Este gap � de altura de pixels, considerando que o processamento
  //� de cima para baixo
  int MaiorDistSemPixelsIdentificador;
  //Usado para determinar o n�mero m�nimo de pixels j� processados com luminosidade abaixo do limiar
  //para se considerar que j� foram processados pixels de identificador
  int NumMinPixelsIdentificador;
  //define a altura m�nima para que um grupo conexo de pixels possivelmente de identificador
  //seja considerado de fato do identificador
  int AltMinGrupoConexoIdentificador;
  //Limiar que define se um identificador tem 1 ou 2 "andares"
  int LimiarAlturaIdentificador;
  //Limiar que define se um identificador � inclinado ou n�o
  float LimiarInclinacaoidentificador;
  //Limiar que define a propor��o entre a largura mediana da parte de cima e de baixo
  //do identificador
  float LimiarLargLinhasIdentificador;
  //limiar que define a maior rela��o entre largura e altura do identificador
  float LimiarRelacaoLargAlt;
  float LimiarNumMedColunas;
  //Diferen�a m�xima entre entre a parte de baixo da regi�o do identificador e a parte de baixo.
  //de cada regi�o candidata � regi�o de identificador. Multiplicado pela altura da tarja
  int DifMinEmbGrupoEmbRegiaoIdentificador;

  //Retorno
  int Alt;
  float Inclinacao;
  int MaiorLargLinha;
  float RelacaoMedianasLargurasEncEmb;
  float RelacaoLargAlt;
  //n�mero m�dio de vezes que percorrendo-se da esquerda para direita a regi�o do identificador
  //ocorre do pixel anterior n�o ser de identificador, e o corrente ser de identificador
  float NumMedColunas;

  int ValorCedula;
};
//---------------------------------------------------------------------------

struct TParamsRC
{
  TParamsMLT ParamsMLT;
  TParamsABT ParamsABT;
  TParamsAI ParamsAI;
  int LumMedianaImagem;
  void ConverteParametrosDependentesLargura();//pega os fatores e multiplica para pegar valores absolutos
  void ConverteParametrosDependentesAlturaFaixa();//pega os fatores e multiplica para pegar valores absolutos
  void ConverteParametrosDependentesLumMediana();//pega os fatores e multiplica para pegar valores absolutos
};
//---------------------------------------------------------------------------

void Media2(CTonsCinza *TCImgSrc, CTonsCinza *TCImgDest);

#endif
