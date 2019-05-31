package Test;

import java.util.*;

public class Tokenize {
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
			n++; //\n������ ������ �� ���� ���� ���� \n�� ����ż� ���Ծ���
			sb.delete(k,n);// "//"�� "\n"���̸� delete����
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
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer("Stack::Stack(int s:s){body}; //comment\nsize = 10;");
		Tokenize t = new Tokenize(sb);
		for(int i = 0; i < t.getRes().size(); i++) {
			System.out.println("Token[" + i + "] : " + t.getRes().get(i));
		}
	}

}
