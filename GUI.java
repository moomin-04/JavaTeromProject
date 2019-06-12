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
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem Open, Exit, Save;
	private JTree tree;
	private JTable table_class, table_field;
	private JScrollPane scrollPane_class, scrollPane_field;
	private JPanel treePanel, contentPanel; //treePanel에는 트리구조와 메소드에 사용되는 변수가, contentPanel에는 메소드의 body와 table이 표시됨.
	private JTextArea contentArea, variableArea;
	private Vector<DefaultMutableTreeNode> method;
	private Vector<DefaultMutableTreeNode> variable;
	private DefaultMutableTreeNode root;
	private Parsing p;
	
	//메뉴설정
	public GUI() { 
		setSize(1000, 1000);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridLayout(0,2)); //왼쪽엔 treePanel, 오른쪽엔 contentPanel.
		
		treePanel = new JPanel(new GridLayout(2,1)); //위쪽은 Tree구조, 아래쪽은 메소드에서 사용되는 변수표시.
		contentPanel = new JPanel(new BorderLayout());
		
		//메뉴
		Open = new JMenuItem("Open");
		Exit = new JMenuItem("Exit");
		Save = new JMenuItem("Save");
		
		//메뉴 이벤트 설정
		Open.addActionListener(this);
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
			
			variableArea = new JTextArea(10,10); //TreePanel의 아래쪽에 붙을 Area
			contentArea = new JTextArea(40,40); //contentPanel에 붙을 메소드의 body를 표시할 Area.
			
			//tree 생성
			root = new DefaultMutableTreeNode("" + p.cls.getName());
			tree = new JTree(root);
			tree.addTreeSelectionListener(this);
			
			//treePanel에 add하기.
			treePanel.add(tree, BorderLayout.PAGE_START); //Tree구조는 treePanel에서 먼저(위쪽)에 배치됨.
			//treePanel.add(variableArea,BorderLayout.PAGE_END);
			//contentPanel에 add하기.
			contentPanel.add(contentArea, BorderLayout.CENTER);
			scrollPane_class=new JScrollPane();
			scrollPane_field = new JScrollPane();
			//Frame에 add하기.
			add(treePanel); 
			add(contentPanel);
			setVisible(true);
		}
	}
	public void valueChanged(TreeSelectionEvent e) {//트리 선택에 대한 이벤트 처리
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent(); //node에 현재 선택된 객체를 가져온다
		if(node == null)
			return;
		
		String s = (String)node.getUserObject(); //노드로 지정된 객체를 반환.
		
		
//트리에서 클래스노드를 선택했을 때.
		if(node == root) { //클래스 Stack과 메소드 Stack을 구분하기 위해 사용.
			if(s.equals(p.cls.getName())) {
				try{//이전 component제거-> 패널 초기화
					contentPanel.remove(contentArea);
					contentPanel.remove(scrollPane_class);
					contentPanel.remove(scrollPane_field);
					variableArea.setBorder(null);
					variableArea.setText(null);
				}	catch(Exception e1) {}
			}
		
		//테이블생성
			String[] col1 = {"Name", "Type", "Access"};
			Object[][] row1 = new Object[20][3]; 
		
		//테이블에 메소드 정보 표시
			method=new Vector<DefaultMutableTreeNode>();
			for(int i = 0; i < p.cls.getMethod().size(); i++) {
            	row1[i][0] = p.cls.getMethod().get(i).getName()+"("+p.cls.getMethod().get(i).getParameterType()+")";
            	row1[i][1] = p.cls.getMethod().get(i).getType();
            	row1[i][2] = p.cls.getMethod().get(i).getAccess();
		
            //트리에 메소드노드를 붙인다.
            	method.add(new DefaultMutableTreeNode("" + p.cls.getMethod().get(i).getName()+"("+p.cls.getMethod().get(i).getParameterType()+")"));
            	root.add(method.get(i));
            	
            	setVisible(true);
			}
		
			//테이블 cell에 필드+정보 채우기
			variable = new Vector<DefaultMutableTreeNode>();
			for(int i = 0; i < p.cls.getField().size(); i++) {
				row1[i + p.cls.getMethod().size()][0] = p.cls.getField().get(i).getName();
				row1[i + p.cls.getMethod().size()][1] = p.cls.getField().get(i).getType();
				row1[i + p.cls.getMethod().size()][2] = p.cls.getField().get(i).getAccess();
		
			//트리에 필드 노드 추가
				variable.add(new DefaultMutableTreeNode("" + p.cls.getField().get(i).getName()));
				root.add(variable.get(i));
			}
			table_class = new JTable(row1, col1);
			scrollPane_class = new JScrollPane(table_class);
			contentPanel.add(scrollPane_class, BorderLayout.CENTER);
			add(contentPanel);
		}
		
//트리에서 메소드 노드를 선택했을 때.
		if(node != root) { //Stack메소드는 root노드가 아님을 확인.
			for(int i = 0 ; i < p.cls.getMethod().size(); i++) {
				if(s.equals(p.cls.getMethod().get(i).getName()+"("+p.cls.getMethod().get(i).getParameterType()+")")) {
					try {//이전 component제거-> 패널 초기화
						contentPanel.remove(contentArea);
						contentPanel.remove(scrollPane_class);
						contentPanel.remove(scrollPane_field);
						variableArea.setBorder(null);
						variableArea.setText(null);
					} catch(Exception e1) {}
				
					//만약 아래 라인이 없다면 메소드가 선택될 때마다 이미 생성되어 있던 contentArea를 이용하기 때문에 새로 생긴 table_class, table_field에 의해 가려진다.
					contentArea = new JTextArea(40, 40); 
				
					String variable = "";
				
					//메소드 정보로부터 메소드가 사용한 변수를 불러와 출력한다.
					for(int j = 0; j < p.cls.getMethod().get(i).getField().size(); j++) {
						variable = variable.concat(p.cls.getMethod().get(i).getField().get(j).getName() + "\n");	
					}
					
					//보더 생성 및 추가
					Border border1 = BorderFactory.createTitledBorder("Use Variable");
					Border border2 = BorderFactory.createTitledBorder("Method Body");
					variableArea.setBorder(border1);
					contentArea.setBorder(border2);
	               
					contentArea.setText("\n" + p.cls.getMethod().get(i).getBody());
					variableArea.setText(variable); 
	            
					variableArea.setEditable(false);
					contentArea.setEditable(true);
	            
	            //add하기.
					contentPanel.add(contentArea,BorderLayout.CENTER);
					treePanel.add(variableArea,BorderLayout.PAGE_END);
					add(treePanel);
					add(contentPanel);   
					setVisible(true);
				}
			}
		}
		
//트리에서 필드 노드를 선택했을 때
		for(int k = 0; k < p.cls.getField().size(); k++) {
			if(s.equals(p.cls.getField().get(k).getName())) {
	        // 트리에서 선택된 노드가 변수 이름과 같을 때
				try{
					contentPanel.remove(contentArea);					
					contentPanel.remove(scrollPane_class);
					contentPanel.remove(scrollPane_field);
	                variableArea.setBorder(null);
	                variableArea.setText(null);
	               } catch(Exception e1){}
	               
	            // 변수 이름과 해당 변수를 사용한 메소드를 저장한 테이블 생성
	            String[] col2 = {"Name","method"};
	            Object[][] row2 = new Object[10][2];
	            for(int j=0; j < p.cls.getField().get(k).getMethod().size(); j++) {
	                  row2[j][0] = null; //변수의 이름이 들어가야함(밑에서 설정함)
	                  row2[j][1] = p.cls.getField().get(k).getMethod().get(j).getName()+
	                		  "("+p.cls.getField().get(k).getMethod().get(j).getParameterType()+")";
	            }
	            row2[0][0] = p.cls.getField().get(k).getName();
	   
	
	            table_field = new JTable(row2, col2);
	            scrollPane_field = new JScrollPane(table_field);
	            contentPanel.add(scrollPane_field,BorderLayout.CENTER);
	            treePanel.add(variableArea,BorderLayout.PAGE_END);
	            
	            add(treePanel);
	            add(contentPanel);
	            setVisible(true);
	            }            
		}
		
	}
	public static void main(String[] args) {
		// TODO Autogenerated method stub
		GUI gui = new GUI();
	}	
}
	
