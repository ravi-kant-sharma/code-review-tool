package main;

import java.awt.Container;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;

public class ReviewListDialog extends JDialog{
	CodeReviewGUIClient crguc;
	String jarLocation;
	JFrame parentFrame;
	ArrayList<String> reviewList;
	
	public ReviewListDialog(Frame parent,CodeReviewGUIClient crguc){
		this.crguc = crguc;
		this.parentFrame = (JFrame)parent;
		this.setLocation(parent.getX()+50, parent.getY()+50);
		this.setAlwaysOnTop(true);
		this.setVisible(true);
		this.jarLocation = crguc.jarLocation;
		initComponents();
		
	}
	private void initComponents(){
		Container dialogContaniner = this.getContentPane();
		dialogContaniner.setLayout(null);
		
		this.setSize(320,340);
		File f = new File(jarLocation+"\\reviews");
		File[] files =f.listFiles();
		reviewList= new ArrayList<String>();
		for(File file : files){
			reviewList.add(file.getName());
		}
		
		fileListScrollPane = new JScrollPane();
		fileList = new JList();
		fileList.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent evt) {
		    	jListNicknamesMouseClicked(evt);
		    }
		});
		fileList.setListData(reviewList.toArray());
		dialogContaniner.add(fileListScrollPane);
		fileListScrollPane.setBounds(0,0,300,300);
		fileListScrollPane.setViewportView(fileList);
	}
	
	private void jListNicknamesMouseClicked(java.awt.event.MouseEvent evt) {                                            
	    JList list = (JList)evt.getSource();
	    if (evt.getClickCount() == 2) {
	        int index = list.locationToIndex(evt.getPoint());
	        
	        File f = new File(jarLocation+"\\reviews\\"+reviewList.get(index));
	        FileInputStream fis;
			try {
				fis = new FileInputStream(f);
				byte[] b = new byte[(int) f.length()];
			    fis.read(b);
			    String s = new String(b);
		        for(String s1 : s.split("\r\n")){
		        	crguc.addFileInListFromFilePath(s1);
		        }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      
	        this.dispose();
	    }
	}
	
	public JScrollPane fileListScrollPane;
	public JList fileList;

}
