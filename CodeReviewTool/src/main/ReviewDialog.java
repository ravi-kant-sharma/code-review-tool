package main;

import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class ReviewDialog extends JDialog{
	CodeReviewGUIClient crguc;
	String jarLocation;
	JFrame parentFrame;
	public ReviewDialog(Frame parent,CodeReviewGUIClient crguc){
		this.crguc = crguc;
		this.parentFrame = (JFrame)parent;
		initComponents();
		this.setLocation(parent.getX()+100, parent.getY()+100);
		this.setAlwaysOnTop(true);
		this.setVisible(true);
		this.jarLocation = crguc.jarLocation;
	}
	
	private void initComponents(){
		reviewNamelabel = new JLabel();
		reviewNameTextField = new JTextField();
		radioButtonGroup = new ButtonGroup();
		newReviewButton = new JRadioButton();
		existingReviewRadioButton = new JRadioButton();
		reviewNumberTextField = new JTextField();
		okButton = new JButton();
		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		Container dialogContaniner = this.getContentPane();
		dialogContaniner.setLayout(null);
		
		this.setSize(310,200);
		
		radioButtonGroup.add(newReviewButton);
		radioButtonGroup.add(existingReviewRadioButton);
		dialogContaniner.add(reviewNamelabel);
		dialogContaniner.add(reviewNameTextField);
		dialogContaniner.add(newReviewButton);
		dialogContaniner.add(existingReviewRadioButton);
		dialogContaniner.add(reviewNumberTextField);
		dialogContaniner.add(okButton);
		
		reviewNamelabel.setText("Review Name");
		newReviewButton.setText("New Review");
		existingReviewRadioButton.setText("Existing Review");
		
		reviewNamelabel.setBounds(10,10,90,30);
		reviewNameTextField.setBounds(110,10,170,30);
		newReviewButton.setBounds(10,50,150,20);
		newReviewButton.setSelected(true);
		existingReviewRadioButton.setBounds(130,50,150,20);
		
		reviewNumberTextField.setBounds(130,80,120,25);
//		reviewNumberTextField.setEnabled(false);
		
		okButton.setText("Ok");
		okButton.setBounds(100,125,60,30);
		
		okButton.addActionListener(new ActionListener(){
			@Override
			   public void actionPerformed(ActionEvent e){
				   okButtonActionPerformed();
			   }
			});
		}
	
	private void okButtonActionPerformed(){
		this.dispose();
		String reviewNum;
		if(newReviewButton.isSelected()){
			reviewNum = "new";
		}else{
			reviewNum = reviewNumberTextField.getText();
		}
		
		File f = new File(jarLocation+ "\\reviews\\"+reviewNameTextField.getText());
		
		ArrayList<String> fileList1 = this.crguc.fileArrayListDev;
		ArrayList<String> fileList2 = this.crguc.fileArrayListInt;
		
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < fileList1.size(); i++) {
			sb.append(fileList1.get(i)).append("\r\n");
		}
		
		try {
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(sb.toString().getBytes());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try{
			String cmd = "cmd /c rmdir /S /Q "+ jarLocation+ "\\files-before";
			Runtime.getRuntime().exec(cmd);
			cmd  = "cmd /c rmdir /S /Q "+ jarLocation + "\\files-after";
			Runtime.getRuntime().exec(cmd);
			File fileBefore = new File( jarLocation+ "\\files-before");
			File fileAfter = new File( jarLocation+ "\\files-after");
			boolean directoryCreatedFilesAfter = false;
			boolean directoryCreatedFilesBefore = false;
			
			while(!(directoryCreatedFilesAfter  && directoryCreatedFilesBefore)){
				if(!fileBefore.exists() && !directoryCreatedFilesBefore){
					fileBefore.mkdir();
					directoryCreatedFilesBefore = true;
				}
				if(!fileAfter.exists() && !directoryCreatedFilesAfter){
					fileAfter.mkdir();
					directoryCreatedFilesAfter = true;
				}
			}
			copyFilesWithCreatingDirectories(fileList1,fileList2);
		}catch(Exception e){
			e.printStackTrace();
		}
		
			String str1 = System.getenv("JAVA_HOME");
		    str1 = convertPath(str1);
		    try
		    {
		      String command = this.crguc.properties.getProperty("ccollab_path")+ " adddiffs --relative " + reviewNum + " '" + jarLocation+"\\files-before" + "' '" + jarLocation+"\\files-after" + "'";
//		      String command = this.crguc.properties.getProperty("ccollab_path")+ " --editor '" + str1 + "\\bin\\java.exe -jar CodeReviewUploadSelect.jar'" +" adddiffs --relative " + reviewNum + " '" + jarLocation+"\\files-before" + "' '" + jarLocation+"\\files-after" + "'";
		      System.out.println(command);
		      Process localProcess = Runtime.getRuntime().exec(command);
		      Thread.sleep(2000);
		      this.parentFrame.dispose();
		      //
		      /*
		      BufferedInputStream localBufferedInputStream1 = new BufferedInputStream(localProcess.getInputStream());
		      BufferedInputStream localBufferedInputStream2 = new BufferedInputStream(localProcess.getErrorStream());
		      StringBuffer localStringBuffer = new StringBuffer();
		      int i = -1;
		      long l = -1L;
		      try
		      {
		        byte[] arrayOfByte = new byte[1000];
		        for (;;)
		        {
		          Thread.yield();
		          int j = localBufferedInputStream1.available();
		          if (j > 0)
		          {
		            if (j > arrayOfByte.length) {
		              j = arrayOfByte.length;
		            }
		            localBufferedInputStream1.read(arrayOfByte, 0, j);
		            System.out.write(arrayOfByte, 0, j);
		            localStringBuffer.append(new String(arrayOfByte, 0, j));
		            if (i == -1) {
		              i = localStringBuffer.indexOf("Opening text editor");
		            }
		            l = System.currentTimeMillis();
		          }
		          else
		          {
		            if ((l != -1L) && (System.currentTimeMillis() - l > 1000L) && (i != -1) && (localStringBuffer.indexOf(" deleted.", i) != -1)) {
		              break;
		            }
		          }
		          try
		          {
		            localProcess.exitValue();
		            while ((j = localBufferedInputStream2.available()) > 0)
		            {
		              if (j > arrayOfByte.length) {
		                j = arrayOfByte.length;
		              }
		              localBufferedInputStream2.read(arrayOfByte, 0, j);
		              System.err.write(arrayOfByte, 0, j);
		            }
		          }
		          catch (Exception localException3) {}
		        }
		      }
		      catch (Exception localException2) {}
		      */
		      //
		    }
		    catch (Exception localException1)
		    {
		      throw new RuntimeException(localException1);
		    }
		}
	private void copyFilesWithCreatingDirectories(ArrayList<String> devFileList,ArrayList<String> intFileList){
		if(devFileList != null && devFileList.size() > 0){
			for(int i = 0; i < devFileList.size();i++){
				String devFilePath = devFileList.get(i);
				String intFilePath = intFileList.get(i);
				String[] devFileDirStruct = devFilePath.split("\\\\");
				String[] intFileDirStruct = null;
				if(intFilePath != null){
					intFileDirStruct = intFilePath.split("\\\\");
				}
				if(devFileDirStruct != null && intFileDirStruct != null && devFileDirStruct.length >0 && intFileDirStruct.length > 0){
					String currentDirDev = jarLocation + "\\files-after";
					String currentDirInt = jarLocation + "\\files-before";
					boolean mismatch = false;
					int mismatchCount = 0;
					for(int j=0;j<devFileDirStruct.length;j++){
						if(!devFileDirStruct[j].equalsIgnoreCase(intFileDirStruct[j]) || mismatch){
							mismatch= true;
							mismatchCount++;
							if(mismatchCount != 1){
							File f = new File(currentDirDev+"\\"+devFileDirStruct[j]);
							File f1 = new File(currentDirInt+"\\"+intFileDirStruct[j]);
							if(!f.exists() && j != (devFileDirStruct.length-1)){
								f.mkdir();
								f1.mkdir();
							}
							currentDirDev = f.getAbsolutePath();
							currentDirInt = f1.getAbsolutePath();
							if(j==(devFileDirStruct.length-1)){
								String cmd = "cmd /c copy " +devFileList.get(i)+ " "+currentDirDev;
								String cmd1 = "cmd /c copy " +intFileList.get(i)+ " "+currentDirInt;
								try {
									Runtime.getRuntime().exec(cmd);
									Runtime.getRuntime().exec(cmd1);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						  }
						}
					}
				}
			}
		}
	}
	
	private String convertPath(String paramString) {
		paramString = paramString.trim().toLowerCase();
		byte[] arrayOfByte = paramString.getBytes();
		int i = 0;
		int j = arrayOfByte.length;
		while (i < j) {
			if (arrayOfByte[i] == 92) {
				arrayOfByte[i] = 47;
			}
			i++;
		}
		return new String(arrayOfByte);
	}
	public JLabel reviewNamelabel;
	public JTextField reviewNameTextField;
	public ButtonGroup radioButtonGroup;
	public JRadioButton newReviewButton;
	public JRadioButton existingReviewRadioButton;
	public JTextField reviewNumberTextField;
	public JButton okButton;
	
}
