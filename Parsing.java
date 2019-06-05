import java.util.*;

public class Parsing {//파싱해주는 클래스
	ReadFileData f=new ReadFileData();
	public Tokenize tk=new Tokenize(f.getSb());
	ArrayList<String> res=tk.getRes();
	public Parsing() {
		find_class(res);
	}
	public void find_class(ArrayList<String> list) {
		int i=list.indexOf("class");//class 위치 찾기
		ClassInfo cls=new ClassInfo();//class정보저장 객체생성
		cls.class_name=list.get(i+1);//"class" 다음에 나오는(i+1위치) 클래스 이름 저장
		cls.start_class=i+2;
		cls.end_class=find_end_index(cls.start_class,list);//시작과 끝 지점 저장
		find_method(list,cls);
		}
	public void find_method(ArrayList<String> list,ClassInfo cls) {
		SaveKeyword kw=new SaveKeyword();
		for(int i=cls.start_class;i<cls.end_class;i++) {//class내부에 선언된 메소드와 필드를 찾아보자
			
			if(cls.class_name==list.get(i)&&list.get(i-1)=="~") {//소멸자->body가 class내부에 정의되어있음->methodinfo따로 set해줘야함
					MethodInfo destructor=new MethodInfo();
					destructor.setMtdName("~"+list.get(i)+"()");//소멸자 이름 설정
					int end_body=find_end_index(i+3,list);//list[i+3]은 {,list[end_body]는 }가르키게
					String body=null;
					for(int k=i+4;k<end_body;k++) {
							body+=list.get(k);//그 사이값들은 모두 body에 저장
					}
					destructor.setMtdBody(body);
					destructor.setType("void");//소멸자 반환형은 void
					cls.method_list.add(destructor);
				}
				else if(kw.kw_type.toString().contains(list.get(i))){
					System.out.println(list.get(i));
					
				}
			}
		}
	
	public int find_end_index(int start,ArrayList<String> arrlist) {//시작 괄호 지점을 알때, 이에 상응하는 끝지점을 저장해주는 메소드
		Stack<Integer> st = new Stack<Integer>();

		for(int i=start;;i++) {
			if(arrlist.get(i)=="{"||arrlist.get(i)=="(") {
				st.push(i);
			}//괄호를 열면 그 인덱스(위치)를 스택에 저장
			else if(arrlist.get(i)==")"||arrlist.get(i)==")") {
				st.pop();
			}//괄호를 닫으면 가장 최근의 여는괄호의 위치를 삭제
			if(st.empty()) {
				return i;//가장 처음에 열린 괄호가 스택에서 빠져나와 스택이 비면, 마지막 닫은 괄호가 메소드의 end 위치이다.
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
	
	//주석을 지워주는 메소드
	public void erase_comment(StringBuffer sb){
		int n = 0, k = 0;
		
		while(true) {			
			k = sb.indexOf("//",k+1);//'//'의 위치 인덱스를  k에 저장
			if(k == -1) break;
			n = sb.indexOf("\n",k);// '//'이후에 처음 나오는 '\n'의 위치 인덱스를 n에 저장
			sb.delete(k,n+1);// "//"과 "\n"사이를 delete해줌
		}
	}
	
	//주석을 제거한 StringBuffer를 기호에 따라 토큰화
	public void tokenizer(StringBuffer sb){
		String s=sb.toString();
		
		//구별기호 :{}();=
		StringTokenizer st = new StringTokenizer(s, ":{}();= ", true) ;
		
		while(st.hasMoreTokens()) {
			res.add(st.nextToken());
		}
		
		//::, whitespace 처리
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
	int end_class;//시작과 끝 인덱스
	
}

class FieldInfo{
	String field_name;
	
}
class MethodInfo {//메소드 정보저장
	private String mtd_name; //메소드 이름
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
	//type와 Ptype은 parsing class에서 keyword를 통해 이게 method인지 구분하면서 지어줄것이다.

	public MethodInfo(String class_name,String mtd_name, StringBuffer sb) {
		find_mtd_body(class_name,mtd_name,sb);
		System.out.println(mtd_body);
	}
	
	public MethodInfo() {
		
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
}
