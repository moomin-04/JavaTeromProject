//StringBuffer로 받아온 문자열의 기호 앞 뒤에 공백(_) 추가하기 - :: 아직 해결 못함, 중복되는 코드 단순화 하는 방법 아직 못 찾음. 
package Test;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer("Stack::Stack(int s){size=s>0?s:10;}") ;
		
		int index_string = 0;
		int[] index_sign = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		int LastIndex = sb.length();
		int small_index_sign = 0;
		int large_index_sign = 0;
		int i = 0;
		
		while(true) {
			index_sign[0] = sb.indexOf(":", index_sign[0]);
			index_sign[1] = sb.indexOf("(", index_sign[1]);
			index_sign[2] = sb.indexOf(")", index_sign[2]);
			index_sign[3] = sb.indexOf("{", index_sign[3]);
			index_sign[4] = sb.indexOf("}", index_sign[4]);
			index_sign[5] = sb.indexOf("=", index_sign[5]);
			index_sign[6] = sb.indexOf("?", index_sign[6]);
			index_sign[7] = sb.indexOf(";", index_sign[7]);
			index_sign[8] = sb.indexOf(">", index_sign[8]);
			
			if(index_string == index_sign[0]) {
				index_sign[0] = insert_whitespace(0, index_string, index_sign[0], sb);
			}
			else if(index_string == index_sign[1]) {
				index_sign[1] = insert_whitespace(1, index_string, index_sign[1], sb);
			}
			else if(index_string == index_sign[2]) {
				index_sign[2] = insert_whitespace(2, index_string, index_sign[2], sb);	
			}
			if(index_string == index_sign[3]) {
				index_sign[3] = insert_whitespace(3, index_string, index_sign[3], sb);
			}
			else if(index_string == index_sign[4]) {
				index_sign[4] = insert_whitespace(4, index_string, index_sign[4], sb);
			}
			else if(index_string == index_sign[5]) {
				index_sign[5] = insert_whitespace(5, index_string, index_sign[5], sb);	
			}
			if(index_string == index_sign[6]) {
				index_sign[6] = insert_whitespace(6, index_string, index_sign[6], sb);
			}
			else if(index_string == index_sign[7]) {
				index_sign[7] = insert_whitespace(7, index_string, index_sign[7], sb);
			}
			else if(index_string == index_sign[8]) {
				index_sign[8] = insert_whitespace(8, index_string, index_sign[8], sb);	
			}
			
			index_string ++;
						
			if(index_string == sb.length()) {
				System.out.println("길이 : " + sb.length());
				break;
			}
		}
		
		String s = sb.toString();
		System.out.println("반환 값 : " + s);
	}
	
	static private int insert_whitespace(int index, int string, int sign, StringBuffer ssb) {
		int smaller = sign;
		int larger = sign + 2;
		StringBuffer sbb = ssb;
		
		smaller = sign;
		larger = sign + 2;
		sbb.insert(smaller, "_");
		sbb.insert(larger, "_");
		System.out.println("index_sign[" + index + "] : "  + sign);
		return (sign = sign + 2);
	}
	
//중복되는 코드 함수로 바꿔보려는 시도 중
//		static private void repeatation_increasing(int index, int string, int sign, StringBuffer sb) {
//		int x = 0;
//		int[] s = new int[10];
//		s[index] = sign;
//		for(x = 0; x < 9; x++) {
//			if(string == s[index]) {
//				sign[x] = insert_whitespace(x, index_string, index_sign[8], sb);
//			}
//		}
//	}
}
