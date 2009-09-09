package chaosrt.client;

import javax.swing.*;
import chaosrt.Statics;
import java.awt.*;
import java.awt.event.*;

public class frmClientOptionForm extends JFrame {
	
	public boolean ok_clicked = false;
	
  JPanel jPanel1 = new JPanel();
  public JTextField txtIPAddress = new JTextField();
  public JTextField txtName = new JTextField();
  JButton cmdOK = new JButton();
  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();

  public frmClientOptionForm() {
    try {
      jbInit();
      this.setSize(404, 222);
      
      ClientSettings settings = new ClientSettings();
      this.txtIPAddress.setText(settings.ip_address);
      this.txtName.setText(settings.player_name);
      
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    this.getContentPane().setLayout(null);
    jPanel1.setBounds(new Rectangle(9, 10, 383, 183));
    jPanel1.setLayout(null);
    this.getContentPane().setBackground(Color.orange);
    this.setForeground(Color.orange);
    this.setResizable(false);
    this.setState(Frame.NORMAL);
    this.setTitle(Statics.TITLE);
    txtIPAddress.setText("127.0.0.1");
    txtIPAddress.setBounds(new Rectangle(83, 18, 288, 27));
    txtName.setText("");
    txtName.setBounds(new Rectangle(83, 57, 289, 31));
    cmdOK.setBounds(new Rectangle(270, 140, 102, 31));
    cmdOK.setText("OK");
    cmdOK.addActionListener(new ClientOptionForm_cmdOK_actionAdapter(this));
    jLabel1.setText("Server");
    jLabel1.setBounds(new Rectangle(8, 17, 64, 31));
    jLabel2.setText("Name");
    jLabel2.setBounds(new Rectangle(9, 61, 63, 32));
    this.getContentPane().add(jPanel1, null);
    jPanel1.add(txtIPAddress, null);
    jPanel1.add(txtName, null);
    jPanel1.add(cmdOK, null);
    jPanel1.add(jLabel1, null);
    jPanel1.add(jLabel2, null);
  }

  void cmdOK_actionPerformed(ActionEvent e) {
	  ok_clicked = true;
      ClientSettings settings = new ClientSettings();
      settings.saveSettings(this.txtIPAddress.getText(), this.txtName.getText());
      
    this.setVisible(false);
  }

}

class ClientOptionForm_cmdOK_actionAdapter implements java.awt.event.ActionListener {
  frmClientOptionForm adaptee;

  ClientOptionForm_cmdOK_actionAdapter(frmClientOptionForm adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.cmdOK_actionPerformed(e);
  }
}
