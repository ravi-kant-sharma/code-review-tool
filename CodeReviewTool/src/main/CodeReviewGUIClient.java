package main;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class CodeReviewGUIClient {

	public Properties properties;
	public Map<String,String> dirMap;
	public String jarLocation;
	public String filePath;
	
	public void setFilePath(String value){
		this.filePath = value;
	}
	
	public String getFilePath(){
		return this.filePath;
	}
	
	public CodeReviewGUIClient(){
		initComponents();
		this.codeReviewFrame.setLocation(300, 200);
		this.codeReviewFrame.setVisible(true);
	}
	
	private void initComponents(){
		try {
			this.properties = CodeReviewUploadHelper.getUserProperties();
			this.jarLocation = (String) this.properties.get("jar_loc");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		codeReviewFrame = new JFrame();
		fileListScrollPane = new JScrollPane();
		fileList = new JList();
		existingReviewsButton = new JButton();
		browseFileButton = new JButton();
		addFileButton = new JButton();
		removeFileButton = new JButton();
		uploadButton = new JButton();
		fileArrayListDev = new ArrayList<String>();
		fileArrayListInt = new ArrayList<String>();
		dirMap = CodeReviewUploadHelper.getDirectoriesMap(this.properties);
		
		
		{
			codeReviewFrame.setTitle("Code Review Upload");
			Container codeReviewContentPane = codeReviewFrame.getContentPane();
			codeReviewContentPane.setLayout(null);
			codeReviewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			codeReviewFrame.setSize(630,400);
			
			codeReviewContentPane.add(fileListScrollPane);
			fileListScrollPane.setBounds(10,10,585,300);
			fileListScrollPane.setViewportView(fileList);
			
			codeReviewContentPane.add(existingReviewsButton);
			codeReviewContentPane.add(browseFileButton);
			codeReviewContentPane.add(addFileButton);
			codeReviewContentPane.add(removeFileButton);
			codeReviewContentPane.add(uploadButton);
			
			existingReviewsButton.setText("Existing Reviews");
			browseFileButton.setText("Browse File");
			addFileButton.setText("Add File");
			removeFileButton.setText("Remove File");
			uploadButton.setText("Upload Files");
			
//			browseFileButton.setBounds(10,320,110,30);
//			addFileButton.setBounds(140, 320, 90, 30);
//			removeFileButton.setBounds(250,320,110,30);
//			uploadButton.setBounds(380,320,110,30);
			
			existingReviewsButton.setBounds(10,320,130,30);
			browseFileButton.setBounds(150, 320, 110, 30);
			addFileButton.setBounds(270,320,90,30);
			removeFileButton.setBounds(370,320,110,30);
			uploadButton.setBounds(490,320,110,30);
			
		}
		
		{
		
			browseFileButton.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					browseFileButtonActionPerformed();
				}
			});
			
			addFileButton.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e){
					addFileButtonActionPerformed();
				}
			});
			
			removeFileButton.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e){
					removeFileButtonActionPerformed();
				}
			});
			
			uploadButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					uploadButtonActionPerformed();
				}
			});
			
			existingReviewsButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					existingReviewsButtonActionPerformed();
				}
			});
		}
		
	}
	
	private void browseFileButtonActionPerformed(){
		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Add File");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setMultiSelectionEnabled(true);
		fileChooser.setAcceptAllFileFilterUsed(false);
		int result = fileChooser.showOpenDialog(this.codeReviewFrame);
		
		
		if (result == JFileChooser.APPROVE_OPTION) {
			File[] filesList = fileChooser.getSelectedFiles();
			for(File f : filesList){
				fileArrayListDev.add(f.getAbsolutePath());
				Iterator itr = dirMap.entrySet().iterator();
				while(itr.hasNext()){
					Object str = itr.next();
					if(f.getAbsolutePath().startsWith((String)((Map.Entry)str).getKey())){
						int i = f.getAbsolutePath().indexOf((String)((Map.Entry)str).getKey()) + ((String)((Map.Entry)str).getKey()).length();
						String str1 = dirMap.get((String)((Map.Entry)str).getKey()) + f.getAbsolutePath().substring(i);
						File intFile = new File(str1);
						if(intFile.exists()){
							fileArrayListInt.add(str1);
						}else{
							fileArrayListInt.add(null);
						}
					}
				}
				
			}
		}
		
		fileList.setListData(fileArrayListDev.toArray());
	}
	
	public void addFileInListFromFilePath(String filePath){
		fileArrayListDev.add(filePath);
		Iterator itr = dirMap.entrySet().iterator();
		while(itr.hasNext()){
			Object str = itr.next();
			if(filePath.startsWith((String)((Map.Entry)str).getKey())){
				int i = filePath.indexOf((String)((Map.Entry)str).getKey()) + ((String)((Map.Entry)str).getKey()).length();
				String str1 = dirMap.get((String)((Map.Entry)str).getKey()) + filePath.substring(i);
				File intFile = new File(str1);
				if(intFile.exists()){
					fileArrayListInt.add(str1);
				}else{
					fileArrayListInt.add(null);
				}
			}
		}
		fileList.setListData(fileArrayListDev.toArray());
	}
	
	private void addFileButtonActionPerformed(){
		AddFilePathDialog addFilePathDialog = new AddFilePathDialog(codeReviewFrame,this);
	}
	
	private void removeFileButtonActionPerformed(){
		int[] selectedIndices = fileList.getSelectedIndices();
		if(selectedIndices != null && selectedIndices.length > 0){
			for(int index : selectedIndices){
				fileArrayListDev.remove(index);
				fileArrayListInt.remove(index);
			}
		}
		fileList.setListData(fileArrayListDev.toArray());
		
	}
	
	private void uploadButtonActionPerformed(){
		ReviewDialog reviewDialog = new ReviewDialog(codeReviewFrame,this);
	}
	
	private void existingReviewsButtonActionPerformed(){
		ReviewListDialog reviewListDialog = new ReviewListDialog(codeReviewFrame,this);
	}
	
	public JFrame codeReviewFrame;
	public JScrollPane fileListScrollPane;
	public JList fileList;
	public JButton existingReviewsButton;
	public JButton browseFileButton;
	public JButton addFileButton;
	public JButton removeFileButton;
	public JButton uploadButton;
	public JDialog reviewDialog;
	public ArrayList<String> fileArrayListDev;
	public ArrayList<String> fileArrayListInt;
	
	public static void main(String[] args) {
		try{
			for(LookAndFeelInfo info: UIManager.getInstalledLookAndFeels()){
				if("Nimbus".equalsIgnoreCase(info.getName())){
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		}catch (Exception e) {
		}
		CodeReviewGUIClient codeReviewGUIClient = new CodeReviewGUIClient();
//		java.awt.EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				
//			}
//		});
	}
}
