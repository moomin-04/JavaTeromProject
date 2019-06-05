import java.util.*;

public class Parsing {//�Ľ����ִ� Ŭ����
	ReadFileData f=new ReadFileData();
	public Tokenize tk=new Tokenize(f.getSb());
	ArrayList<String> res=tk.getRes();
	public Parsing() {
		find_class(res);
	}
	public void find_class(ArrayList<String> list) {
		int i=list.indexOf("class");//class ��ġ ã��
		ClassInfo cls=new ClassInfo();//class�������� ��ü����
		cls.class_name=list.get(i+1);//"class" ������ ������(i+1��ġ) Ŭ���� �̸� ����
		cls.start_class=i+2;
		cls.end_class=find_end_index(cls.start_class,list);//���۰� �� ���� ����
		find_method(list,cls);
		}
	public void find_method(ArrayList<String> list,ClassInfo cls) {
		SaveKeyword kw=new SaveKeyword();
		for(int i=cls.start_class;i<cls.end_class;i++) {//class���ο� ����� �޼ҵ�� �ʵ带 ã�ƺ���
			
			if(cls.class_name==list.get(i)&&list.get(i-1)=="~") {//�Ҹ���->body�� class���ο� ���ǵǾ�����->methodinfo���� set�������
					MethodInfo destructor=new MethodInfo();
					destructor.setMtdName("~"+list.get(i)+"()");//�Ҹ��� �̸� ����
					int end_body=find_end_index(i+3,list);//list[i+3]�� {,list[end_body]�� }����Ű��
					String body=null;
					for(int k=i+4;k<end_body;k++) {
							body+=list.get(k);//�� ���̰����� ��� body�� ����
					}
					destructor.setMtdBody(body);
					destructor.setType("void");//�Ҹ��� ��ȯ���� void
					cls.method_list.add(destructor);
				}
				else if(kw.kw_type.toString().contains(list.get(i))){
					System.out.println(list.get(i));
					
				}
			}
		}
	
	public int find_end_index(int start,ArrayList<String> arrlist) {//���� ��ȣ ������ �˶�, �̿� �����ϴ� �������� �������ִ� �޼ҵ�
		Stack<Integer> st = new Stack<Integer>();

		for(int i=start;;i++) {
			if(arrlist.get(i)=="{"||arrlist.get(i)=="(") {
				st.push(i);
			}//��ȣ�� ���� �� �ε���(��ġ)�� ���ÿ� ����
			else if(arrlist.get(i)==")"||arrlist.get(i)==")") {
				st.pop();
			}//��ȣ�� ������ ���� �ֱ��� ���°�ȣ�� ��ġ�� ����
			if(st.empty()) {
				return i;//���� ó���� ���� ��ȣ�� ���ÿ��� �������� ������ ���, ������ ���� ��ȣ�� �޼ҵ��� end ��ġ�̴�.
			}
		}
	}

public static void main(String[] args) {
		new Parsing();		
	}

}
class SaveKeyword {
	String[] kw_access= {"public","protected","private"};
	String[] kw_type= {"int","void","bool"};
	String kw_class="class";
}
class Tokenize {
	private ArrayList<String> res = new ArrayList<String>();
	public ArrayList<String> getRes() {return res;}
	
	public Tokenize(StringBuffer sb) {
		erase_comment(sb);
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
		StringTokenizer st = new StringTokenizer(s, ":{}();= ", true) ;
		
		while(st.hasMoreTokens()) {
			res.add(st.nextToken());
		}
		
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
			if(res.get(i).equals(" ")) {
				res.remove(i);
			}
		}
	}
}
class ClassInfo{
	String class_name;
	ArrayList<MethodInfo> method_list;
	FieldInfo[] field_list;
	int start_class;
	int end_class;//���۰� �� �ε���
	
}

class FieldInfo{
	String field_name;
	
}
class MethodInfo {//�޼ҵ� ��������
	private String mtd_name; //�޼ҵ� �̸�
	private String mtd_type;
	private String mtd_body;
	private String mtd_parameter_type;
	private String mtd_access;
	private int start_index,end_index; 
	
	public void setMtdAccess(String access) {mtd_access=access;}
	public void setMtdName(String name) {mtd_name=name;}
	public void setMtdBody(String body) {mtd_body=body;}
	public void setType(String type) {mtd_type=type;}
	public void setParameterType(String Ptype) {mtd_parameter_type=Ptype;}
	//type�� Ptype�� parsing class���� keyword�� ���� �̰� method���� �����ϸ鼭 �����ٰ��̴�.

	public MethodInfo(String class_name,String mtd_name, StringBuffer sb) {
		find_mtd_body(class_name,mtd_name,sb);
		System.out.println(mtd_body);
	}
	
	public MethodInfo() {
		
	}
	public void find_mtd_body(String class_name, String name, StringBuffer sb) {//�޼ҵ� �������� ������ �̿��Ͽ� body�� mtd_body�ʵ忡 ��Ʈ�� ���·� ����
		String method_form=class_name+"::"+name;
		
		//find_body
		int start=sb.indexOf(method_form);
		start_index=sb.indexOf("{",start);
		find_end_index(start_index,sb);
		mtd_body=sb.substring(start_index+1, end_index);//�������� ����, ������ ���� ���ϴ� �޼ҵ��̹Ƿ� ������+1�Ͽ� ��ȣ�� ��������.
		
//		//find method type(��ȯ��), find parameter type(�Ű����� �ڷ���)
//		if(class_name==name||"~"+class_name==name) {
//			mtd_type="void";		
//		}
//		else {
//			for(int i=0; i<res.size();i++) {
//				if(res.get(i)+res.get(i+1)+res.get(i+2)==class_name+"::"+name) {
//					mtd_type=res.get(i-1);//'Class::method'�ٷ� �տ� ��ġ�� ��ȯ���� ����
//					if(res.get(i+3)+res.get(i+4)=="()")
//						mtd_parameter_type="";//��ȣ �ȿ� �ƹ��͵� ������ �Ű����� ����
//					else()					
//				}
//			}
//		}
		//���Ŀ� Parsing class���� method���� �ĺ��ϰ� ��ü �����ÿ� set~ �޼ҵ� ����Ͽ� �Ű������� �޼ҵ��� �ڷ��� �����ϴ� �������� �� ����
	}
	
	public void find_end_index(int start,StringBuffer sb) {//���� ��ȣ ������ �˶�, �̿� �����ϴ� �������� �������ִ� �޼ҵ�
		Stack<Integer> st = new Stack<Integer>();

		for(int i=start;;i++) {
			if(sb.charAt(i)=='{'||sb.charAt(i)=='(') {
				st.push(i);
			}//��ȣ�� ���� �� �ε���(��ġ)�� ���ÿ� ����
			else if(sb.charAt(i)=='}'||sb.charAt(i)==')') {
				st.pop();
			}//��ȣ�� ������ ���� �ֱ��� ���°�ȣ�� ��ġ�� ����
			if(st.empty()) {
				end_index=i;//���� ó���� ���� ��ȣ�� ���ÿ��� �������� ������ ���, ������ ���� ��ȣ�� �޼ҵ��� end ��ġ�̴�.
				break;
			}
		}
	}
}
