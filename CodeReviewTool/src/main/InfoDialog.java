package main;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InfoDialog extends JDialog{

	public InfoDialog(Container parent, String tile, String message){
		initComponents();
		this.setLocation(parent.getX()+10,parent.getY()+10);
		messagePanel.add(new JLabel(message));
	}
	
	private void initComponents(){
		{
			messagePanel = new JPanel();
			okButton = new JButton();
			this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		}
		
		Container infoDialogContainer = this.getContentPane();
		infoDialogContainer.setLayout(null);
		this.setSize(300, 170);
		
		infoDialogContainer.add(messagePanel);
		infoDialogContainer.add(okButton);
		
		okButton.setText("Ok");
		
		messagePanel.setBounds(10, 10, 250, 50);
		okButton.setBounds(100, 70, 60, 30);
		
		okButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				setVisible(false);
				dispose();
			}
		});
		
		this.setAlwaysOnTop(true);
		this.setVisible(true);
	}
	
	private JPanel messagePanel;
	private JButton okButton;
}
