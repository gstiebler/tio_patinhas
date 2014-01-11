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
  //Fator que define a região de busca da tarja. Multiplica-se a largura da imagem por este fator
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
  //Altura mínima que uma faixa escura pode ter para ser considerada tarja
  int AltMinTarja;
  //Altura máxima que uma faixa escura pode ter para ser considerada tarja
  int AltMaxTarja;
  //distância horizontal máxima entre faixas escuras para serem consideradas tarja
  int DistMaxTarjas;
  //Largura mínima de uma faixa escura para ser considerada tarja
  uint LargMinTarja;
  //Largura mínima de uma faixa escura para ser considerada tarja
  uint LargMaxTarja;
  //Define o desvio médio máximo para que uma faixa seja considerada a tarja.
  //Este desvio é a média da diferença de altura de cada coluna da faixa para a média das alturas
  double DesvioMax;

  bool AchouTarja;
  TPonto RefTarja;
  //Vetor com locais de bordas que são analizadas para saber se são bordas da tarja
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
  //Distância da parte de cima da tarja para uma faixa de referência de um pixel de altura,
  //que é usada para ver a média da luminosidade logo acima da tarja
  int DistFaixaRef;
  //largura da faixa de referência
  int LargFaixaRef;
  //Utilizada para determinar a luminosidade máxima de um pixel para que ele seja considerado
  // como um pixel do identificador. Esta luminosidade máxima é:
  // Limiar=Media-ParamsAI.DifMinMediaFaixaRef, onde Media é a média de luminosidade da faixa
  // de referência.
  int DifMinMediaFaixaRef;
  //Largura máxima em pixels do identificador. Utilizada para definir a região de busca de
  // pixels de identificador
  int LargIdentificador;
  //Análogo à LargIdentificador
  int AltIdentificador;
  //deslocamento para à esquerda em pixels do início da região de busca dos pixels de identificador.
  // O início da região de busca fica: xIni=ParamsAI.RefTarja.x-ParamsAI.XIniParaRefTarja
  int XIniParaRefTarja;
  // yIni=ParamsAI.RefTarja.y-ParamsAI.YIniParaRefTarja
  int YIniParaRefTarja;
  //Define o maior gap em número de pixels que pode haver para que não se considere que o
  //identificador está quebrado. Se houve um gap maior que o definido, considera-se que
  //o identificador acabou. Este gap é de altura de pixels, considerando que o processamento
  //é de cima para baixo
  int MaiorDistSemPixelsIdentificador;
  //Usado para determinar o número mínimo de pixels já processados com luminosidade abaixo do limiar
  //para se considerar que já foram processados pixels de identificador
  int NumMinPixelsIdentificador;
  //define a altura mínima para que um grupo conexo de pixels possivelmente de identificador
  //seja considerado de fato do identificador
  int AltMinGrupoConexoIdentificador;
  //Limiar que define se um identificador tem 1 ou 2 "andares"
  int LimiarAlturaIdentificador;
  //Limiar que define se um identificador é inclinado ou não
  float LimiarInclinacaoidentificador;
  //Limiar que define a proporção entre a largura mediana da parte de cima e de baixo
  //do identificador
  float LimiarLargLinhasIdentificador;
  //limiar que define a maior relação entre largura e altura do identificador
  float LimiarRelacaoLargAlt;
  float LimiarNumMedColunas;
  //Diferença máxima entre entre a parte de baixo da região do identificador e a parte de baixo.
  //de cada região candidata à região de identificador. Multiplicado pela altura da tarja
  int DifMinEmbGrupoEmbRegiaoIdentificador;

  //Retorno
  int Alt;
  float Inclinacao;
  int MaiorLargLinha;
  float RelacaoMedianasLargurasEncEmb;
  float RelacaoLargAlt;
  //número médio de vezes que percorrendo-se da esquerda para direita a região do identificador
  //ocorre do pixel anterior não ser de identificador, e o corrente ser de identificador
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
