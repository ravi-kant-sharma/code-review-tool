package main;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class AddFilePathDialog extends JDialog{
	public JFrame parent;
	public CodeReviewGUIClient crgc;
	public AddFilePathDialog(JFrame parent,CodeReviewGUIClient crgc){
		initComponents();
		this.parent = parent;
		this.crgc = crgc;
		this.setLocation(parent.getX()+20, parent.getY()+80);
		this.setVisible(true);
	}
	public void initComponents(){
		{
			filePathLabel = new JLabel();
			filePathTextField = new JTextField();
			okButton = new JButton();
			cancelButton = new JButton();
			
			this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			this.setAlwaysOnTop(true);
		}
		this.setSize(600, 170);
		Container addFilePathDialogContainer = this.getContentPane();
		addFilePathDialogContainer.setLayout(null);
		
		addFilePathDialogContainer.add(filePathLabel);
		addFilePathDialogContainer.add(filePathTextField);
		addFilePathDialogContainer.add(okButton);
		addFilePathDialogContainer.add(cancelButton);
		
		filePathLabel.setText("File Path");
		filePathLabel.setAlignmentY(CENTER_ALIGNMENT);
		okButton.setText("OK");
		cancelButton.setText("Cancel");
		
		filePathLabel.setBounds(10,25, 60, 40);
		filePathTextField.setBounds(60, 25, 500, 30);
		okButton.setBounds(200,80,60,30);
		cancelButton.setBounds(280,80,90,30);
		
		okButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				okButtonActionPerformed();
			}
		});
		
		cancelButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				cancelButtonActionPerformed();
			}
		});
		
	}
	
	private void okButtonActionPerformed(){
		if(filePathTextField.getText() != null && filePathTextField.getText().length() == 0){
			infoDialog = new InfoDialog(this,"Information", "Please Enter a file name");
		}else{
			File f = new File(filePathTextField.getText());
			if(f.isDirectory()){
				infoDialog = new InfoDialog(this,"Information", "Only files are allowed");
			}else if(!f.exists()){
				infoDialog = new InfoDialog(this,"Information", "Entered file doesn't exist.\r\nPlese enter valid file.");
			}else{
				String filePath = filePathTextField.getText();
				this.crgc.setFilePath(filePath);
				this.crgc.addFileInListFromFilePath(filePath);
				setVisible(false);
				dispose();
			}
		}
	}
	
	private void cancelButtonActionPerformed(){
		setVisible(false);
		dispose();
	}
	
	private JLabel filePathLabel;
	private JTextField filePathTextField;
	private JButton okButton;
	private JButton cancelButton;
	InfoDialog infoDialog;
	
}
