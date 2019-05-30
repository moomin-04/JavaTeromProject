import java.util.*;

public class MethodInfo {//메소드 정보저장
	private String mtd_name; //메소드 이름
	private String mtd_type;
	private String mtd_body;
	private String mtd_parameter_type;
	private int start_index,end_index; 
	
	public void setType(String type) {mtd_type=type;}
	public void setParameterType(String Ptype) {mtd_parameter_type=Ptype;}
	//type와 Ptype은 parsing class에서 keyword를 통해 이게 method인지 구분하면서 지어줄것이다.

	public MethodInfo(String class_name,String mtd_name, StringBuffer sb) {
		find_mtd_body(class_name,mtd_name,sb);
		System.out.println(mtd_body);
	}
	
	public void find_mtd_body(String class_name, String name, StringBuffer sb) {//메소드 시작점과 끝점을 이용하여 body를 mtd_body필드에 스트링 형태로 저장
		String method_form=class_name+"::"+name;
		
		//find_body
		int start=sb.indexOf(method_form);
		start_index=sb.indexOf("{",start);
		find_end_index(start_index,sb);
		mtd_body=sb.substring(start_index+1, end_index);//시작점은 포함, 끝점은 포함 안하는 메소드이므로 시작점+1하여 괄호는 지워야함.
		
//		//find method type(반환형), find parameter type(매개변수 자료형)
//		if(class_name==name||"~"+class_name==name) {
//			mtd_type="void";		
//		}
//		else {
//			for(int i=0; i<res.size();i++) {
//				if(res.get(i)+res.get(i+1)+res.get(i+2)==class_name+"::"+name) {
//					mtd_type=res.get(i-1);//'Class::method'바로 앞에 위치한 반환형을 저장
//					if(res.get(i+3)+res.get(i+4)=="()")
//						mtd_parameter_type="";//괄호 안에 아무것도 없으면 매개변수 없음
//					else()					
//				}
//			}
//		}
		//이후에 Parsing class에서 method인지 식별하고 객체 생성시에 set~ 메소드 사용하여 매개변수와 메소드의 자료형 설정하는 방향으로 할 예정
	}
	
	public void find_end_index(int start,StringBuffer sb) {//시작 괄호 지점을 알때, 이에 상응하는 끝지점을 저장해주는 메소드
		Stack<Integer> st = new Stack<Integer>();

		for(int i=start;;i++) {
			if(sb.charAt(i)=='{'||sb.charAt(i)=='(') {
				st.push(i);
			}//괄호를 열면 그 인덱스(위치)를 스택에 저장
			else if(sb.charAt(i)=='}'||sb.charAt(i)==')') {
				st.pop();
			}//괄호를 닫으면 가장 최근의 여는괄호의 위치를 삭제
			if(st.empty()) {
				end_index=i;//가장 처음에 열린 괄호가 스택에서 빠져나와 스택이 비면, 마지막 닫은 괄호가 메소드의 end 위치이다.
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
