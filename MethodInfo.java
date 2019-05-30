import java.util.*;

public class MethodInfo {//�޼ҵ� ��������
	private String mtd_name; //�޼ҵ� �̸�
	private String mtd_type;
	private String mtd_body;
	private String mtd_parameter_type;
	private int start_index,end_index; 
	
	public void setType(String type) {mtd_type=type;}
	public void setParameterType(String Ptype) {mtd_parameter_type=Ptype;}
	//type�� Ptype�� parsing class���� keyword�� ���� �̰� method���� �����ϸ鼭 �����ٰ��̴�.

	public MethodInfo(String class_name,String mtd_name, StringBuffer sb) {
		find_mtd_body(class_name,mtd_name,sb);
		System.out.println(mtd_body);
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
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StringBuffer test=new StringBuffer("Stack::Stack(int s) {\nsize=s>0?s:10;\ntop=-1;\nptr=new int[size];}");
			
		new MethodInfo("Stack","Stack",test);
	}

}
