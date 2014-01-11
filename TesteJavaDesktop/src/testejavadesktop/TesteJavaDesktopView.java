/*
 * TesteJavaDesktopView.java
 */
package testejavadesktop;

import java.awt.GridLayout;
import java.awt.Image;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import java.io.*;
import java.net.MalformedURLException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The application's main frame.
 */
public class TesteJavaDesktopView extends FrameView {

    public TesteJavaDesktopView(SingleFrameApplication app) {
        super(app);

        initComponents();

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;

            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");


        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                }
            }
        });
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = TesteJavaDesktopApp.getApplication().getMainFrame();
            aboutBox = new TesteJavaDesktopAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        TesteJavaDesktopApp.getApplication().show(aboutBox);
    }

    @Action
    public void AjustaParametros() {
        AjustesParametros.AjustaParametros(cbCarregaImagensAjusta.isSelected());
    }

    @Action
    public void ExecutarUma() {
        try {
            COutputDebug.OutTela=true;
            JPanel cp = new JPanel(new GridLayout(0, 1));
            ImageProcessor imgProc = new ImageProcessor();
            TParamsDir ParamsDir = new TParamsDir();
            Image image2 = imgProc.loadbitmap(txCaminhoArquivo.getText());
            Image image = ImageProcessor.toBufferedImage(image2);

            TParamsRC ParamsRC = new TParamsRC(new TParamsIni(ParamsDir.getDirBase() + "ParamsTP.ini"),
                    new CTonsCinza(image), new CBitmap(image), new CBitmap(image));
            UTioPatinhas.ReconheceCedula(ParamsRC);
            System.out.println("Valor cédula: " +
                    String.valueOf(ParamsRC.ParamsAI.ValorCedula));

            addImage(cp, image);
            addImage(cp, ParamsRC.ParamsMLT.BImgDest.SaveImage());

            JFrame fr = new JFrame("Tio Patinhas Debug");
            fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            fr.setContentPane(cp);
            fr.pack();
            fr.setLocationRelativeTo(null);
            fr.setVisible(true);

            mainPanel.add(fr);


        } catch (IOException e) {
            // catch io errors from FileInputStream or readLine() 
            System.out.println("Uh oh, got an IOException error!" + e.getMessage());

        }

    }

    @Action
    public void ExecutaTestes() {
        try {
            COutputDebug.OutTela=false;
            jTextArea1.append(CTestes.ExecutaTestes(cbCarregaImagens.isSelected(), cbSalvaErradas.isSelected(), cbServidorImagens.isSelected()));
        } catch (IOException ex) {
            Logger.getLogger(TesteJavaDesktopView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Action
    public void ConectaSocket() throws IOException
    {
        CClienteSocket socket=new CClienteSocket();
    }

    @Action
    public void CarregaDir()
    {
        File dir = new File(txDiretorioImagens.getText());
        for (int n=0; n<dir.list().length; n++)
            taArquivos.append(dir.list()[n]+"\n");
    }

    static void addImage(JPanel cp, Image img) throws MalformedURLException {
        cp.add(new JLabel(new ImageIcon(img, "test image")));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        txCaminhoArquivo = new javax.swing.JTextField();
        cbCarregaImagens = new javax.swing.JCheckBox();
        cbSalvaErradas = new javax.swing.JCheckBox();
        jButton4 = new javax.swing.JButton();
        cbServidorImagens = new javax.swing.JCheckBox();
        txDiretorioImagens = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        taArquivos = new javax.swing.JTextArea();
        cbCarregaImagensAjusta = new javax.swing.JCheckBox();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();

        mainPanel.setName("mainPanel"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(testejavadesktop.TesteJavaDesktopApp.class).getContext().getActionMap(TesteJavaDesktopView.class, this);
        jButton1.setAction(actionMap.get("ExecutarUma")); // NOI18N
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(testejavadesktop.TesteJavaDesktopApp.class).getContext().getResourceMap(TesteJavaDesktopView.class);
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setName("jTextArea1"); // NOI18N
        jScrollPane1.setViewportView(jTextArea1);

        jPanel1.setName("jPanel1"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 54, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 231, Short.MAX_VALUE)
        );

        jButton2.setAction(actionMap.get("ExecutaTestes")); // NOI18N
        jButton2.setText(resourceMap.getString("jButton2.text")); // NOI18N
        jButton2.setName("jButton2"); // NOI18N

        jButton3.setAction(actionMap.get("AjustaParametros")); // NOI18N
        jButton3.setText(resourceMap.getString("jButton3.text")); // NOI18N
        jButton3.setName("jButton3"); // NOI18N

        txCaminhoArquivo.setText(resourceMap.getString("txCaminhoArquivo.text")); // NOI18N
        txCaminhoArquivo.setName("txCaminhoArquivo"); // NOI18N

        cbCarregaImagens.setText(resourceMap.getString("cbCarregaImagens.text")); // NOI18N
        cbCarregaImagens.setName("cbCarregaImagens"); // NOI18N

        cbSalvaErradas.setText(resourceMap.getString("cbSalvaErradas.text")); // NOI18N
        cbSalvaErradas.setName("cbSalvaErradas"); // NOI18N

        jButton4.setAction(actionMap.get("ConectaSocket")); // NOI18N
        jButton4.setLabel(resourceMap.getString("jButton4.label")); // NOI18N
        jButton4.setName("jButton4"); // NOI18N

        cbServidorImagens.setLabel(resourceMap.getString("cbServidorImagens.label")); // NOI18N
        cbServidorImagens.setName("cbServidorImagens"); // NOI18N
        cbServidorImagens.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                MudouServidorImagens(evt);
            }
        });

        txDiretorioImagens.setText(resourceMap.getString("txDiretorioImagens.text")); // NOI18N
        txDiretorioImagens.setName("txDiretorioImagens"); // NOI18N

        jButton5.setAction(actionMap.get("CarregaDir")); // NOI18N
        jButton5.setLabel(resourceMap.getString("jButton5.label")); // NOI18N
        jButton5.setName("jButton5"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        taArquivos.setColumns(20);
        taArquivos.setRows(5);
        taArquivos.setName("taArquivos"); // NOI18N
        jScrollPane2.setViewportView(taArquivos);

        cbCarregaImagensAjusta.setSelected(true);
        cbCarregaImagensAjusta.setText(resourceMap.getString("cbCarregaImagensAjusta.text")); // NOI18N
        cbCarregaImagensAjusta.setName("cbCarregaImagensAjusta"); // NOI18N

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txCaminhoArquivo, javax.swing.GroupLayout.PREFERRED_SIZE, 539, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, mainPanelLayout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 432, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, mainPanelLayout.createSequentialGroup()
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(55, 55, 55)
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(mainPanelLayout.createSequentialGroup()
                                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cbSalvaErradas)
                                            .addGroup(mainPanelLayout.createSequentialGroup()
                                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(cbCarregaImagens))
                                                .addGap(45, 45, 45)
                                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(cbCarregaImagensAjusta)
                                                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addGap(39, 39, 39)
                                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(cbServidorImagens)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, mainPanelLayout.createSequentialGroup()
                                .addComponent(txDiretorioImagens, javax.swing.GroupLayout.PREFERRED_SIZE, 539, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(687, 687, 687)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(219, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE))
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbCarregaImagens)
                            .addComponent(cbCarregaImagensAjusta))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbSalvaErradas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbServidorImagens)
                        .addGap(20, 20, 20)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txDiretorioImagens, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txCaminhoArquivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(267, 267, 267))
        );

        jButton1.getAccessibleContext().setAccessibleName(resourceMap.getString("jButton1.AccessibleContext.accessibleName")); // NOI18N

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setComponent(mainPanel);
        setMenuBar(menuBar);
    }// </editor-fold>//GEN-END:initComponents

    private void MudouServidorImagens(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_MudouServidorImagens
        cbCarregaImagens.setEnabled(!cbServidorImagens.isSelected());
    }//GEN-LAST:event_MudouServidorImagens

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox cbCarregaImagens;
    private javax.swing.JCheckBox cbCarregaImagensAjusta;
    private javax.swing.JCheckBox cbSalvaErradas;
    private javax.swing.JCheckBox cbServidorImagens;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JTextArea taArquivos;
    private javax.swing.JTextField txCaminhoArquivo;
    private javax.swing.JTextField txDiretorioImagens;
    // End of variables declaration//GEN-END:variables
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;
}
