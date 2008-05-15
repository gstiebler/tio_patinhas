object Zoom: TZoom
  Left = 0
  Top = 169
  Width = 1023
  Height = 571
  Caption = 'Zoom'
  Color = clBtnFace
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'MS Sans Serif'
  Font.Style = []
  OldCreateOrder = False
  WindowState = wsMaximized
  OnCreate = FormCreate
  OnDestroy = FormDestroy
  OnMouseMove = FormMouseMove
  PixelsPerInch = 96
  TextHeight = 13
  object ScrollBox1: TScrollBox
    Left = 0
    Top = 257
    Width = 1015
    Height = 261
    Align = alClient
    TabOrder = 1
    object Image1: TImage
      Left = 0
      Top = 0
      Width = 569
      Height = 345
      Stretch = True
      OnMouseDown = Image1MouseDown
      OnMouseMove = Image1MouseMove
      OnMouseUp = Image1MouseUp
    end
  end
  object Panel1: TPanel
    Left = 0
    Top = 0
    Width = 1015
    Height = 257
    Align = alTop
    TabOrder = 0
    object Image2: TImage
      Left = 168
      Top = 8
      Width = 320
      Height = 240
      Stretch = True
      OnMouseDown = Image2MouseDown
    end
    object Label1: TLabel
      Left = 16
      Top = 32
      Width = 10
      Height = 13
      Caption = 'X:'
    end
    object Label2: TLabel
      Left = 16
      Top = 56
      Width = 10
      Height = 13
      Caption = 'Y:'
    end
    object Label3: TLabel
      Left = 16
      Top = 80
      Width = 16
      Height = 13
      Caption = 'X2:'
    end
    object Label4: TLabel
      Left = 16
      Top = 104
      Width = 23
      Height = 13
      Caption = 'B/R:'
    end
    object Label5: TLabel
      Left = 16
      Top = 128
      Width = 24
      Height = 13
      Caption = 'G/R:'
    end
    object Label6: TLabel
      Left = 16
      Top = 152
      Width = 23
      Height = 13
      Caption = 'Lum:'
    end
    object Label7: TLabel
      Left = 16
      Top = 176
      Width = 23
      Height = 13
      Caption = 'Azul:'
    end
    object Label8: TLabel
      Left = 16
      Top = 200
      Width = 31
      Height = 13
      Caption = 'Verde:'
    end
    object Label9: TLabel
      Left = 16
      Top = 224
      Width = 47
      Height = 13
      Caption = 'Vermelho:'
    end
    object sbFator: TScrollBar
      Left = 2
      Top = 3
      Width = 159
      Height = 16
      LargeChange = 5
      Min = 10
      PageSize = 0
      Position = 30
      TabOrder = 0
      OnChange = sbFatorChange
    end
    object edX: TEdit
      Left = 72
      Top = 24
      Width = 41
      Height = 21
      TabOrder = 1
      Text = 'edX'
    end
    object edY: TEdit
      Left = 72
      Top = 48
      Width = 41
      Height = 21
      TabOrder = 2
      Text = 'edY'
    end
    object edX2: TEdit
      Left = 72
      Top = 72
      Width = 41
      Height = 21
      TabOrder = 3
      Text = 'edX2'
    end
    object edBR: TEdit
      Left = 72
      Top = 96
      Width = 41
      Height = 21
      TabOrder = 4
      Text = 'edBR'
    end
    object edGR: TEdit
      Left = 72
      Top = 120
      Width = 41
      Height = 21
      TabOrder = 5
      Text = 'edGR'
    end
    object edLum: TEdit
      Left = 72
      Top = 144
      Width = 41
      Height = 21
      TabOrder = 6
      Text = 'edLum'
    end
    object edAzul: TEdit
      Left = 72
      Top = 168
      Width = 41
      Height = 21
      TabOrder = 7
      Text = 'edAzul'
    end
    object edVerde: TEdit
      Left = 72
      Top = 192
      Width = 41
      Height = 21
      TabOrder = 8
      Text = 'edVerde'
    end
    object edVermelho: TEdit
      Left = 72
      Top = 216
      Width = 41
      Height = 21
      TabOrder = 9
      Text = 'edVermelho'
    end
    object pnOriginal: TPanel
      Left = 496
      Top = 8
      Width = 505
      Height = 241
      TabOrder = 10
      object imCores: TImage
        Left = 16
        Top = 44
        Width = 257
        Height = 30
      end
      object edFundoCor: TEdit
        Left = 188
        Top = 15
        Width = 29
        Height = 21
        Color = clBlack
        Font.Charset = DEFAULT_CHARSET
        Font.Color = clLime
        Font.Height = -11
        Font.Name = 'MS Sans Serif'
        Font.Style = [fsBold]
        ParentFont = False
        TabOrder = 0
        Text = '80'
      end
      object tbFundoCor: TTrackBar
        Tag = 12
        Left = 13
        Top = 10
        Width = 164
        Height = 33
        HelpType = htKeyword
        Max = 255
        Orientation = trHorizontal
        Frequency = 10
        Position = 255
        SelEnd = 0
        SelStart = 0
        TabOrder = 1
        ThumbLength = 12
        TickMarks = tmBoth
        TickStyle = tsAuto
        OnChange = tbFundoCorChange
      end
      object cbMarcaCorte: TCheckBox
        Left = 15
        Top = 140
        Width = 50
        Height = 17
        Caption = 'marca'
        TabOrder = 2
      end
      object cbOriginal: TCheckBox
        Left = 16
        Top = 108
        Width = 57
        Height = 17
        Caption = 'original'
        TabOrder = 3
      end
      object cbMostraCores: TCheckBox
        Left = 15
        Top = 123
        Width = 86
        Height = 17
        Caption = 'mostra cores'
        Checked = True
        State = cbChecked
        TabOrder = 4
      end
      object rgCor: TRadioGroup
        Left = 13
        Top = 162
        Width = 84
        Height = 71
        Caption = 'Cor'
        ItemIndex = 0
        Items.Strings = (
          'azul'
          'verde'
          'vermelho')
        TabOrder = 5
      end
      object GroupBox1: TGroupBox
        Left = 104
        Top = 88
        Width = 393
        Height = 145
        Caption = 'Gera Cor'
        TabOrder = 6
        object lbAzul: TLabel
          Left = 16
          Top = 16
          Width = 20
          Height = 13
          Caption = 'Azul'
        end
        object lbVerde: TLabel
          Left = 16
          Top = 56
          Width = 28
          Height = 13
          Caption = 'Verde'
        end
        object lbVermelho: TLabel
          Left = 16
          Top = 96
          Width = 44
          Height = 13
          Caption = 'Vermelho'
        end
        object sbAzul: TScrollBar
          Left = 16
          Top = 32
          Width = 249
          Height = 17
          LargeChange = 5
          Max = 255
          PageSize = 0
          TabOrder = 0
          OnChange = CoresChange
        end
        object sbVerde: TScrollBar
          Left = 16
          Top = 72
          Width = 249
          Height = 17
          LargeChange = 5
          Max = 255
          PageSize = 0
          TabOrder = 1
          OnChange = CoresChange
        end
        object sbVermelho: TScrollBar
          Left = 16
          Top = 112
          Width = 249
          Height = 17
          LargeChange = 5
          Max = 255
          PageSize = 0
          TabOrder = 2
          OnChange = CoresChange
        end
        object pnCor: TPanel
          Left = 280
          Top = 32
          Width = 100
          Height = 100
          TabOrder = 3
        end
      end
      object btImgOrig: TButton
        Left = 224
        Top = 8
        Width = 97
        Height = 25
        Caption = 'Imagem Original'
        TabOrder = 7
        OnClick = btImgOrigClick
      end
      object btTamanhoTela: TButton
        Left = 344
        Top = 8
        Width = 75
        Height = 25
        Caption = 'Aumentar'
        TabOrder = 8
        OnClick = btTamanhoTelaClick
      end
      object tbOffsetSig: TTrackBar
        Tag = 12
        Left = 309
        Top = 34
        Width = 164
        Height = 33
        HelpType = htKeyword
        Max = 255
        Orientation = trHorizontal
        Frequency = 10
        Position = 255
        SelEnd = 0
        SelStart = 0
        TabOrder = 9
        ThumbLength = 12
        TickMarks = tmBoth
        TickStyle = tsAuto
        OnChange = tbOffsetSigChange
      end
      object tbEsticaSig: TTrackBar
        Tag = 12
        Left = 309
        Top = 58
        Width = 164
        Height = 33
        HelpType = htKeyword
        Max = 255
        Orientation = trHorizontal
        Frequency = 10
        Position = 10
        SelEnd = 0
        SelStart = 0
        TabOrder = 10
        ThumbLength = 12
        TickMarks = tmBoth
        TickStyle = tsAuto
        OnChange = tbEsticaSigChange
      end
    end
    object pnParams: TPanel
      Left = 493
      Top = 240
      Width = 508
      Height = 241
      TabOrder = 11
      Visible = False
      object sbParams: TScrollBox
        Left = 1
        Top = 1
        Width = 506
        Height = 239
        Align = alClient
        TabOrder = 0
      end
    end
    object cbStretch: TCheckBox
      Left = 16
      Top = 240
      Width = 65
      Height = 17
      Caption = 'Stretch'
      Checked = True
      State = cbChecked
      TabOrder = 12
      OnClick = cbStretchClick
    end
  end
  object StatusBar1: TStatusBar
    Left = 0
    Top = 518
    Width = 1015
    Height = 19
    Panels = <
      item
        Width = 100
      end>
    SimplePanel = False
  end
end
