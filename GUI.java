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
	private JPanel treePanel, contentPanel; //treePanel���� Ʈ�������� �޼ҵ忡 ���Ǵ� ������, contentPanel���� �޼ҵ��� body�� table�� ǥ�õ�.
	private JTextArea contentArea, variableArea;
	private Vector<DefaultMutableTreeNode> method;
	private Vector<DefaultMutableTreeNode> variable;
	private DefaultMutableTreeNode root;
	private Parsing p;
	
	//�޴�����
	public GUI() { 
		setSize(1000, 1000);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridLayout(0,2)); //���ʿ� treePanel, �����ʿ� contentPanel.
		
		treePanel = new JPanel(new GridLayout(2,1)); //������ Tree����, �Ʒ����� �޼ҵ忡�� ���Ǵ� ����ǥ��.
		contentPanel = new JPanel(new BorderLayout());
		
		//�޴�
		Open = new JMenuItem("Open");
		Exit = new JMenuItem("Exit");
		Save = new JMenuItem("Save");
		
		//�޴� �̺�Ʈ ����
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
	//�̺�Ʈ ó��
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == Exit) {
			System.exit(0);
		}
		if(e.getSource() == Open) {
			p = new Parsing();
			
			variableArea = new JTextArea(10,10); //TreePanel�� �Ʒ��ʿ� ���� Area
			contentArea = new JTextArea(40,40); //contentPanel�� ���� �޼ҵ��� body�� ǥ���� Area.
			
			//tree ����
			root = new DefaultMutableTreeNode("" + p.cls.getName());
			tree = new JTree(root);
			tree.addTreeSelectionListener(this);
			
			//treePanel�� add�ϱ�.
			treePanel.add(tree, BorderLayout.PAGE_START); //Tree������ treePanel���� ����(����)�� ��ġ��.
			//treePanel.add(variableArea,BorderLayout.PAGE_END);
			//contentPanel�� add�ϱ�.
			contentPanel.add(contentArea, BorderLayout.CENTER);
			scrollPane_class=new JScrollPane();
			scrollPane_field = new JScrollPane();
			//Frame�� add�ϱ�.
			add(treePanel); 
			add(contentPanel);
			setVisible(true);
		}
	}
	public void valueChanged(TreeSelectionEvent e) {//Ʈ�� ���ÿ� ���� �̺�Ʈ ó��
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent(); //node�� ���� ���õ� ��ü�� �����´�
		if(node == null)
			return;
		
		String s = (String)node.getUserObject(); //���� ������ ��ü�� ��ȯ.
		
		
//Ʈ������ Ŭ������带 �������� ��.
		if(node == root) { //Ŭ���� Stack�� �޼ҵ� Stack�� �����ϱ� ���� ���.
			if(s.equals(p.cls.getName())) {
				try{//���� component����-> �г� �ʱ�ȭ
					contentPanel.remove(contentArea);
					contentPanel.remove(scrollPane_class);
					contentPanel.remove(scrollPane_field);
					variableArea.setBorder(null);
					variableArea.setText(null);
				}	catch(Exception e1) {}
			}
		
		//���̺����
			String[] col1 = {"Name", "Type", "Access"};
			Object[][] row1 = new Object[20][3]; 
		
		//���̺� �޼ҵ� ���� ǥ��
			method=new Vector<DefaultMutableTreeNode>();
			for(int i = 0; i < p.cls.getMethod().size(); i++) {
            	row1[i][0] = p.cls.getMethod().get(i).getName()+"("+p.cls.getMethod().get(i).getParameterType()+")";
            	row1[i][1] = p.cls.getMethod().get(i).getType();
            	row1[i][2] = p.cls.getMethod().get(i).getAccess();
		
            //Ʈ���� �޼ҵ��带 ���δ�.
            	method.add(new DefaultMutableTreeNode("" + p.cls.getMethod().get(i).getName()+"("+p.cls.getMethod().get(i).getParameterType()+")"));
            	root.add(method.get(i));
            	
            	setVisible(true);
			}
		
			//���̺� cell�� �ʵ�+���� ä���
			variable = new Vector<DefaultMutableTreeNode>();
			for(int i = 0; i < p.cls.getField().size(); i++) {
				row1[i + p.cls.getMethod().size()][0] = p.cls.getField().get(i).getName();
				row1[i + p.cls.getMethod().size()][1] = p.cls.getField().get(i).getType();
				row1[i + p.cls.getMethod().size()][2] = p.cls.getField().get(i).getAccess();
		
			//Ʈ���� �ʵ� ��� �߰�
				variable.add(new DefaultMutableTreeNode("" + p.cls.getField().get(i).getName()));
				root.add(variable.get(i));
			}
			table_class = new JTable(row1, col1);
			scrollPane_class = new JScrollPane(table_class);
			contentPanel.add(scrollPane_class, BorderLayout.CENTER);
			add(contentPanel);
		}
		
//Ʈ������ �޼ҵ� ��带 �������� ��.
		if(node != root) { //Stack�޼ҵ�� root��尡 �ƴ��� Ȯ��.
			for(int i = 0 ; i < p.cls.getMethod().size(); i++) {
				if(s.equals(p.cls.getMethod().get(i).getName()+"("+p.cls.getMethod().get(i).getParameterType()+")")) {
					try {//���� component����-> �г� �ʱ�ȭ
						contentPanel.remove(contentArea);
						contentPanel.remove(scrollPane_class);
						contentPanel.remove(scrollPane_field);
						variableArea.setBorder(null);
						variableArea.setText(null);
					} catch(Exception e1) {}
				
					//���� �Ʒ� ������ ���ٸ� �޼ҵ尡 ���õ� ������ �̹� �����Ǿ� �ִ� contentArea�� �̿��ϱ� ������ ���� ���� table_class, table_field�� ���� ��������.
					contentArea = new JTextArea(40, 40); 
				
					String variable = "";
				
					//�޼ҵ� �����κ��� �޼ҵ尡 ����� ������ �ҷ��� ����Ѵ�.
					for(int j = 0; j < p.cls.getMethod().get(i).getField().size(); j++) {
						variable = variable.concat(p.cls.getMethod().get(i).getField().get(j).getName() + "\n");	
					}
					
					//���� ���� �� �߰�
					Border border1 = BorderFactory.createTitledBorder("Use Variable");
					Border border2 = BorderFactory.createTitledBorder("Method Body");
					variableArea.setBorder(border1);
					contentArea.setBorder(border2);
	               
					contentArea.setText("\n" + p.cls.getMethod().get(i).getBody());
					variableArea.setText(variable); 
	            
					variableArea.setEditable(false);
					contentArea.setEditable(true);
	            
	            //add�ϱ�.
					contentPanel.add(contentArea,BorderLayout.CENTER);
					treePanel.add(variableArea,BorderLayout.PAGE_END);
					add(treePanel);
					add(contentPanel);   
					setVisible(true);
				}
			}
		}
		
//Ʈ������ �ʵ� ��带 �������� ��
		for(int k = 0; k < p.cls.getField().size(); k++) {
			if(s.equals(p.cls.getField().get(k).getName())) {
	        // Ʈ������ ���õ� ��尡 ���� �̸��� ���� ��
				try{
					contentPanel.remove(contentArea);					
					contentPanel.remove(scrollPane_class);
					contentPanel.remove(scrollPane_field);
	                variableArea.setBorder(null);
	                variableArea.setText(null);
	               } catch(Exception e1){}
	               
	            // ���� �̸��� �ش� ������ ����� �޼ҵ带 ������ ���̺� ����
	            String[] col2 = {"Name","method"};
	            Object[][] row2 = new Object[10][2];
	            for(int j=0; j < p.cls.getField().get(k).getMethod().size(); j++) {
	                  row2[j][0] = null; //������ �̸��� ������(�ؿ��� ������)
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
	
