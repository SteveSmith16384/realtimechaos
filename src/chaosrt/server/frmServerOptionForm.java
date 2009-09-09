package chaosrt.server;

import chaosrt.Statics;
import javax.swing.*;
import java.awt.Color;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 */
public class frmServerOptionForm extends JFrame {

    public boolean ok_clicked = false;

    public frmServerOptionForm() {
        try {
            jbInit();
            this.setSize(200, 200);
            this.setTitle(Statics.TITLE + " Options");
            this.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.getContentPane().setBackground(Color.orange);
        this.getContentPane().setLayout(gridLayout2);
        jPanel1.setLayout(gridLayout1);
        txtMapWidth.setText("20");
        jLabel4.setToolTipText("");
        jLabel4.setText("Map Height");
        txtMapHeight.setText("20");
        gridLayout1.setColumns(2);
        gridLayout1.setRows(5);
        gridLayout2.setColumns(1);
        cmdOK.addActionListener(new frmServerOptionForm_cmdOK_actionAdapter(this));
        this.getContentPane().add(jPanel1);
        jLabel1.setText("Human Players");
        txtHumanPlayers.setText("2");
        jLabel2.setText("Computer Players");
        txtComputerPlayers.setText("0");
        jLabel3.setText("Map Width");
        optUseUndead.setText("Use Undead");
        jPanel1.add(jLabel1);
        jPanel1.add(txtHumanPlayers);
        jPanel1.add(jLabel2);
        jPanel1.add(txtComputerPlayers);
        jPanel1.add(jLabel3);
        jPanel1.add(txtMapWidth);
        jPanel1.add(jLabel4);
        jPanel1.add(txtMapHeight);

        jPanel1.add(optUseUndead);
        jPanel1.add(cmdOK);
        cmdOK.setText("OK");
    }

    JPanel jPanel1 = new JPanel();
    JButton cmdOK = new JButton();
    JLabel jLabel1 = new JLabel();
    public JTextField txtHumanPlayers = new JTextField();
    JLabel jLabel2 = new JLabel();
    public JTextField txtComputerPlayers = new JTextField();
    JLabel jLabel3 = new JLabel();
    public JCheckBox optUseUndead = new JCheckBox();
    public JTextField txtMapWidth = new JTextField();
    JLabel jLabel4 = new JLabel();
    public JTextField txtMapHeight = new JTextField();
    GridLayout gridLayout1 = new GridLayout();
    GridLayout gridLayout2 = new GridLayout();

    public void cmdOK_actionPerformed(ActionEvent e) {
        this.ok_clicked = true;
        this.setVisible(false);
    }

}


class frmServerOptionForm_cmdOK_actionAdapter implements ActionListener {
    private frmServerOptionForm adaptee;
    frmServerOptionForm_cmdOK_actionAdapter(frmServerOptionForm adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.cmdOK_actionPerformed(e);
    }
}
