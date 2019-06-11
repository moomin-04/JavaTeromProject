package FinalTest;

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
	private JTable table_class, table_field;
	private JPanel treePanel, contentPanel, changePanel; //content�� method�� body
	private JScrollPane scrollPane;
	private JTextArea contentArea, variableArea, ChangeTextArea;
	private Vector<DefaultMutableTreeNode> method;
	private Vector<DefaultMutableTreeNode> variable;
	private DefaultMutableTreeNode root;
	private Parsing p;
	
	//�޴�����
	public GUI() { 
		setSize(1000, 1000);
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
			root = new DefaultMutableTreeNode("" + p.cls.getName());
			tree=new JTree(root);
			tree.addTreeSelectionListener(this);
			
			scrollPane = new JScrollPane(tree);
			treePanel.add(scrollPane);
			treePanel.add(variableArea);
			contentPanel.add(contentArea, BorderLayout.CENTER);
			add(treePanel);
			add(contentPanel);
			setVisible(true);
		}
	}
	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent(); //node�� ���� ���õ� ��ü�� �����´�
		if(node == null)
			return;
		String s = (String)node.getUserObject(); //���� ������ ��ü�� ��ȯ�Ѵ�. ���� �̺�Ʈ ó���� ����� �̸��� ������ �� ����Ѵ�.
		
		//Ʈ������ ���õ� �׸��� Ŭ������ �̸��� ���� ���?
		if(s.equals(p.cls.getName())) {
			try{
				//scrollPane.remove(contentArea);
                //scrollPane.remove(table_class);
                //scrollPane.remove(table_field);
                remove(contentPanel);
                variableArea.setBorder(null);
                variableArea.setText(null);
                treePanel.remove(variableArea);
                setVisible(true);
	            } catch(Exception e1){}
		}
		
		//table����
		String[] col = {"Name", "Type", "Access"};
		Object[][] row = new Object[10][3]; //ũ��� ���Ƿ� ����
		table_class = new JTable(row, col);
		//scrollPane = new JScrollPane(table_class);
		//contentPanel = new JPanel();
		setVisible(true);
		
		//���̺� �޼ҵ� ���� ǥ��
		method=new Vector<DefaultMutableTreeNode>();
		for(int i = 0; i < p.cls.getMethod().size(); i++) {
            row[i][0] = p.cls.getMethod().get(i).getName();
            row[i][1] = p.cls.getMethod().get(i).getType();
            row[i][2] = p.cls.getMethod().get(i).getAccess();
		
            //Ʈ���� �޼ҵ��带 ���δ�.
            method.add(new DefaultMutableTreeNode("" + p.cls.getMethod().get(i).getName()));
            root.add(method.get(i));
		} setVisible(true);
		
        //���̺� ���� ǥ��
		variable = new Vector<DefaultMutableTreeNode>();
		for(int i = 0; i < p.cls.getField().size(); i++) {
			row[i + p.cls.getMethod().size()][0] = p.cls.getField().get(i).getName();
			row[i + p.cls.getMethod().size()][1] = p.cls.getField().get(i).getType();
			row[i + p.cls.getMethod().size()][2] = p.cls.getField().get(i).getAccess();
		
			//Ʈ���� ���� �׸� ǥ��
			variable.add(new DefaultMutableTreeNode("" + p.cls.getField().get(i).getName()));
			root.add(variable.get(i));
		} setVisible(true);
		contentPanel.add(table_class, BorderLayout.CENTER);
		add(contentPanel);
		
		
		//�޼ҵ带 �������� �� �̺�Ʈ ǥ��
		for(int i = 0 ; i < p.cls.getMethod().size(); i++) {
			if(s.equals(p.cls.getMethod().get(i).getName())) {
				try {
					//scrollPane�� �ִ� ���� �����
//					scrollPane.remove(contentArea);
//	                scrollPane.remove(table_class);
//	                scrollPane.remove(table_field);
//	                contentPanel.remove(contentArea);
	                contentPanel.remove(table_class);
	                variableArea.setBorder(null);
	                variableArea.setText(null);
				} catch(Exception e1) {}
				
				String variable = "";
				
				//�޼ҵ� �����κ��� �޼ҵ尡 ����� ������ �ҷ��� ����Ѵ�.
				for(int j = 0; j < p.cls.getMethod().get(i).getField().size(); j++) {
					variable = variable.concat(p.cls.getMethod().get(i).getField().get(j).getName() + "\n");	
				}
				
				Border border1 = BorderFactory.createTitledBorder("Use Variable");
	            Border border2 = BorderFactory.createTitledBorder("Method Content");
	            variableArea.setBorder(border1);
	            contentArea.setBorder(border2);
	               
	            contentArea.setText("\n" + p.cls.getMethod().get(i).getBody());
	            variableArea.setText(variable); //method���� ����ϴ� var�� �ٷ� ������ ������ string���� ǥ��
	               
	            variableArea.setEditable(false);
	            contentArea.setEditable(true);
	               
	            //scrollPane = new JScrollPane(contentArea);
	            contentPanel.add(contentArea,BorderLayout.CENTER);
	            
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
		for(int k = 0; k < p.cls.getField().size(); k++) {
			if(s.equals("" + p.cls.getField().get(k).getName() + " : "+p.cls.getField().get(k).getType())) {
	        // Ʈ������ ���õ� ��尡 ���� �̸��� ���� ��
				try{
//					scrollPane.remove(contentArea);
//	                scrollPane.remove(table_class);
//	                scrollPane.remove(table_field);
//	                contentPanel.remove(scrollPane);
					remove(variableArea);
	                variableArea.setBorder(null);
	                variableArea.setText(null);
	               } catch(Exception e1){}
	               
	            // ���� �̸��� ������ ���� �޼ҵ带 ����� ���̺� ����
	            String[] cols = {"Name","method"};
	            Object[][] data = new Object[10][2];
	            for(int j=0; j < p.cls.getField().get(k).getMethod().size(); j++) {
	                  data[j][0] = null; //������ �̸��� ������(�ؿ��� ������)
	                  data[j][1] = p.cls.getField().get(k).getMethod().get(j).getName();
	            }
	            data[0][0] = p.cls.getField().get(k).getName();
	        
	            //table �Ӽ� ����
	            //table.setPreferredScrollableViewportSize(new Dimension(200,200));
	            //table.setFillsViewportHeight(true);
	            //table.setAutoCreateRowSorter(true);
	            table_field = new JTable(data,cols);
	            //scrollPane = new JScrollPane(table_field);
	            contentPanel.add(table_field,BorderLayout.CENTER);
	            treePanel.add(variableArea,BorderLayout.PAGE_END);
	            add(treePanel);
	            add(contentPanel);                          
	            }            
		}
		
	}
	public static void main(String[] args) {
		// TODO Autogenerated method stub
		GUI gui = new GUI();
	}	
}
	
