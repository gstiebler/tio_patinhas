object Main: TMain
  Left = -9
  Top = 87
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
    Left = 350
    Top = 464
    Width = 195
    Height = 99
    FileList = flb1
    ItemHeight = 16
    TabOrder = 2
  end
  object dcb1: TDriveComboBox
    Left = 350
    Top = 568
    Width = 195
    Height = 19
    DirList = dlb1
    TabOrder = 1
  end
  object flb1: TFileListBox
    Left = 350
    Top = 597
    Width = 195
    Height = 116
    ItemHeight = 16
    Mask = '*.tif;*.bmp'
    ShowGlyphs = True
    TabOrder = 0
    OnClick = flb1Click
  end
  object Panel: TPanel
    Left = 556
    Top = 468
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
  object Panel1: TPanel
    Left = 12
    Top = 12
    Width = 333
    Height = 253
    TabOrder = 4
    object imgOriginal: TImage
      Left = 4
      Top = 4
      Width = 320
      Height = 240
      Proportional = True
      Stretch = True
      OnClick = ZoomImagem
    end
  end
  object Panel2: TPanel
    Left = 12
    Top = 268
    Width = 333
    Height = 253
    TabOrder = 5
    object imgTemp: TImage
      Left = 4
      Top = 4
      Width = 320
      Height = 240
      Proportional = True
      Stretch = True
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
    Left = 360
    Top = 8
    Width = 889
    Height = 441
    ActivePage = tsTioPatinhas
    TabIndex = 0
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
  end
  object btReconheceCedula: TButton
    Left = 896
    Top = 468
    Width = 113
    Height = 25
    Caption = 'Reconhece Cedula'
    TabOrder = 8
    OnClick = btReconheceCedulaClick
  end
end
