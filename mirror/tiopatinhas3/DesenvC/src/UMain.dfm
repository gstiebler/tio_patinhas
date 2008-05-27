object Main: TMain
  Left = 148
  Top = 76
  Width = 1292
  Height = 793
  HorzScrollBar.Smooth = True
  HorzScrollBar.Tracking = True
  AlphaBlendValue = 0
  Caption = 'Tio Patinhas'
  Color = clBtnFace
  Font.Charset = ANSI_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'MS Sans Serif'
  Font.Style = []
  OldCreateOrder = False
  Position = poDesktopCenter
  Visible = True
  OnCreate = FormCreate
  PixelsPerInch = 96
  TextHeight = 13
  object dlb1: TDirectoryListBox
    Left = 342
    Top = 16
    Width = 195
    Height = 185
    FileList = flb1
    ItemHeight = 16
    TabOrder = 2
  end
  object dcb1: TDriveComboBox
    Left = 342
    Top = 208
    Width = 195
    Height = 19
    DirList = dlb1
    TabOrder = 1
  end
  object flb1: TFileListBox
    Left = 342
    Top = 237
    Width = 195
    Height = 204
    ItemHeight = 16
    Mask = '*.tif;*.bmp'
    ShowGlyphs = True
    TabOrder = 0
    OnClick = flb1Click
  end
  object Panel: TPanel
    Left = 348
    Top = 460
    Width = 333
    Height = 253
    TabOrder = 3
    object imgProcessada: TImage
      Left = 4
      Top = 4
      Width = 320
      Height = 240
      AutoSize = True
      Proportional = True
      OnClick = ZoomImagem
    end
  end
  object pnlCaptura: TPanel
    Left = 4
    Top = 12
    Width = 320
    Height = 240
    TabOrder = 4
  end
  object Panel2: TPanel
    Left = 4
    Top = 340
    Width = 333
    Height = 253
    TabOrder = 5
    object imgTemp: TImage
      Left = 4
      Top = 4
      Width = 320
      Height = 240
      AutoSize = True
      Proportional = True
      OnClick = ZoomImagem
    end
  end
  object StatusBar1: TStatusBar
    Left = 0
    Top = 740
    Width = 1284
    Height = 19
    Panels = <
      item
        Width = 500
      end
      item
        Width = 200
      end
      item
        Width = 200
      end
      item
        Width = 200
      end>
    SimplePanel = False
  end
  object PageControl1: TPageControl
    Left = 544
    Top = 8
    Width = 665
    Height = 441
    ActivePage = tsHistograma
    TabIndex = 2
    TabOrder = 7
    object tsTioPatinhas: TTabSheet
      Caption = 'TioPatinhas'
      ImageIndex = 1
    end
    object tsLog: TTabSheet
      Caption = 'Log'
      ImageIndex = 1
      object mmLog: TMemo
        Left = 0
        Top = 0
        Width = 881
        Height = 413
        Align = alClient
        ScrollBars = ssVertical
        TabOrder = 0
      end
    end
    object tsHistograma: TTabSheet
      Caption = 'tsHistograma'
      ImageIndex = 2
      object imgHistograma: TImage
        Left = 152
        Top = 32
        Width = 256
        Height = 102
        OnClick = ZoomImagem
      end
      object imgHistCumulativa: TImage
        Left = 152
        Top = 152
        Width = 256
        Height = 102
        OnClick = ZoomImagem
      end
      object btHistograma: TButton
        Left = 32
        Top = 24
        Width = 75
        Height = 25
        Caption = 'Histograma'
        TabOrder = 0
        OnClick = btHistogramaClick
      end
    end
  end
  object btReconheceCedula: TButton
    Left = 688
    Top = 460
    Width = 113
    Height = 25
    Caption = 'Reconhece Cedula'
    TabOrder = 8
    OnClick = btReconheceCedulaClick
  end
  object btIniciarCaptura: TButton
    Left = 24
    Top = 272
    Width = 89
    Height = 25
    Caption = 'Iniciar Captura'
    TabOrder = 9
    OnClick = btIniciarCapturaClick
  end
  object edPlaca: TEdit
    Left = 128
    Top = 272
    Width = 25
    Height = 21
    TabOrder = 10
    Text = '1'
  end
  object edCanal: TEdit
    Left = 160
    Top = 272
    Width = 25
    Height = 21
    TabOrder = 11
    Text = '0'
  end
  object btCapturar: TButton
    Left = 216
    Top = 272
    Width = 75
    Height = 25
    Caption = 'Capturar'
    TabOrder = 12
    OnClick = btCapturarClick
  end
  object cbTocaSom: TCheckBox
    Left = 688
    Top = 528
    Width = 97
    Height = 17
    Caption = 'toca som'
    TabOrder = 13
  end
  object MediaPlayer: TMediaPlayer
    Left = 1064
    Top = 472
    Width = 29
    Height = 30
    VisibleButtons = [btPlay]
    Visible = False
    TabOrder = 14
  end
  object btSalvar: TButton
    Left = 24
    Top = 312
    Width = 97
    Height = 25
    Caption = 'Salvar Captura'
    TabOrder = 15
    OnClick = btSalvarClick
  end
  object cbSalvarAoCapturar: TCheckBox
    Left = 192
    Top = 312
    Width = 121
    Height = 17
    Caption = 'salvar ao capturar'
    TabOrder = 16
  end
  object btReconheceProxima: TButton
    Left = 688
    Top = 496
    Width = 113
    Height = 25
    Caption = 'Reconhece Proxima'
    TabOrder = 17
    OnClick = btReconheceProximaClick
  end
end
