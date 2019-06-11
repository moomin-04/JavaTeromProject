import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.*;
import java.util.*;

public class Parsing {//파싱해주는 클래스
	ReadFileData f=new ReadFileData("./Stack.h");
	public Tokenize tk=new Tokenize(f.getSb());
	ArrayList<String> res=tk.getRes();
	StringBuffer sb=new StringBuffer(tk.sb_without_comment);
	static int methodcnt;
	static int fieldcnt;//메소드 및 필드 개수 카운트
	
	public Parsing() {//parsing 생성자
		find_class(res);
	}
	public void find_class(ArrayList<String> list) {
		int i=list.indexOf("class");//class 위치 찾기
		ClassInfo cls=new ClassInfo();//class정보저장 객체생성
		cls.class_name=list.get(i+1);//"class" 다음에 나오는(i+1위치) 클래스 이름 저장
		cls.start_class=list.indexOf("{");
		cls.end_class=find_end_index(cls.start_class,list);//시작과 끝 지점 저장
		System.out.print(cls.end_class);
		find_method(list,cls);
		}
	
	public void find_method(ArrayList<String> list,ClassInfo cls) {
		SaveKeyword kw=new SaveKeyword();
		String access = null;
		for(int i=cls.start_class;i<cls.end_class;i++) {//class내부에 선언된 메소드와 필드를 찾아보자
//			System.out.print(list.get(i)+"/");
			if(kw.kw_access.contains(list.get(i))){
				access=list.get(i);					
			}
			System.out.println(i+access);
			if(cls.class_name.equals(list.get(i))&&list.get(i-1).equals("~")) {//소멸자->body가 class내부에 정의되어있음->methodinfo따로 set해줘야함
					MethodInfo destructor=new MethodInfo();
					destructor.setName("~"+list.get(i)+"()");//소멸자 이름 설정
					int end_body=find_end_index(i+3,list);//list[i+3]은 {,list[end_body]는 }가르키게
					String body=null;
					for(int k=i+4;k<end_body;k++) {
							body+=list.get(k);//그 사이값들은 모두 body에 저장
					}
					destructor.setBody(body);
					destructor.setType("void");//소멸자 반환형은 void
					destructor.setAccess(access);
					cls.addMethod(destructor);
					methodcnt++;
				}
			
				else if(cls.class_name.equals(list.get(i))) {//생성자	
					System.out.println("생성:"+list.get(i));
					cls.method_list.add(new MethodInfo(cls.class_name,cls.class_name,sb));
					System.out.print(cls.getMethod().get(methodcnt).getAccess());
					//cls.addMethod(new MethodInfo(cls.class_name,cls.class_name,sb));
					cls.getMethod().get(methodcnt).setAccess(access);
					cls.getMethod().get(methodcnt).setParameterType(list.get(i+2));//i:메소드이름, i+1:괄호(, i+2:parameter type
					cls.getMethod().get(methodcnt).setType("void");
					methodcnt++;
					
				}
				else if(kw.kw_type.toString().contains(list.get(i))&&(list.get(i-1).equals(":")||list.get(i-1).equals(";"))) {
					
				//i가 일반 메소드 또는 필드의 반환형을 가리킬 때, parameter의 type과 구분 위해 :또는 ;뒤에 나오는 반환형만 해당
					String name=list.get(i+1);//반환형 다음엔 이름
					if(list.get(i+2).equals("(")) {//이름 뒤에 괄호가 나오면 메소드, 아니면 필드
						System.out.println("메소드:"+list.get(i+1));

						cls.addMethod(new MethodInfo(cls.class_name,name,sb));
						cls.getMethod().get(methodcnt).setType(list.get(i));
						cls.getMethod().get(methodcnt).setAccess(access);
						String p_type=(list.get(i+3).equals(")")?null:list.get(i+3));
						//괄호안에 아무것도 없으면 parameter type=null, 아니면 parameter type=i+3
						cls.getMethod().get(methodcnt).setParameterType(p_type);
					
						methodcnt++;
					}
					else {//필드
						if(list.get(i+1).equals("*")) {//포인터일때는 *까지 type에 포함 후 이름 set
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
						System.out.println("필드:"+
						cls.getField().get(fieldcnt).getName()+fieldcnt+"번쨰");
						fieldcnt++;
					}
					
				}
			}
		//for문
		for(int i=0;i<cls.method_list.size();i++) {
			System.out.println(cls.method_list.get(i).getName());
			}
		System.out.println("then,");
		for(int i=0;i<cls.field_list.size();i++) {
				System.out.println(cls.field_list.get(i).getName());
				}
		//메소드 내부에 있는 field 찾아서 서로 저장 해주는 메소드? 만들어야함!
	}
	
	public int find_end_index(int start,ArrayList<String> arrlist) {//시작 괄호 지점을 알때, 이에 상응하는 끝지점을 저장해주는 메소드
		Stack<Integer> st = new Stack<Integer>();

		for(int i=start;;i++) {
			if(arrlist.get(i).equals("{")||arrlist.get(i).equals("(")) {
				st.push(i);
				System.out.print("push"+i+":"+arrlist.get(i));
			}//괄호를 열면 그 인덱스(위치)를 스택에 저장
			else if(arrlist.get(i).equals("}")||arrlist.get(i).equals(")")) {
				st.pop();
				System.out.print("pop"+i+":"+arrlist.get(i));

			}//괄호를 닫으면 가장 최근의 여는괄호의 위치를 삭제
			if(st.empty()) {
				System.out.println("empty"+i+":"+arrlist.get(i));
				return i;//가장 처음에 열린 괄호가 스택에서 빠져나와 스택이 비면, 마지막 닫은 괄호가 메소드의 end 위치이다.
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

//stringbuffer 형식으로 저장된 코드의 주석을 제거하고 토큰화해주는 클래스
class Tokenize {
	private ArrayList<String> res = new ArrayList<String>();
	StringBuffer sb_without_comment=new StringBuffer();
	public ArrayList<String> getRes() {return res;}
	
	public Tokenize(StringBuffer sb) {
		erase_comment(sb);
		sb_without_comment=sb;
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
		StringTokenizer st = new StringTokenizer(s, "*+?->~!:{}();=[]\n\r"
				+ "\r\n"+"\t"+" ", true) ;
		
			
		while(st.hasMoreTokens()) {
			res.add(st.nextToken());
		}
		System.out.print(res.size());
//		String temp=null;
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

//정보저장 클래스

class ClassInfo{//클래스 정보 저장
	String class_name;
	ArrayList<MethodInfo> method_list=new ArrayList<MethodInfo>();//클래스 내부의 method,field list 저장
	ArrayList<FieldInfo> field_list=new ArrayList<FieldInfo>();
	int start_class;
	int end_class;//시작과 끝 인덱스
	
	public void addMethod(MethodInfo m) {method_list.add(m);}
	public ArrayList<MethodInfo> getMethod() {return method_list;}
	public void addField(FieldInfo f) {field_list.add(f);}
	public ArrayList<FieldInfo> getField() {return field_list;}
}

class MemberInfo{
	private String name; //이름
	private String type; //반환형
	private String access;//접근지정자
	protected int start_index;
	protected int end_index; 
	
	public void setAccess(String access) {this.access=access;}
	public String getAccess() {return access;}
	public void setName(String name) {this.name=name;}
	public String getName() {return name;}
	public void setType(String type) {this.type=type;}
	public String getType() {return type;}

}

class FieldInfo extends MemberInfo{//필드 정보저장
	private ArrayList<MethodInfo> method=new ArrayList<MethodInfo>();
	
	public void addMethod(MethodInfo m) {method.add(m);}
	public ArrayList<MethodInfo> getMethod() {return method;}
	
	public FieldInfo() {
		
	}
}


class MethodInfo extends MemberInfo{//메소드 정보저장
	private String body;
	private String parameter_type;
	private ArrayList<FieldInfo> field=new ArrayList<FieldInfo>();
	
	public void setBody(String body) {this.body=body;}
	public String getBody() {return body;}
	public void addField(FieldInfo f) {field.add(f);}
	public ArrayList<FieldInfo> getField() {return field;}
	public void setParameterType(String parameter_type) {this.parameter_type=parameter_type;}
	public String getParameterType() {return parameter_type;}
	
	public MethodInfo(String class_name,String mtd_name, StringBuffer sb) {//클래스 네임 말고 클래스인포형이
		this.setName(mtd_name);
		find_mtd_body(class_name,mtd_name,sb);
	}
	
	public MethodInfo() {//소멸자에서만 쓰일 계획
		
	}
	public void find_mtd_body(String class_name, String name, StringBuffer sb) {
	//메소드 시작점과 끝점을 이용하여 body를 mtd_body필드에 스트링 형태로 저장
		
		String method_form=class_name+"::"+name;
		
		//find_body
		int start=sb.indexOf(method_form);
		start_index=sb.indexOf("{",start);
		find_end_index(start_index,sb);
		body=sb.substring(start_index+1, end_index);
		//시작점은 포함, 끝점은 포함 안하는 메소드이므로 시작점+1하여 괄호는 지워야함.
		
	}
	
	public void find_end_index(int start,StringBuffer sb) {
	//시작 괄호 지점을 알때, 이에 상응하는 끝지점을 저장해주는 메소드
		Stack<Integer> st = new Stack<Integer>();

		for(int i=start;;i++) {
			if(sb.charAt(i)=='{'||sb.charAt(i)=='(') {
				st.push(i);
			}//괄호를 열면 그 인덱스(위치)를 스택에 저장
			else if(sb.charAt(i)=='}'||sb.charAt(i)==')') {
				st.pop();
			}//괄호를 닫으면 가장 최근의 여는괄호의 위치를 삭제
			if(st.empty()) {
				end_index=i;
				//가장 처음에 열린 괄호가 스택에서 빠져나와 스택이 비면, 마지막 닫은 괄호가 메소드의 end 위치이다.
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
	public void read(String address){//불러오고자 하는 파일의 주소를 매개변수로 받아오면 파일 읽는 메소드
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
