package com.POSGlobal.view;

import com.POSGlobal.controller.clsFixedSizeText;
import com.POSGlobal.controller.clsGlobalVarClass;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.*;

public class frmAlfaNumericKeyBoard extends javax.swing.JDialog
{

    private String textValue = "", passValue = "", inputMode;

    public frmAlfaNumericKeyBoard(java.awt.Frame parent, boolean modal, String flag, String Msg)
    {

        super(parent, modal);
        if (clsGlobalVarClass.gTouchScreenMode)
        {
            try
            {
                initComponents();

                capsON();
                funSetShortCutKeys();
                setLocationRelativeTo(parent);
                lblTitle.setText(Msg);
                StringBuilder sb = new StringBuilder(flag);
                inputMode = sb.substring(sb.length() - 1, sb.length());

                if (inputMode.equals("2"))
                {
                    txtBoard.setEchoChar('*');
                }
                super.setVisible(clsGlobalVarClass.gTouchScreenMode);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            super.setVisible(clsGlobalVarClass.gTouchScreenMode);
            this.dispose();
            clsGlobalVarClass.gKeyboardValue = "";
        }
    }

    private void funSetShortCutKeys()
    {
        btnclose.setMnemonic('c');
    }

    public frmAlfaNumericKeyBoard(java.awt.Frame parent, boolean modal, String txt, String flag, String Msg)
    {
        super(parent, modal);
        if (clsGlobalVarClass.gTouchScreenMode)
        {
            try
            {
                initComponents();
                capsON();
                passValue = txt;
                textValue = txt;
                lblTitle.setText(Msg);
                setLocationRelativeTo(parent);
                StringBuilder sb = new StringBuilder(flag);
                inputMode = sb.substring(sb.length() - 1, sb.length());
                if (inputMode.equals("2"))
                {
                    txtBoard.setEchoChar('*');
                }
                txtBoard.setText(txt);
                super.setVisible(clsGlobalVarClass.gTouchScreenMode);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            clsGlobalVarClass.gKeyboardValue = txt;
            super.setVisible(clsGlobalVarClass.gTouchScreenMode);
            this.dispose();
        }
    }

    /**
     * Ritesh 20 Feb 2015 This constructor is called when touch screen false but
     * still we need alfa numeric keyboard Example:- Free Flow modifier: -
     * entering name of free flow modifier on Make KOT And Direct Biller
     *
     * @param parent
     * @param modal
     * @param flag
     * @param Msg
     * @param required either true or false no effect of this value
     */
    public frmAlfaNumericKeyBoard(java.awt.Frame parent, boolean modal, String flag, String Msg, boolean required)
    {
        super(parent, modal);
        try
        {
            initComponents();
            capsON();
            setLocationRelativeTo(parent);
            lblTitle.setText(Msg);
            StringBuilder sb = new StringBuilder(flag);
            inputMode = sb.substring(sb.length() - 1, sb.length());

            if (inputMode.equals("2"))
            {
                txtBoard.setEchoChar('*');
            }
            super.setVisible(true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void capsON()
    {
        btnq.setText("Q");
        btnw.setText("W");
        btne.setText("E");
        btnr.setText("R");
        btnt.setText("T");
        btny.setText("Y");
        btnu.setText("U");
        btni.setText("I");
        btno.setText("O");
        btnp.setText("P");
        btna.setText("A");
        btns.setText("S");
        btnd.setText("D");
        btnf.setText("F");
        btnh.setText("H");
        btng.setText("G");
        btnj.setText("J");
        btnk.setText("K");
        btnl.setText("L");
        btnz.setText("Z");
        btnx.setText("X");
        btnc.setText("C");
        btnv.setText("V");
        btnb.setText("B");
        btnn.setText("N");
        btnm.setText("M");
    }

    public void capOff()
    {
        btnq.setText("q");
        btnw.setText("w");
        btne.setText("e");
        btnr.setText("r");
        btnt.setText("t");
        btny.setText("y");
        btnu.setText("u");
        btni.setText("i");
        btno.setText("o");
        btnp.setText("p");
        btna.setText("a");
        btns.setText("s");
        btnd.setText("d");
        btnf.setText("f");
        btng.setText("g");
        btnh.setText("h");
        btnj.setText("j");
        btnk.setText("k");
        btnl.setText("l");
        btnz.setText("z");
        btnx.setText("x");
        btnc.setText("c");
        btnv.setText("v");
        btnb.setText("b");
        btnn.setText("n");
        btnm.setText("m");
    }

    @Override
    public void setVisible(boolean b)
    {

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton19 = new javax.swing.JButton();
        panelMain = new javax.swing.JPanel();
        btnbackSpace = new javax.swing.JButton();
        btnequal = new javax.swing.JButton();
        btnhypen = new javax.swing.JButton();
        btn0 = new javax.swing.JButton();
        btn9 = new javax.swing.JButton();
        btn8 = new javax.swing.JButton();
        btn7 = new javax.swing.JButton();
        btn6 = new javax.swing.JButton();
        btn5 = new javax.swing.JButton();
        btn4 = new javax.swing.JButton();
        btn3 = new javax.swing.JButton();
        btn2 = new javax.swing.JButton();
        btn1 = new javax.swing.JButton();
        btnTilda = new javax.swing.JButton();
        btnenter = new javax.swing.JButton();
        btnSpace = new javax.swing.JButton();
        btns = new javax.swing.JButton();
        btnq = new javax.swing.JButton();
        btnx = new javax.swing.JButton();
        btng = new javax.swing.JButton();
        btnCtrl = new javax.swing.JButton();
        btnz = new javax.swing.JButton();
        btni = new javax.swing.JButton();
        btno = new javax.swing.JButton();
        btnw = new javax.swing.JButton();
        btnapostropes = new javax.swing.JButton();
        btne = new javax.swing.JButton();
        btnp = new javax.swing.JButton();
        btnu = new javax.swing.JButton();
        btny = new javax.swing.JButton();
        btnt = new javax.swing.JButton();
        btnr = new javax.swing.JButton();
        btnh = new javax.swing.JButton();
        btnTab = new javax.swing.JButton();
        btna = new javax.swing.JButton();
        btnb = new javax.swing.JButton();
        btnf = new javax.swing.JButton();
        btnc = new javax.swing.JButton();
        btnv = new javax.swing.JButton();
        btnd = new javax.swing.JButton();
        btnn = new javax.swing.JButton();
        btnj = new javax.swing.JButton();
        btnm = new javax.swing.JButton();
        btnk = new javax.swing.JButton();
        btncomma = new javax.swing.JButton();
        btnl = new javax.swing.JButton();
        btndot = new javax.swing.JButton();
        btnforwardslace = new javax.swing.JButton();
        btnRigthSqure = new javax.swing.JButton();
        btnclose = new javax.swing.JButton();
        btnsemecolon = new javax.swing.JButton();
        btnbackslace = new javax.swing.JButton();
        btnalt = new javax.swing.JButton();
        btnleftSquare = new javax.swing.JButton();
        btnCaps = new javax.swing.JToggleButton();
        txtBoard = new java.awt.TextField();
        btnleftShift = new javax.swing.JToggleButton();
        btnrightshift = new javax.swing.JToggleButton();
        lblBodyImg = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();

        jButton19.setText("jButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(800, 375));
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(null);

        panelMain.setBackground(new java.awt.Color(255, 255, 255));
        panelMain.setLayout(null);

        btnbackSpace.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnbackSpace.setForeground(new java.awt.Color(255, 255, 255));
        btnbackSpace.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyBoardBackspaceButtonDark.png"))); // NOI18N
        btnbackSpace.setText("BACK");
        btnbackSpace.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnbackSpace.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnbackSpace.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyBoardBackspaceButtonLight.png"))); // NOI18N
        btnbackSpace.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnbackSpaceMouseClicked(evt);
            }
        });
        panelMain.add(btnbackSpace);
        btnbackSpace.setBounds(670, 60, 120, 50);

        btnequal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnequal.setForeground(new java.awt.Color(255, 255, 255));
        btnequal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btnequal.setText("=");
        btnequal.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnequal.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnequal.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
        btnequal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnequalActionPerformed(evt);
            }
        });
        panelMain.add(btnequal);
        btnequal.setBounds(620, 60, 50, 50);

        btnhypen.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnhypen.setForeground(new java.awt.Color(255, 255, 255));
        btnhypen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btnhypen.setText("-");
        btnhypen.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnhypen.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnhypen.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
        btnhypen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnhypenActionPerformed(evt);
            }
        });
        panelMain.add(btnhypen);
        btnhypen.setBounds(570, 60, 50, 50);

        btn0.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn0.setForeground(new java.awt.Color(255, 255, 255));
        btn0.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btn0.setText("0");
        btn0.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btn0.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn0.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
        btn0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn0ActionPerformed(evt);
            }
        });
        panelMain.add(btn0);
        btn0.setBounds(520, 60, 50, 50);

        btn9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn9.setForeground(new java.awt.Color(255, 255, 255));
        btn9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btn9.setText("9");
        btn9.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btn9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn9.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
        btn9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn9ActionPerformed(evt);
            }
        });
        panelMain.add(btn9);
        btn9.setBounds(470, 60, 50, 50);

        btn8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn8.setForeground(new java.awt.Color(255, 255, 255));
        btn8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btn8.setText("8");
        btn8.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btn8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn8.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
        btn8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn8ActionPerformed(evt);
            }
        });
        panelMain.add(btn8);
        btn8.setBounds(420, 60, 50, 50);

        btn7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn7.setForeground(new java.awt.Color(255, 255, 255));
        btn7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btn7.setText("7");
        btn7.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btn7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn7.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
        btn7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn7ActionPerformed(evt);
            }
        });
        panelMain.add(btn7);
        btn7.setBounds(370, 60, 50, 50);

        btn6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn6.setForeground(new java.awt.Color(255, 255, 255));
        btn6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btn6.setText("6");
        btn6.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btn6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn6.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
        btn6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn6ActionPerformed(evt);
            }
        });
        panelMain.add(btn6);
        btn6.setBounds(320, 60, 50, 50);

        btn5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn5.setForeground(new java.awt.Color(255, 255, 255));
        btn5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btn5.setText("5");
        btn5.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btn5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn5.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
        btn5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn5ActionPerformed(evt);
            }
        });
        panelMain.add(btn5);
        btn5.setBounds(270, 60, 50, 50);

        btn4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn4.setForeground(new java.awt.Color(255, 255, 255));
        btn4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btn4.setText("4");
        btn4.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btn4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn4.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
        btn4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn4ActionPerformed(evt);
            }
        });
        panelMain.add(btn4);
        btn4.setBounds(220, 60, 50, 50);

        btn3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn3.setForeground(new java.awt.Color(255, 255, 255));
        btn3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btn3.setText("3");
        btn3.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btn3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn3.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
        btn3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn3ActionPerformed(evt);
            }
        });
        panelMain.add(btn3);
        btn3.setBounds(170, 60, 50, 50);

        btn2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn2.setForeground(new java.awt.Color(255, 255, 255));
        btn2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btn2.setText("2");
        btn2.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btn2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
        btn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn2ActionPerformed(evt);
            }
        });
        panelMain.add(btn2);
        btn2.setBounds(120, 60, 50, 50);

        btn1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn1.setForeground(new java.awt.Color(255, 255, 255));
        btn1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btn1.setText("1");
        btn1.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btn1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn1ActionPerformed(evt);
            }
        });
        panelMain.add(btn1);
        btn1.setBounds(70, 60, 50, 50);

        btnTilda.setBackground(new java.awt.Color(255, 255, 255));
        btnTilda.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnTilda.setForeground(new java.awt.Color(255, 255, 255));
        btnTilda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btnTilda.setText("`");
        btnTilda.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnTilda.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTilda.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardBtn2 .png"))); // NOI18N
        btnTilda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTildaActionPerformed(evt);
            }
        });
        panelMain.add(btnTilda);
        btnTilda.setBounds(20, 60, 50, 50);

        btnenter.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnenter.setForeground(new java.awt.Color(255, 255, 255));
        btnenter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyBoardEnterButtonDark.png"))); // NOI18N
        btnenter.setText("Enter");
        btnenter.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnenter.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnenter.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyBoardEnterButtonLight.png"))); // NOI18N
        btnenter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnenterMouseClicked(evt);
            }
        });
        btnenter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnenterActionPerformed(evt);
            }
        });
        panelMain.add(btnenter);
        btnenter.setBounds(670, 110, 120, 100);

        btnSpace.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSpace.setForeground(new java.awt.Color(255, 255, 255));
        btnSpace.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyBoardSpaceBar.png"))); // NOI18N
        btnSpace.setText("Space");
        btnSpace.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnSpace.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSpace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSpaceActionPerformed(evt);
            }
        });
        panelMain.add(btnSpace);
        btnSpace.setBounds(120, 260, 500, 50);

        btns.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btns.setForeground(new java.awt.Color(255, 255, 255));
        btns.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btns.setText("s");
        btns.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btns.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btns.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
        btns.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsActionPerformed(evt);
            }
        });
        panelMain.add(btns);
        btns.setBounds(170, 160, 50, 50);

        btnq.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnq.setForeground(new java.awt.Color(255, 255, 255));
        btnq.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btnq.setText("q");
        btnq.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnq.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnq.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
        btnq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnqActionPerformed(evt);
            }
        });
        panelMain.add(btnq);
        btnq.setBounds(120, 110, 50, 50);

        btnx.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnx.setForeground(new java.awt.Color(255, 255, 255));
        btnx.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btnx.setText("x");
        btnx.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnx.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnx.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
        btnx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnxActionPerformed(evt);
            }
        });
        panelMain.add(btnx);
        btnx.setBounds(170, 210, 50, 50);

        btng.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btng.setForeground(new java.awt.Color(255, 255, 255));
        btng.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btng.setText("g");
        btng.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btng.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btng.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
        btng.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btngActionPerformed(evt);
            }
        });
        panelMain.add(btng);
        btng.setBounds(320, 160, 50, 50);

        btnCtrl.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCtrl.setForeground(new java.awt.Color(255, 255, 255));
        btnCtrl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyBoardBackspaceButtonDark.png"))); // NOI18N
        btnCtrl.setText("Ctrl");
        btnCtrl.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnCtrl.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCtrl.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyBoardBackspaceButtonLight.png"))); // NOI18N
        panelMain.add(btnCtrl);
        btnCtrl.setBounds(20, 260, 100, 50);

        btnz.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnz.setForeground(new java.awt.Color(255, 255, 255));
        btnz.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btnz.setText("z");
        btnz.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnz.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnz.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
        btnz.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnzActionPerformed(evt);
            }
        });
        panelMain.add(btnz);
        btnz.setBounds(120, 210, 50, 50);

        btni.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btni.setForeground(new java.awt.Color(255, 255, 255));
        btni.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btni.setText("i");
        btni.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btni.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btni.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
        btni.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btniActionPerformed(evt);
            }
        });
        panelMain.add(btni);
        btni.setBounds(470, 110, 50, 50);

        btno.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btno.setForeground(new java.awt.Color(255, 255, 255));
        btno.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btno.setText("o");
        btno.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btno.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btno.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
        btno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnoActionPerformed(evt);
            }
        });
        panelMain.add(btno);
        btno.setBounds(520, 110, 50, 50);

        btnw.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnw.setForeground(new java.awt.Color(255, 255, 255));
        btnw.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btnw.setText("w");
        btnw.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnw.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnw.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
        btnw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnwActionPerformed(evt);
            }
        });
        panelMain.add(btnw);
        btnw.setBounds(170, 110, 50, 50);

        btnapostropes.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnapostropes.setForeground(new java.awt.Color(255, 255, 255));
        btnapostropes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btnapostropes.setText("'");
        btnapostropes.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnapostropes.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnapostropes.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
        btnapostropes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnapostropesActionPerformed(evt);
            }
        });
        panelMain.add(btnapostropes);
        btnapostropes.setBounds(620, 110, 50, 50);

        btne.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btne.setForeground(new java.awt.Color(255, 255, 255));
        btne.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btne.setText("e");
        btne.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btne.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btne.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
        btne.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneActionPerformed(evt);
            }
        });
        panelMain.add(btne);
        btne.setBounds(220, 110, 50, 50);

        btnp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnp.setForeground(new java.awt.Color(255, 255, 255));
        btnp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btnp.setText("p");
        btnp.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnp.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
        btnp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnpActionPerformed(evt);
            }
        });
        panelMain.add(btnp);
        btnp.setBounds(570, 110, 50, 50);

        btnu.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnu.setForeground(new java.awt.Color(255, 255, 255));
        btnu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btnu.setText("u");
        btnu.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnu.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnu.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
        btnu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnuActionPerformed(evt);
            }
        });
        panelMain.add(btnu);
        btnu.setBounds(420, 110, 50, 50);

        btny.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btny.setForeground(new java.awt.Color(255, 255, 255));
        btny.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btny.setText("y");
        btny.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btny.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btny.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
        btny.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnyActionPerformed(evt);
            }
        });
        panelMain.add(btny);
        btny.setBounds(370, 110, 50, 50);

        btnt.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnt.setForeground(new java.awt.Color(255, 255, 255));
        btnt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btnt.setText("t");
        btnt.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnt.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnt.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
        btnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btntActionPerformed(evt);
            }
        });
        panelMain.add(btnt);
        btnt.setBounds(320, 110, 50, 50);

        btnr.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnr.setForeground(new java.awt.Color(255, 255, 255));
        btnr.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btnr.setText("r");
        btnr.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnr.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnr.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
        btnr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnrActionPerformed(evt);
            }
        });
        panelMain.add(btnr);
        btnr.setBounds(270, 110, 50, 50);

        btnh.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnh.setForeground(new java.awt.Color(255, 255, 255));
        btnh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btnh.setText("h");
        btnh.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnh.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnh.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
        btnh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnhActionPerformed(evt);
            }
        });
        panelMain.add(btnh);
        btnh.setBounds(370, 160, 50, 50);

        btnTab.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnTab.setForeground(new java.awt.Color(255, 255, 255));
        btnTab.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyBoardBackspaceButtonDark.png"))); // NOI18N
        btnTab.setText("Tab");
        btnTab.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnTab.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTab.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyBoardBackspaceButtonLight.png"))); // NOI18N
        panelMain.add(btnTab);
        btnTab.setBounds(20, 110, 100, 50);

        btna.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btna.setForeground(new java.awt.Color(255, 255, 255));
        btna.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btna.setText("a");
        btna.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btna.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btna.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
        btna.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnaActionPerformed(evt);
            }
        });
        panelMain.add(btna);
        btna.setBounds(120, 160, 50, 50);

        btnb.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnb.setForeground(new java.awt.Color(255, 255, 255));
        btnb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btnb.setText("b");
        btnb.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnb.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnb.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
        btnb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbActionPerformed(evt);
            }
        });
        panelMain.add(btnb);
        btnb.setBounds(320, 210, 50, 50);

        btnf.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnf.setForeground(new java.awt.Color(255, 255, 255));
        btnf.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btnf.setText("f");
        btnf.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnf.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnf.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
        btnf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnfActionPerformed(evt);
            }
        });
        panelMain.add(btnf);
        btnf.setBounds(270, 160, 50, 50);

        btnc.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnc.setForeground(new java.awt.Color(255, 255, 255));
        btnc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btnc.setText("c");
        btnc.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnc.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnc.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
        btnc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncActionPerformed(evt);
            }
        });
        panelMain.add(btnc);
        btnc.setBounds(220, 210, 50, 50);

        btnv.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnv.setForeground(new java.awt.Color(255, 255, 255));
        btnv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btnv.setText("v");
        btnv.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnv.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnv.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
        btnv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnvActionPerformed(evt);
            }
        });
        panelMain.add(btnv);
        btnv.setBounds(270, 210, 50, 50);

        btnd.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnd.setForeground(new java.awt.Color(255, 255, 255));
        btnd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btnd.setText("d");
        btnd.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnd.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
        btnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btndActionPerformed(evt);
            }
        });
        panelMain.add(btnd);
        btnd.setBounds(220, 160, 50, 50);

        btnn.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnn.setForeground(new java.awt.Color(255, 255, 255));
        btnn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btnn.setText("n");
        btnn.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnn.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
        btnn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnnActionPerformed(evt);
            }
        });
        panelMain.add(btnn);
        btnn.setBounds(370, 210, 50, 50);

        btnj.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnj.setForeground(new java.awt.Color(255, 255, 255));
        btnj.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btnj.setText("j");
        btnj.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnj.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnj.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
        btnj.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnjActionPerformed(evt);
            }
        });
        panelMain.add(btnj);
        btnj.setBounds(420, 160, 50, 50);

        btnm.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnm.setForeground(new java.awt.Color(255, 255, 255));
        btnm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btnm.setText("m");
        btnm.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnm.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnm.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
        btnm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnmActionPerformed(evt);
            }
        });
        panelMain.add(btnm);
        btnm.setBounds(420, 210, 50, 50);

        btnk.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnk.setForeground(new java.awt.Color(255, 255, 255));
        btnk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btnk.setText("k");
        btnk.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnk.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnk.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
        btnk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnkActionPerformed(evt);
            }
        });
        panelMain.add(btnk);
        btnk.setBounds(470, 160, 50, 50);

        btncomma.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btncomma.setForeground(new java.awt.Color(255, 255, 255));
        btncomma.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btncomma.setText(",");
        btncomma.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btncomma.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btncomma.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
        btncomma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncommaActionPerformed(evt);
            }
        });
        panelMain.add(btncomma);
        btncomma.setBounds(470, 210, 50, 50);

        btnl.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnl.setForeground(new java.awt.Color(255, 255, 255));
        btnl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btnl.setText("l");
        btnl.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnl.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnl.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
        btnl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnlActionPerformed(evt);
            }
        });
        panelMain.add(btnl);
        btnl.setBounds(520, 160, 50, 50);

        btndot.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btndot.setForeground(new java.awt.Color(255, 255, 255));
        btndot.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btndot.setText(".");
        btndot.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btndot.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btndot.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
        btndot.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btndotMouseClicked(evt);
            }
        });
        btndot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btndotActionPerformed(evt);
            }
        });
        panelMain.add(btndot);
        btndot.setBounds(520, 210, 50, 50);

        btnforwardslace.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnforwardslace.setForeground(new java.awt.Color(255, 255, 255));
        btnforwardslace.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btnforwardslace.setText("/");
        btnforwardslace.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnforwardslace.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnforwardslace.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
        btnforwardslace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnforwardslaceActionPerformed(evt);
            }
        });
        panelMain.add(btnforwardslace);
        btnforwardslace.setBounds(570, 210, 50, 50);

        btnRigthSqure.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnRigthSqure.setForeground(new java.awt.Color(255, 255, 255));
        btnRigthSqure.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btnRigthSqure.setText("]");
        btnRigthSqure.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnRigthSqure.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRigthSqure.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
        btnRigthSqure.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRigthSqureActionPerformed(evt);
            }
        });
        panelMain.add(btnRigthSqure);
        btnRigthSqure.setBounds(620, 160, 50, 50);

        btnclose.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnclose.setForeground(new java.awt.Color(255, 255, 255));
        btnclose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyBoardBackspaceButtonDark.png"))); // NOI18N
        btnclose.setText("Close");
        btnclose.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnclose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnclose.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyBoardBackspaceButtonLight.png"))); // NOI18N
        btnclose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btncloseMouseClicked(evt);
            }
        });
        btnclose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncloseActionPerformed(evt);
            }
        });
        panelMain.add(btnclose);
        btnclose.setBounds(700, 260, 90, 50);

        btnsemecolon.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnsemecolon.setForeground(new java.awt.Color(255, 255, 255));
        btnsemecolon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btnsemecolon.setText(";");
        btnsemecolon.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
        btnsemecolon.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnsemecolon.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
        btnsemecolon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsemecolonActionPerformed(evt);
            }
        });
        panelMain.add(btnsemecolon);
        btnsemecolon.setBounds(670, 210, 50, 50);

        btnbackslace.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnbackslace.setForeground(new java.awt.Color(255, 255, 255));
        btnbackslace.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
        btnbackslace.setText("\\");
            btnbackslace.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
            btnbackslace.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            btnbackslace.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
            btnbackslace.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnbackslaceActionPerformed(evt);
                }
            });
            panelMain.add(btnbackslace);
            btnbackslace.setBounds(620, 210, 50, 50);

            btnalt.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
            btnalt.setForeground(new java.awt.Color(255, 255, 255));
            btnalt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyBoardBackspaceButtonDark.png"))); // NOI18N
            btnalt.setText("Clear");
            btnalt.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
            btnalt.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            btnalt.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyBoardBackspaceButtonLight.png"))); // NOI18N
            btnalt.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    btnaltMouseClicked(evt);
                }
            });
            btnalt.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnaltActionPerformed(evt);
                }
            });
            panelMain.add(btnalt);
            btnalt.setBounds(620, 260, 80, 50);

            btnleftSquare.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
            btnleftSquare.setForeground(new java.awt.Color(255, 255, 255));
            btnleftSquare.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonDark.png"))); // NOI18N
            btnleftSquare.setText("[");
            btnleftSquare.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
            btnleftSquare.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            btnleftSquare.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyboardButtonLight.png"))); // NOI18N
            btnleftSquare.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnleftSquareActionPerformed(evt);
                }
            });
            panelMain.add(btnleftSquare);
            btnleftSquare.setBounds(570, 160, 50, 50);

            btnCaps.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
            btnCaps.setForeground(new java.awt.Color(255, 255, 255));
            btnCaps.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyBoardBackspaceButtonDark.png"))); // NOI18N
            btnCaps.setSelected(true);
            btnCaps.setText("Caps");
            btnCaps.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 102), new java.awt.Color(0, 102, 255)));
            btnCaps.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            btnCaps.setPreferredSize(new java.awt.Dimension(110, 50));
            btnCaps.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyBoardBackspaceButtonLight.png"))); // NOI18N
            btnCaps.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnCapsActionPerformed(evt);
                }
            });
            panelMain.add(btnCaps);
            btnCaps.setBounds(20, 160, 100, 50);

            txtBoard.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
            txtBoard.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    txtBoardActionPerformed(evt);
                }
            });
            txtBoard.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    txtBoardKeyPressed(evt);
                }
            });
            panelMain.add(txtBoard);
            txtBoard.setBounds(110, 0, 600, 50);

            btnleftShift.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
            btnleftShift.setForeground(new java.awt.Color(255, 255, 255));
            btnleftShift.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyBoardBackspaceButtonDark.png"))); // NOI18N
            btnleftShift.setText("Shift");
            btnleftShift.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 0), new java.awt.Color(51, 51, 255)));
            btnleftShift.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            btnleftShift.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyBoardBackspaceButtonLight.png"))); // NOI18N
            btnleftShift.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnleftShiftActionPerformed(evt);
                }
            });
            panelMain.add(btnleftShift);
            btnleftShift.setBounds(20, 210, 100, 50);

            btnrightshift.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
            btnrightshift.setForeground(new java.awt.Color(255, 255, 255));
            btnrightshift.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyBoardBackspaceButtonDark.png"))); // NOI18N
            btnrightshift.setText("Shift");
            btnrightshift.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 255)));
            btnrightshift.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            btnrightshift.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgKeyBoardBackspaceButtonLight.png"))); // NOI18N
            btnrightshift.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnrightshiftActionPerformed(evt);
                }
            });
            panelMain.add(btnrightshift);
            btnrightshift.setBounds(720, 210, 70, 50);

            lblBodyImg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/POSGlobal/images/imgBGJPOS.png"))); // NOI18N
            panelMain.add(lblBodyImg);
            lblBodyImg.setBounds(0, 0, 800, 330);

            getContentPane().add(panelMain);
            panelMain.setBounds(0, 54, 801, 319);

            lblTitle.setBackground(new java.awt.Color(255, 255, 255));
            lblTitle.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
            lblTitle.setForeground(new java.awt.Color(153, 0, 0));
            lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            getContentPane().add(lblTitle);
            lblTitle.setBounds(200, 11, 416, 37);

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void btn5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn5ActionPerformed
        // TODO add your handling code here:
        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btn5.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btn5.getActionCommand());
            }
            else
            {
                //textValue+=btn5.getActionCommand();                
                txtBoard.setText(txtBoard.getText() + btn5.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btn5ActionPerformed

    private void btnequalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnequalActionPerformed
        // TODO add your handling code here:
        try
        {
            if (inputMode.equals("2"))
            {
                textValue += btnequal.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btnequal.getActionCommand());
            }
            else
            {
                textValue += btnequal.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btnequal.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btnequalActionPerformed

    private void btniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btniActionPerformed
        // TODO add your handling code here:
        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btni.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btni.getActionCommand());
            }
            else
            {
                textValue += btni.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btni.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btniActionPerformed

    private void btnTildaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTildaActionPerformed
        // TODO add your handling code here:
        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btnTilda.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btnTilda.getActionCommand());
            }
            else
            {
                textValue += btnTilda.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btnTilda.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btnTildaActionPerformed

    private void btn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn1ActionPerformed
        // TODO add your handling code here:
        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btn1.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btn1.getActionCommand());
            }
            else
            {
                textValue += btn1.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btn1.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btn1ActionPerformed

    private void btn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn2ActionPerformed
        // TODO add your handling code here:
        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btn2.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btn2.getActionCommand());
            }
            else
            {
                textValue += btn2.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btn2.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btn2ActionPerformed

    private void btn3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn3ActionPerformed
        // TODO add your handling code here:
        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btn3.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btn3.getActionCommand());
            }
            else
            {
                textValue += btn3.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btn3.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btn3ActionPerformed

    private void btn4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn4ActionPerformed
        // TODO add your handling code here:
        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btn4.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btn4.getActionCommand());
            }
            else
            {
                textValue += btn4.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btn4.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btn4ActionPerformed

    private void btn6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn6ActionPerformed
        // TODO add your handling code here:
        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btn6.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btn6.getActionCommand());
            }
            else
            {
                textValue += btn6.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btn6.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btn6ActionPerformed

    private void btn7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn7ActionPerformed
        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btn7.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btn7.getActionCommand());
            }
            else
            {
                textValue += btn7.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btn7.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btn7ActionPerformed

    private void btn8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn8ActionPerformed
        // TODO add your handling code here:
        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btn8.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btn8.getActionCommand());
            }
            else
            {
                textValue += btn8.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btn8.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btn8ActionPerformed

    private void btn9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn9ActionPerformed
        // TODO add your handling code here:
        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btn9.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btn9.getActionCommand());
            }
            else
            {
                textValue += btn9.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btn9.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btn9ActionPerformed

    private void btn0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn0ActionPerformed

        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btn0.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btn0.getActionCommand());
            }
            else
            {
                textValue += btn0.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btn0.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btn0ActionPerformed

    private void btnhypenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnhypenActionPerformed
        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btnhypen.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btnhypen.getActionCommand());
            }
            else
            {
                textValue += btnhypen.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btnhypen.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btnhypenActionPerformed

    private void btnqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnqActionPerformed
        // TODO add your handling code here:
        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btnq.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btnq.getActionCommand());
            }
            else
            {
                textValue += btnq.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btnq.getActionCommand());

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btnqActionPerformed

    private void btnwActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnwActionPerformed
        // TODO add your handling code here:
        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btnw.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btnw.getActionCommand());
            }
            else
            {
                textValue += btnw.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btnw.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btnwActionPerformed

    private void btneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneActionPerformed
        // TODO add your handling code here:
        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btne.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btne.getActionCommand());
            }
            else
            {
                textValue += btne.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btne.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btneActionPerformed

    private void btnrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnrActionPerformed
        // TODO add your handling code here:
        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btnr.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btnr.getActionCommand());
            }
            else
            {
                textValue += btnr.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btnr.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btnrActionPerformed

    private void btntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btntActionPerformed
        // TODO add your handling code here:
        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btnt.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btnt.getActionCommand());
            }
            else
            {
                textValue += btnt.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btnt.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btntActionPerformed

    private void btnyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnyActionPerformed
        // TODO add your handling code here:
        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btny.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btny.getActionCommand());
            }
            else
            {
                textValue += btny.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btny.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btnyActionPerformed

    private void btnuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnuActionPerformed
        // TODO add your handling code here:
        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btnu.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btnu.getActionCommand());
            }
            else
            {
                textValue += btnu.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btnu.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btnuActionPerformed

    private void btnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnoActionPerformed
        // TODO add your handling code here:
        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btno.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btno.getActionCommand());
            }
            else
            {
                textValue += btno.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btno.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btnoActionPerformed

    private void btnpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnpActionPerformed
        // TODO add your handling code here:
        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btnp.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btnp.getActionCommand());
            }
            else
            {
                textValue += btnp.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btnp.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btnpActionPerformed

    private void btnaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaActionPerformed
        // TODO add your handling code here:
        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btna.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btna.getActionCommand());
            }
            else
            {
                textValue += btna.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btna.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btnaActionPerformed

    private void btnsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsActionPerformed
        // TODO add your handling code here:
        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btns.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btns.getActionCommand());
            }
            else
            {
                textValue += btns.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btns.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btnsActionPerformed

    private void btndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btndActionPerformed
        // TODO add your handling code here:
        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btnd.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btnd.getActionCommand());
            }
            else
            {
                textValue += btnd.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btnd.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btndActionPerformed

    private void btnfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnfActionPerformed
        // TODO add your handling code here:
        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btnf.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btnf.getActionCommand());
            }
            else
            {
                textValue += btnf.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btnf.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btnfActionPerformed

    private void btngActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btngActionPerformed
        // TODO add your handling code here:
        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btng.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btng.getActionCommand());
            }
            else
            {
                textValue += btng.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btng.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btngActionPerformed

    private void btnhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnhActionPerformed
        // TODO add your handling code here:
        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btnh.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btnh.getActionCommand());
            }
            else
            {
                textValue += btnh.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btnh.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btnhActionPerformed

    private void btnjActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnjActionPerformed
        // TODO add your handling code here:
        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btnj.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btnj.getActionCommand());
            }
            else
            {
                textValue += btnj.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btnj.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btnjActionPerformed

    private void btnkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnkActionPerformed
        // TODO add your handling code here:
        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btnk.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btnk.getActionCommand());
            }
            else
            {
                textValue += btnk.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btnk.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btnkActionPerformed

    private void btnlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnlActionPerformed
        // TODO add your handling code here:
        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btnl.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btnl.getActionCommand());
            }
            else
            {
                textValue += btnl.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btnl.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btnlActionPerformed

    private void btnzActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnzActionPerformed
        // TODO add your handling code here:
        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btnz.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btnz.getActionCommand());
            }
            else
            {
                textValue += btnz.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btnz.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btnzActionPerformed

    private void btnxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnxActionPerformed
        // TODO add your handling code here:
        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btnx.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btnx.getActionCommand());
            }
            else
            {
                textValue += btnx.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btnx.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btnxActionPerformed

    private void btncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncActionPerformed
        // TODO add your handling code here:
        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btnc.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btnc.getActionCommand());
            }
            else
            {
                textValue += btnc.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btnc.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btncActionPerformed

    private void btnvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnvActionPerformed
        // TODO add your handling code here:
        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btnv.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btnv.getActionCommand());
            }
            else
            {
                textValue += btnv.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btnv.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btnvActionPerformed

    private void btnbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbActionPerformed

        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btnb.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btnb.getActionCommand());
            }
            else
            {
                textValue += btnb.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btnb.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btnbActionPerformed

    private void btnnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnnActionPerformed

        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btnn.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btnn.getActionCommand());
            }
            else
            {
                textValue += btnn.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btnn.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btnnActionPerformed

    private void btnmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmActionPerformed

        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btnm.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btnm.getActionCommand());
            }
            else
            {
                textValue += btnm.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btnm.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btnmActionPerformed

    private void btnSpaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSpaceActionPerformed
        try
        {
            textValue += " ";
            txtBoard.setText(txtBoard.getText() + " ");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnSpaceActionPerformed

    private void btnenterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnenterMouseClicked
        pressEnterBtn();
    }//GEN-LAST:event_btnenterMouseClicked

    public void pressEnterBtn()
    {
        clsGlobalVarClass.gKeyboardValue = txtBoard.getText().trim();
        dispose();
    }

    private void btncloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btncloseMouseClicked
        // TODO add your handling code here:
        clsGlobalVarClass.gKeyboardValue = passValue;
        dispose();
    }//GEN-LAST:event_btncloseMouseClicked

    private void btnbackSpaceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnbackSpaceMouseClicked
        // TODO add your handling code here:
        try
        {
            if (txtBoard.getText().length() > 0)
            {
                StringBuilder sb = new StringBuilder(txtBoard.getText());
                sb.delete(txtBoard.getText().length() - 1, txtBoard.getText().length());
                String s = sb.toString();
                txtBoard.setText(s);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }//GEN-LAST:event_btnbackSpaceMouseClicked

    private void btndotMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btndotMouseClicked
        // TODO add your handling code here:       
    }//GEN-LAST:event_btndotMouseClicked

    private void btndotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btndotActionPerformed
        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btndot.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btnn.getActionCommand());
            }
            else
            {
                textValue += btndot.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btndot.getActionCommand());
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btndotActionPerformed

    private void btnCapsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapsActionPerformed
        // TODO add your handling code here:
        JToggleButton btn = (JToggleButton) evt.getSource();
        if (btn.isSelected())
        {
            capsON();

        }
        else
        {
            capOff();
        }
    }//GEN-LAST:event_btnCapsActionPerformed

    private void txtBoardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBoardActionPerformed


    }//GEN-LAST:event_txtBoardActionPerformed

    private void btnaltMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnaltMouseClicked
        txtBoard.setText("");
    }//GEN-LAST:event_btnaltMouseClicked

    private void btnRigthSqureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRigthSqureActionPerformed
        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btnRigthSqure.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btnRigthSqure.getActionCommand());
            }
            else
            {
                textValue += btnRigthSqure.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btnRigthSqure.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btnRigthSqureActionPerformed

    private void btnleftSquareActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnleftSquareActionPerformed
        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btnleftSquare.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btnleftSquare.getActionCommand());
            }
            else
            {
                textValue += btnleftSquare.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btnleftSquare.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btnleftSquareActionPerformed

    private void btnforwardslaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnforwardslaceActionPerformed
        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btnforwardslace.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btnforwardslace.getActionCommand());
            }
            else
            {
                textValue += btnforwardslace.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btnforwardslace.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btnforwardslaceActionPerformed

    private void btnbackslaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbackslaceActionPerformed
        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btnbackslace.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btnbackslace.getActionCommand());
            }
            else
            {
                textValue += btnbackslace.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btnbackslace.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btnbackslaceActionPerformed

    private void btnsemecolonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsemecolonActionPerformed
        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btnsemecolon.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btnsemecolon.getActionCommand());
            }
            else
            {
                textValue += btnsemecolon.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btnsemecolon.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btnsemecolonActionPerformed

    private void btncommaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncommaActionPerformed
        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btncomma.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btncomma.getActionCommand());
            }
            else
            {
                textValue += btncomma.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btncomma.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btncommaActionPerformed

    private void btnleftShiftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnleftShiftActionPerformed
        try
        {
            Toolkit.getDefaultToolkit().beep();
            if (btnleftShift.isSelected())
            {

                btnleftShift.setFont(btnleftShift.getFont().deriveFont(Font.BOLD, 14));
                btnrightshift.setFont(btnrightshift.getFont().deriveFont(Font.BOLD, 14));
                btnrightshift.setSelected(true);
                capsON();
                pressShiftbtnChangeCaption();
            }
            else
            {
                btnleftShift.setFont(btnleftShift.getFont().deriveFont(Font.BOLD, 12));
                btnrightshift.setFont(btnrightshift.getFont().deriveFont(Font.BOLD, 12));
                //btnrightshift.setFont(btnrightshift.getFont().deriveFont(Font.LAYOUT_LEFT_TO_RIGHT));
                capOff();

                relesedShiftbtnChangeCaption();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnleftShiftActionPerformed

    private void btnrightshiftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnrightshiftActionPerformed
        try
        {
            Toolkit.getDefaultToolkit().beep();
            if (btnrightshift.isSelected())
            {
                btnleftShift.setFont(btnleftShift.getFont().deriveFont(Font.BOLD, 14));
                btnrightshift.setFont(btnrightshift.getFont().deriveFont(Font.BOLD, 14));
                btnleftShift.setSelected(true);
                capsON();
                pressShiftbtnChangeCaption();
            }
            else
            {
                btnrightshift.setFont(btnrightshift.getFont().deriveFont(Font.BOLD, 12));
                btnleftShift.setFont(btnleftShift.getFont().deriveFont(Font.BOLD, 12));
                //btnrightshift.setFont(btnrightshift.getFont().deriveFont(Font.LAYOUT_LEFT_TO_RIGHT));
                capOff();
                relesedShiftbtnChangeCaption();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnrightshiftActionPerformed

    private void btnaltActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaltActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnaltActionPerformed

    private void btnapostropesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnapostropesActionPerformed
        try
        {
            if (inputMode.equals("2"))
            {
                textValue += "*";
                passValue += btnapostropes.getActionCommand();
                txtBoard.setEchoChar('*');
                txtBoard.setText(txtBoard.getText() + btnapostropes.getActionCommand());
            }
            else
            {
                textValue += btnapostropes.getActionCommand();
                txtBoard.setText(txtBoard.getText() + btnapostropes.getActionCommand());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            unselectedShiftbtn();
        }
    }//GEN-LAST:event_btnapostropesActionPerformed

    private void btnenterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnenterActionPerformed
        pressEnterBtn();
    }//GEN-LAST:event_btnenterActionPerformed

    private void txtBoardKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBoardKeyPressed
        try
        {
            if (evt.getKeyCode() == 10)
            {
                pressEnterBtn();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_txtBoardKeyPressed

    private void btncloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncloseActionPerformed
        // TODO add your handling code here:
        clsGlobalVarClass.gKeyboardValue = passValue;
        dispose();
    }//GEN-LAST:event_btncloseActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(frmAlfaNumericKeyBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(frmAlfaNumericKeyBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(frmAlfaNumericKeyBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(frmAlfaNumericKeyBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                //new FrmKeyBoard(this,true,"UserReg1",new FrmUserRegistration(),"Msg").setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn0;
    private javax.swing.JButton btn1;
    private javax.swing.JButton btn2;
    private javax.swing.JButton btn3;
    private javax.swing.JButton btn4;
    private javax.swing.JButton btn5;
    private javax.swing.JButton btn6;
    private javax.swing.JButton btn7;
    private javax.swing.JButton btn8;
    private javax.swing.JButton btn9;
    private javax.swing.JToggleButton btnCaps;
    private javax.swing.JButton btnCtrl;
    private javax.swing.JButton btnRigthSqure;
    private javax.swing.JButton btnSpace;
    private javax.swing.JButton btnTab;
    private javax.swing.JButton btnTilda;
    private javax.swing.JButton btna;
    private javax.swing.JButton btnalt;
    private javax.swing.JButton btnapostropes;
    private javax.swing.JButton btnb;
    private javax.swing.JButton btnbackSpace;
    private javax.swing.JButton btnbackslace;
    private javax.swing.JButton btnc;
    private javax.swing.JButton btnclose;
    private javax.swing.JButton btncomma;
    private javax.swing.JButton btnd;
    private javax.swing.JButton btndot;
    private javax.swing.JButton btne;
    private javax.swing.JButton btnenter;
    private javax.swing.JButton btnequal;
    private javax.swing.JButton btnf;
    private javax.swing.JButton btnforwardslace;
    private javax.swing.JButton btng;
    private javax.swing.JButton btnh;
    private javax.swing.JButton btnhypen;
    private javax.swing.JButton btni;
    private javax.swing.JButton btnj;
    private javax.swing.JButton btnk;
    private javax.swing.JButton btnl;
    private javax.swing.JToggleButton btnleftShift;
    private javax.swing.JButton btnleftSquare;
    private javax.swing.JButton btnm;
    private javax.swing.JButton btnn;
    private javax.swing.JButton btno;
    private javax.swing.JButton btnp;
    private javax.swing.JButton btnq;
    private javax.swing.JButton btnr;
    private javax.swing.JToggleButton btnrightshift;
    private javax.swing.JButton btns;
    private javax.swing.JButton btnsemecolon;
    private javax.swing.JButton btnt;
    private javax.swing.JButton btnu;
    private javax.swing.JButton btnv;
    private javax.swing.JButton btnw;
    private javax.swing.JButton btnx;
    private javax.swing.JButton btny;
    private javax.swing.JButton btnz;
    private javax.swing.JButton jButton19;
    private javax.swing.JLabel lblBodyImg;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel panelMain;
    private java.awt.TextField txtBoard;
    // End of variables declaration//GEN-END:variables

    public void pressShiftbtnChangeCaption()
    {
        try
        {
            btnTilda.setText("~");
            btn1.setText("!");
            btn2.setText("@");
            btn3.setText("#");
            btn4.setText("$");
            btn5.setText("%");
            btn6.setText("^");
            btn7.setText("&");
            btn8.setText("*");
            btn9.setText("(");
            btn0.setText(")");
            btnhypen.setText("_");
            btnequal.setText("+");
            btncomma.setText("<");
            btndot.setText(">");
            btnleftSquare.setText("{");
            btnRigthSqure.setText("}");
            btnbackslace.setText("|");
            btnforwardslace.setText("?");
            btnsemecolon.setText(":");
            btnapostropes.setText("\"");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void relesedShiftbtnChangeCaption()
    {
        try
        {
            btnTilda.setText("`");
            btn1.setText("1");
            btn2.setText("2");
            btn3.setText("3");
            btn4.setText("4");
            btn5.setText("5");
            btn6.setText("6");
            btn7.setText("7");
            btn8.setText("8");
            btn9.setText("9");
            btn0.setText("0");
            btnhypen.setText("-");
            btnequal.setText("=");
            btncomma.setText(",");
            btndot.setText(".");
            btnleftSquare.setText("[");
            btnRigthSqure.setText("]");
            btnbackslace.setText("\\");
            btnforwardslace.setText("/");
            btnsemecolon.setText(";");
            btnapostropes.setText("'");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void unselectedShiftbtn()
    {
        try
        {
            if (btnleftShift.isSelected() && btnrightshift.isSelected())
            {
                btnleftShift.setSelected(false);
                btnrightshift.setSelected(false);
                relesedShiftbtnChangeCaption();
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
