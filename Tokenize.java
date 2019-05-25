import java.util.*;
public class Tokenize {
	
	//Stack.h를 tokenize하기 이전에 주석을 지워주는 메소드
	public void erase_comment(StringBuffer s){
		int n=0,k=0;
		
		while(k!=-1) {			
			k=s.indexOf("//",k+1);//'//'의 위치 인덱스를  k에 저장
			n=s.indexOf("\n",k);// '//'이후에 처음 나오는 '\n'의 위치 인덱스를 n에 저장
			s.delete(k,n);// "//"과 "\n"사이를 delete해줌
		}
	}
	//공백기준으로 StringBuffer를 나눈 후 {,(,),},;등도 모두 토큰화해줌
	public ArrayList<String> tokenizer(StringBuffer sb){
		String s=sb.toString();
		
		//공백문자를 기준으로 tokenizer
		StringTokenizer st=new StringTokenizer(s);
		ArrayList<String> res=new ArrayList<String>();//나뉜 token을 ArrayList로 저장
		for(int i=0; i<st.countTokens();i++) {
			res.add(st.nextToken());
		}
		
		//공백 이외 모두 토큰화
		Iterator<String> res_it=res.iterator();
		while(res_it.hasNext()) {
			switch(res_it.next()) {
			case()
			}
		}
		return res;
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
