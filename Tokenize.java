package Test;

import java.util.*;

public class Tokenize {
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
			n++; //\n까지만 지웠을 땐 다음 문자 전에 \n가 적용돼서 나왔었음
			sb.delete(k,n);// "//"과 "\n"사이를 delete해줌
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
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer("Stack::Stack(int s:s){body}; //comment\nsize = 10;");
		Tokenize t = new Tokenize(sb);
		for(int i = 0; i < t.getRes().size(); i++) {
			System.out.println("Token[" + i + "] : " + t.getRes().get(i));
		}
	}

}
