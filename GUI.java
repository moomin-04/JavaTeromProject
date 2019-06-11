package TreeModel;
import Test.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class GUI extends JFrame implements ActionListener, TreeSelectionListener{
	private JFrame frame;
	private JButton OK, Cancel, Change;
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem Open, Exit, Save;
	private JTree tree;
	private JTable table;
	private JPanel treePanel, contentPanel, changePanel; //content는 method의 body
	private JScrollPane scrollPane1, scrollPane2;
	private JTextArea contentArea, variableArea, ChangeTextArea;
	private Vector<DefaultMutableTreeNode> method;
	private Vector<DefaultMutableTreeNode> variable;
	private DefaultMutableTreeNode root;
	private String text;
	private String address;
	private Parsing p;
	private int num;
	private String temp;
	private String Original;
	
	//메뉴설정
	public GUI() { 
		setSize(500, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridLayout(0,2));
		
		treePanel = new JPanel(new GridLayout(2,1));
		contentPanel = new JPanel(new BorderLayout());
		Change = new JButton("변경");
		Change.addActionListener(this);
		//메뉴
		Open = new JMenuItem("Open");
		Save = new JMenuItem("Sava");
		Exit = new JMenuItem("Exit");
		
		//메뉴 이벤트 설정
		Open.addActionListener(this);
		Save.addActionListener(this);
		Exit.addActionListener(this);
		
		menu = new JMenu("File");
		menuBar = new JMenuBar();
		menu.add(Open);
		menu.add(Save);
		menu.add(Exit);
		menuBar.add(menu);
		setJMenuBar(menuBar);
		setVisible(true);
	}
	//이벤트 처리
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == Exit) {
			System.exit(0);
		}
		if(e.getSource() == Open) {
			p = new Parsing();
			variableArea = new JTextArea(10,10);
			contentArea = new JTextArea(40,40);
			
			//tree 생성
			root = new DefaultMutableTreeNode("" + p.getClassName()); //classInfo.getName같은 메소드 필요?
			tree = new JTree(root);
			tree.addTreeSelectionListener(this);
			
			scrollPane1 = new JScrollPane(tree);
			treePanel.add(scrollPane1);
			treePanel.add(variableArea);
			contentPanel.add(contentArea, BorderLayout.CENTER);
			add(treePanel);
			add(contentPanel);
			setVisible(true);
		}
		//Change랑 Save 잘 모르겠음
		if(e.getSource() == Change) {
			temp = contentArea.getText();
		}
		if(e.getSource() == Save) {
		
		}
		if(e.getSource() == OK) {
			
		}
		if(e.getSource() == Cancel) {
			
		}
	}
	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent(); //node에 현재 선택된 객체를 가져온다
		if(node == null)
			return;
		String s = (String)node.getUserObject(); //노드로 지정된 객체를 반환한다. 보통 이벤트 처리시 노드의 이름을 가져올 때 사용한다.
		
		//트리에서 선택된 항목이 클래스의 이름과 같은 경우?
		if(s.equals(p.getClassName())) {
			try{
				scrollPane2.remove(contentArea);
	            // 메소드와 변수내용을 가진 테이블을 스크롤패널에 붙인다. 
	            scrollPane2.add(table);
	            //밑에 두개가 뭐지?
	            //contentPanel.remove(scrollPane); 
	            //contentPanel.remove(changePanel);
	            variableArea.setBorder(null);
	            variableArea.setText(null);
	            } catch(Exception e1){}
		}
		
		//table생성
		String[] col = {"Name", "Type", "Access"};
		Object[][] row = new Object[20][3]; //크기는 임의로 지정
		table = new JTable(row, col);
		scrollPane2 = new JScrollPane(table);
		contentPanel.add(scrollPane2, BorderLayout.CENTER);
		add(contentPanel);
		setVisible(true);
		
		//테이블에 메소드 정보 표시
		for(int i = 0; i < p.getMethodInfo.size(); i++) {
            row[i][0] = p.getMethoInfo(i).getName();
            row[i][1] = getMethodInfo(i).MethodType();
            row[i][2] = getMethodInfo(i).MethodAccess();
		
        //트리에 메소드노드를 붙인다.
        method.add(i, new DefaultMutableTreeNode("-" + p.getMethodInfo(i).getName()));
        root.add(method.get(i));
		}
		
        //테이블에 변수 표시
		for(int i = 0; i < p.getVarInfo.size(); i++) {
			row[i + p.getMethodInfo.size()][0] = p.getVarInfo(i).getName();
			row[i + p.getMethodInfo.size()][1] = p.getVarInfo(i).getType();
			row[i + p.getMethodInfo.size()][2] = p.getVarInfo(i).getAccess();
		
		//트리에 변수 항목 표시
		variable.add(new DefaultMutableTreeNode("-" + p.getVarInfo(i).getName()));
		root.add(variable.get(i));
		}
		
		//메소드를 선택했을 때 이벤트 표시
		for(int i = 0 ; i < p.getMethodInfo.size(); i++) {
			if(s.equals("-" + p.getMehtoInfo(i).getName())) {
				try {
					//scrollPane2에 있던 내용 지우기
					scrollPane2.remove(contentArea);
					scrollPane2.remove(table);
					contentPanel.remove(scrollPane2);
					//있던 내용 옮겨두기
					Original = p.getMethodInfo(i).getContent();
					//이벤트가 발생한 노드에 대한 메소드 객체배열에서의 위치표시?
					num=i;
				} catch(Exception e1) {}
				
				String variable = "";
				
				//메소드 정보로부터 메소드가 사용한 변수를 불러와 출력한다.
				for(int j = 0; j < p.getMethodInfo[i].getVarSize; j++) {
					variable = variable.concat(p.getMethodInfo(i).getVarInfo(j).getName() + "\n");	
				}
				
				Border border1 = BorderFactory.createTitledBorder("Use Variable");
	            Border border2 = BorderFactory.createTitledBorder("Method Content");
	            variableArea.setBorder(border1);
	            contentArea.setBorder(border2);
	               
	            contentArea.setText("\n" + p.getMethodInfo(i).getContent());
	            variableArea.setText(variable); //method에서 사요하는 var를 바로 위에서 정의한 string으로 표현
	               
	            variableArea.setEditable(false);
	            contentArea.setEditable(true);
	               
	            scrollPane1 = new JScrollPane(contentArea);
	            contentPanel.add(scrollPane1,BorderLayout.CENTER);
	            
	            //chagne부분 잘 모르겠다
	            changePanel = new JPanel();
	            changePanel.add(Change);
	            contentPanel.add(changePanel, BorderLayout.PAGE_END);
	            
	            treePanel.add(variableArea,BorderLayout.PAGE_END);
	            add(treePanel);
	            add(contentPanel);   
	            setVisible(true);
			}
		}
		//변수를 선택했을 테이블 표시
		for(int k = 0; k < p.getVarInfo.size(); k++) {
			if(s.equals("-" + p.getVarInfo(i).getName() + " : "+p.getVarInfo(i).getType())) {
	        // 트리에서 선택된 노드가 변수 이름과 같을 때
				try{
					scrollPane2.remove(contentArea);
	                scrollPane2.remove(table);
	                contentPanel.remove(scrollPane2);
	                contentPanel.remove(changePanel);
	                variableArea.setBorder(null);
	                variableArea.setText(null);
	               } catch(Exception e1){}
	               
	            // 변수 이름과 변수과 사용된 메소드를 기록한 테이블 생성
	            String[] cols = {"Name","method"};
	            Object[][] data = new Object[10][2];
	            for(int j=0; j<p.getVarInfo(k).getMethodSize; j++) {
	                  data[j][0] = null; //변수의 이름이 들어가야함(밑에서 설정함)
	                  data[j][1] = p.getVarInfo(k).getMethodInfo(j).getName();
	            }
	            data[0][0] = p.getVarInfo(k).getName();
	               
	            table = new JTable(data,cols);
	            
	            //table 속성 설정
	            //table.setPreferredScrollableViewportSize(new Dimension(200,200));
	            //table.setFillsViewportHeight(true);
	            //table.setAutoCreateRowSorter(true);
	               
	            scrollPane2 = new JScrollPane(table);
	            contentPanel.add(scrollPane2,BorderLayout.CENTER);
	            treePanel.add(variableArea,BorderLayout.PAGE_END);
	            add(treePanel);
	            add(contentPanel);                          
	            }            
		}
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GUI gui = new GUI();
	}	
}
	
