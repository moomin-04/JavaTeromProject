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
	private JPanel treePanel, contentPanel, changePanel; //content�� method�� body
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
	
	//�޴�����
	public GUI() { 
		setSize(500, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridLayout(0,2));
		
		treePanel = new JPanel(new GridLayout(2,1));
		contentPanel = new JPanel(new BorderLayout());
		Change = new JButton("����");
		Change.addActionListener(this);
		//�޴�
		Open = new JMenuItem("Open");
		Save = new JMenuItem("Sava");
		Exit = new JMenuItem("Exit");
		
		//�޴� �̺�Ʈ ����
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
	//�̺�Ʈ ó��
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == Exit) {
			System.exit(0);
		}
		if(e.getSource() == Open) {
			p = new Parsing();
			variableArea = new JTextArea(10,10);
			contentArea = new JTextArea(40,40);
			
			//tree ����
			root = new DefaultMutableTreeNode("" + p.getClassName()); //classInfo.getName���� �޼ҵ� �ʿ�?
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
		//Change�� Save �� �𸣰���
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
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent(); //node�� ���� ���õ� ��ü�� �����´�
		if(node == null)
			return;
		String s = (String)node.getUserObject(); //���� ������ ��ü�� ��ȯ�Ѵ�. ���� �̺�Ʈ ó���� ����� �̸��� ������ �� ����Ѵ�.
		
		//Ʈ������ ���õ� �׸��� Ŭ������ �̸��� ���� ���?
		if(s.equals(p.getClassName())) {
			try{
				scrollPane2.remove(contentArea);
	            // �޼ҵ�� ���������� ���� ���̺��� ��ũ���гο� ���δ�. 
	            scrollPane2.add(table);
	            //�ؿ� �ΰ��� ����?
	            //contentPanel.remove(scrollPane); 
	            //contentPanel.remove(changePanel);
	            variableArea.setBorder(null);
	            variableArea.setText(null);
	            } catch(Exception e1){}
		}
		
		//table����
		String[] col = {"Name", "Type", "Access"};
		Object[][] row = new Object[20][3]; //ũ��� ���Ƿ� ����
		table = new JTable(row, col);
		scrollPane2 = new JScrollPane(table);
		contentPanel.add(scrollPane2, BorderLayout.CENTER);
		add(contentPanel);
		setVisible(true);
		
		//���̺� �޼ҵ� ���� ǥ��
		for(int i = 0; i < p.getMethodInfo.size(); i++) {
            row[i][0] = p.getMethoInfo(i).getName();
            row[i][1] = getMethodInfo(i).MethodType();
            row[i][2] = getMethodInfo(i).MethodAccess();
		
        //Ʈ���� �޼ҵ��带 ���δ�.
        method.add(i, new DefaultMutableTreeNode("-" + p.getMethodInfo(i).getName()));
        root.add(method.get(i));
		}
		
        //���̺� ���� ǥ��
		for(int i = 0; i < p.getVarInfo.size(); i++) {
			row[i + p.getMethodInfo.size()][0] = p.getVarInfo(i).getName();
			row[i + p.getMethodInfo.size()][1] = p.getVarInfo(i).getType();
			row[i + p.getMethodInfo.size()][2] = p.getVarInfo(i).getAccess();
		
		//Ʈ���� ���� �׸� ǥ��
		variable.add(new DefaultMutableTreeNode("-" + p.getVarInfo(i).getName()));
		root.add(variable.get(i));
		}
		
		//�޼ҵ带 �������� �� �̺�Ʈ ǥ��
		for(int i = 0 ; i < p.getMethodInfo.size(); i++) {
			if(s.equals("-" + p.getMehtoInfo(i).getName())) {
				try {
					//scrollPane2�� �ִ� ���� �����
					scrollPane2.remove(contentArea);
					scrollPane2.remove(table);
					contentPanel.remove(scrollPane2);
					//�ִ� ���� �Űܵα�
					Original = p.getMethodInfo(i).getContent();
					//�̺�Ʈ�� �߻��� ��忡 ���� �޼ҵ� ��ü�迭������ ��ġǥ��?
					num=i;
				} catch(Exception e1) {}
				
				String variable = "";
				
				//�޼ҵ� �����κ��� �޼ҵ尡 ����� ������ �ҷ��� ����Ѵ�.
				for(int j = 0; j < p.getMethodInfo[i].getVarSize; j++) {
					variable = variable.concat(p.getMethodInfo(i).getVarInfo(j).getName() + "\n");	
				}
				
				Border border1 = BorderFactory.createTitledBorder("Use Variable");
	            Border border2 = BorderFactory.createTitledBorder("Method Content");
	            variableArea.setBorder(border1);
	            contentArea.setBorder(border2);
	               
	            contentArea.setText("\n" + p.getMethodInfo(i).getContent());
	            variableArea.setText(variable); //method���� ����ϴ� var�� �ٷ� ������ ������ string���� ǥ��
	               
	            variableArea.setEditable(false);
	            contentArea.setEditable(true);
	               
	            scrollPane1 = new JScrollPane(contentArea);
	            contentPanel.add(scrollPane1,BorderLayout.CENTER);
	            
	            //chagne�κ� �� �𸣰ڴ�
	            changePanel = new JPanel();
	            changePanel.add(Change);
	            contentPanel.add(changePanel, BorderLayout.PAGE_END);
	            
	            treePanel.add(variableArea,BorderLayout.PAGE_END);
	            add(treePanel);
	            add(contentPanel);   
	            setVisible(true);
			}
		}
		//������ �������� ���̺� ǥ��
		for(int k = 0; k < p.getVarInfo.size(); k++) {
			if(s.equals("-" + p.getVarInfo(i).getName() + " : "+p.getVarInfo(i).getType())) {
	        // Ʈ������ ���õ� ��尡 ���� �̸��� ���� ��
				try{
					scrollPane2.remove(contentArea);
	                scrollPane2.remove(table);
	                contentPanel.remove(scrollPane2);
	                contentPanel.remove(changePanel);
	                variableArea.setBorder(null);
	                variableArea.setText(null);
	               } catch(Exception e1){}
	               
	            // ���� �̸��� ������ ���� �޼ҵ带 ����� ���̺� ����
	            String[] cols = {"Name","method"};
	            Object[][] data = new Object[10][2];
	            for(int j=0; j<p.getVarInfo(k).getMethodSize; j++) {
	                  data[j][0] = null; //������ �̸��� ������(�ؿ��� ������)
	                  data[j][1] = p.getVarInfo(k).getMethodInfo(j).getName();
	            }
	            data[0][0] = p.getVarInfo(k).getName();
	               
	            table = new JTable(data,cols);
	            
	            //table �Ӽ� ����
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
	
