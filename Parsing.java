import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.*;
import java.util.*;

public class Parsing {//�Ľ����ִ� Ŭ����
	ReadFileData f=new ReadFileData("./Stack.h");
	public Tokenize tk=new Tokenize(f.getSb());
	ArrayList<String> res=tk.getRes();
	StringBuffer sb=new StringBuffer(tk.sb_without_comment);
	static int methodcnt;
	static int fieldcnt;//�޼ҵ� �� �ʵ� ���� ī��Ʈ
	
	public Parsing() {//parsing ������
		find_class(res);
	}
	public void find_class(ArrayList<String> list) {
		int i=list.indexOf("class");//class ��ġ ã��
		ClassInfo cls=new ClassInfo();//class�������� ��ü����
		cls.class_name=list.get(i+1);//"class" ������ ������(i+1��ġ) Ŭ���� �̸� ����
		cls.start_class=list.indexOf("{");
		cls.end_class=find_end_index(cls.start_class,list);//���۰� �� ���� ����
		System.out.print(cls.end_class);
		find_method(list,cls);
		}
	
	public void find_method(ArrayList<String> list,ClassInfo cls) {
		SaveKeyword kw=new SaveKeyword();
		String access = null;
		for(int i=cls.start_class;i<cls.end_class;i++) {//class���ο� ����� �޼ҵ�� �ʵ带 ã�ƺ���
//			System.out.print(list.get(i)+"/");
			if(kw.kw_access.contains(list.get(i))){
				access=list.get(i);					
			}
			System.out.println(i+access);
			if(cls.class_name.equals(list.get(i))&&list.get(i-1).equals("~")) {//�Ҹ���->body�� class���ο� ���ǵǾ�����->methodinfo���� set�������
					MethodInfo destructor=new MethodInfo();
					destructor.setName("~"+list.get(i)+"()");//�Ҹ��� �̸� ����
					int end_body=find_end_index(i+3,list);//list[i+3]�� {,list[end_body]�� }����Ű��
					String body=null;
					for(int k=i+4;k<end_body;k++) {
							body+=list.get(k);//�� ���̰����� ��� body�� ����
					}
					destructor.setBody(body);
					destructor.setType("void");//�Ҹ��� ��ȯ���� void
					destructor.setAccess(access);
					cls.addMethod(destructor);
					methodcnt++;
				}
			
				else if(cls.class_name.equals(list.get(i))) {//������	
					System.out.println("����:"+list.get(i));
					cls.method_list.add(new MethodInfo(cls.class_name,cls.class_name,sb));
					System.out.print(cls.getMethod().get(methodcnt).getAccess());
					//cls.addMethod(new MethodInfo(cls.class_name,cls.class_name,sb));
					cls.getMethod().get(methodcnt).setAccess(access);
					cls.getMethod().get(methodcnt).setParameterType(list.get(i+2));//i:�޼ҵ��̸�, i+1:��ȣ(, i+2:parameter type
					cls.getMethod().get(methodcnt).setType("void");
					methodcnt++;
					
				}
				else if(kw.kw_type.toString().contains(list.get(i))&&(list.get(i-1).equals(":")||list.get(i-1).equals(";"))) {
					
				//i�� �Ϲ� �޼ҵ� �Ǵ� �ʵ��� ��ȯ���� ����ų ��, parameter�� type�� ���� ���� :�Ǵ� ;�ڿ� ������ ��ȯ���� �ش�
					String name=list.get(i+1);//��ȯ�� ������ �̸�
					if(list.get(i+2).equals("(")) {//�̸� �ڿ� ��ȣ�� ������ �޼ҵ�, �ƴϸ� �ʵ�
						System.out.println("�޼ҵ�:"+list.get(i+1));

						cls.addMethod(new MethodInfo(cls.class_name,name,sb));
						cls.getMethod().get(methodcnt).setType(list.get(i));
						cls.getMethod().get(methodcnt).setAccess(access);
						String p_type=(list.get(i+3).equals(")")?null:list.get(i+3));
						//��ȣ�ȿ� �ƹ��͵� ������ parameter type=null, �ƴϸ� parameter type=i+3
						cls.getMethod().get(methodcnt).setParameterType(p_type);
					
						methodcnt++;
					}
					else {//�ʵ�
						if(list.get(i+1).equals("*")) {//�������϶��� *���� type�� ���� �� �̸� set
							cls.addField(new FieldInfo());
							cls.getField().get(fieldcnt).setType(list.get(i)+"*");
							cls.getField().get(fieldcnt).setName(list.get(i+2));
						}
						else {
							cls.addField(new FieldInfo());
							cls.getField().get(fieldcnt).setType(list.get(i));
							cls.getField().get(fieldcnt).setName(list.get(i+1));
						}
						
						cls.getField().get(fieldcnt).setAccess(access);
						System.out.println("�ʵ�:"+
						cls.getField().get(fieldcnt).getName()+fieldcnt+"����");
						fieldcnt++;
					}
					
				}
			}
		//for��
		for(int i=0;i<cls.method_list.size();i++) {
			System.out.println(cls.method_list.get(i).getName());
			}
		System.out.println("then,");
		for(int i=0;i<cls.field_list.size();i++) {
				System.out.println(cls.field_list.get(i).getName());
				}
		//�޼ҵ� ���ο� �ִ� field ã�Ƽ� ���� ���� ���ִ� �޼ҵ�? ��������!
	}
	
	public int find_end_index(int start,ArrayList<String> arrlist) {//���� ��ȣ ������ �˶�, �̿� �����ϴ� �������� �������ִ� �޼ҵ�
		Stack<Integer> st = new Stack<Integer>();

		for(int i=start;;i++) {
			if(arrlist.get(i).equals("{")||arrlist.get(i).equals("(")) {
				st.push(i);
				System.out.print("push"+i+":"+arrlist.get(i));
			}//��ȣ�� ���� �� �ε���(��ġ)�� ���ÿ� ����
			else if(arrlist.get(i).equals("}")||arrlist.get(i).equals(")")) {
				st.pop();
				System.out.print("pop"+i+":"+arrlist.get(i));

			}//��ȣ�� ������ ���� �ֱ��� ���°�ȣ�� ��ġ�� ����
			if(st.empty()) {
				System.out.println("empty"+i+":"+arrlist.get(i));
				return i;//���� ó���� ���� ��ȣ�� ���ÿ��� �������� ������ ���, ������ ���� ��ȣ�� �޼ҵ��� end ��ġ�̴�.
			}
		}
	}

public static void main(String[] args) {
		Parsing a=new Parsing();		
	}

}
class SaveKeyword {
	ArrayList<String> kw_access= new ArrayList<String>();
	ArrayList<String> kw_type= new ArrayList<String>();
	String kw_class="class";
	SaveKeyword(){
		kw_access.add("public");
		kw_access.add("protected");
		kw_access.add("private");
		
		kw_type.add("int");
		kw_type.add("void");
		kw_type.add("bool");
		
	}
}

//stringbuffer �������� ����� �ڵ��� �ּ��� �����ϰ� ��ūȭ���ִ� Ŭ����
class Tokenize {
	private ArrayList<String> res = new ArrayList<String>();
	StringBuffer sb_without_comment=new StringBuffer();
	public ArrayList<String> getRes() {return res;}
	
	public Tokenize(StringBuffer sb) {
		erase_comment(sb);
		sb_without_comment=sb;
		tokenizer(sb);
	}
	
	//�ּ��� �����ִ� �޼ҵ�
	public void erase_comment(StringBuffer sb){
		int n = 0, k = 0;
		
		while(true) {			
			k = sb.indexOf("//",k+1);//'//'�� ��ġ �ε�����  k�� ����
			if(k == -1) break;
			n = sb.indexOf("\n",k);// '//'���Ŀ� ó�� ������ '\n'�� ��ġ �ε����� n�� ����
			sb.delete(k,n+1);// "//"�� "\n"���̸� delete����
		}
	}
	
	//�ּ��� ������ StringBuffer�� ��ȣ�� ���� ��ūȭ
	public void tokenizer(StringBuffer sb){
		
		String s=sb.toString();
		
		//������ȣ :{}();=
		StringTokenizer st = new StringTokenizer(s, "*+?->~!:{}();=[]\n\r"
				+ "\r\n"+"\t"+" ", true) ;
		
			
		while(st.hasMoreTokens()) {
			res.add(st.nextToken());
		}
		System.out.print(res.size());
//		String temp=null;
		//::, whitespace ó��
		for(int i = 0; i < res.size(); i++) {
			
			
			if(res.get(i).equals(":")) {
				i++;
				if(res.get(i).equals(":")) {
					res.remove(i);
					i--;
					res.set(i, "::");
				}
			}
			if(res.get(i).equals("")||res.get(i).contains(" ")||res.get(i).contains("\r\n")||res.get(i).contains("\t")||res.get(i).contains("\r")||res.get(i).contains("\n")) {
				res.remove(i);
				i--;
			}
//			if(res.get(i).contains("\n") || res.get(i).contains("\t")) {
//	            temp = res.get(i).trim();
//	            res.set(i, temp);
//	         }
//			if(res.get(i).equals("")) {
//				res.remove(i);
//			}
			
			
		}

	}
}

//�������� Ŭ����

class ClassInfo{//Ŭ���� ���� ����
	String class_name;
	ArrayList<MethodInfo> method_list=new ArrayList<MethodInfo>();//Ŭ���� ������ method,field list ����
	ArrayList<FieldInfo> field_list=new ArrayList<FieldInfo>();
	int start_class;
	int end_class;//���۰� �� �ε���
	
	public void addMethod(MethodInfo m) {method_list.add(m);}
	public ArrayList<MethodInfo> getMethod() {return method_list;}
	public void addField(FieldInfo f) {field_list.add(f);}
	public ArrayList<FieldInfo> getField() {return field_list;}
}

class MemberInfo{
	private String name; //�̸�
	private String type; //��ȯ��
	private String access;//����������
	protected int start_index;
	protected int end_index; 
	
	public void setAccess(String access) {this.access=access;}
	public String getAccess() {return access;}
	public void setName(String name) {this.name=name;}
	public String getName() {return name;}
	public void setType(String type) {this.type=type;}
	public String getType() {return type;}

}

class FieldInfo extends MemberInfo{//�ʵ� ��������
	private ArrayList<MethodInfo> method=new ArrayList<MethodInfo>();
	
	public void addMethod(MethodInfo m) {method.add(m);}
	public ArrayList<MethodInfo> getMethod() {return method;}
	
	public FieldInfo() {
		
	}
}


class MethodInfo extends MemberInfo{//�޼ҵ� ��������
	private String body;
	private String parameter_type;
	private ArrayList<FieldInfo> field=new ArrayList<FieldInfo>();
	
	public void setBody(String body) {this.body=body;}
	public String getBody() {return body;}
	public void addField(FieldInfo f) {field.add(f);}
	public ArrayList<FieldInfo> getField() {return field;}
	public void setParameterType(String parameter_type) {this.parameter_type=parameter_type;}
	public String getParameterType() {return parameter_type;}
	
	public MethodInfo(String class_name,String mtd_name, StringBuffer sb) {//Ŭ���� ���� ���� Ŭ������������
		this.setName(mtd_name);
		find_mtd_body(class_name,mtd_name,sb);
	}
	
	public MethodInfo() {//�Ҹ��ڿ����� ���� ��ȹ
		
	}
	public void find_mtd_body(String class_name, String name, StringBuffer sb) {
	//�޼ҵ� �������� ������ �̿��Ͽ� body�� mtd_body�ʵ忡 ��Ʈ�� ���·� ����
		
		String method_form=class_name+"::"+name;
		
		//find_body
		int start=sb.indexOf(method_form);
		start_index=sb.indexOf("{",start);
		find_end_index(start_index,sb);
		body=sb.substring(start_index+1, end_index);
		//�������� ����, ������ ���� ���ϴ� �޼ҵ��̹Ƿ� ������+1�Ͽ� ��ȣ�� ��������.
		
	}
	
	public void find_end_index(int start,StringBuffer sb) {
	//���� ��ȣ ������ �˶�, �̿� �����ϴ� �������� �������ִ� �޼ҵ�
		Stack<Integer> st = new Stack<Integer>();

		for(int i=start;;i++) {
			if(sb.charAt(i)=='{'||sb.charAt(i)=='(') {
				st.push(i);
			}//��ȣ�� ���� �� �ε���(��ġ)�� ���ÿ� ����
			else if(sb.charAt(i)=='}'||sb.charAt(i)==')') {
				st.pop();
			}//��ȣ�� ������ ���� �ֱ��� ���°�ȣ�� ��ġ�� ����
			if(st.empty()) {
				end_index=i;
				//���� ó���� ���� ��ȣ�� ���ÿ��� �������� ������ ���, ������ ���� ��ȣ�� �޼ҵ��� end ��ġ�̴�.
				break;
			}
		}
	}
}
class ReadFileData {
	static private StringBuffer sb;
	ReadFileData(String address){
		read(address);
	}
	public StringBuffer getSb() {return sb;}
	public void read(String address){//�ҷ������� �ϴ� ������ �ּҸ� �Ű������� �޾ƿ��� ���� �д� �޼ҵ�
		int b=0;
		StringBuffer buffer= new StringBuffer();
		FileInputStream file=null;
		try {
			file=new FileInputStream(address);
			b=file.read();
			while(b!=-1) {
				buffer.append((char)b);
				b=file.read();
			}
			sb=buffer;
		}catch(FileNotFoundException e) {
			System.out.println("Oops:FileNotFoundException");
		}catch(IOException e) {
			System.out.println("Input error");
		}
	} 
}
