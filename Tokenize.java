import java.util.*;
public class Tokenize {
	
	//Stack.h�� tokenize�ϱ� ������ �ּ��� �����ִ� �޼ҵ�
	public void erase_comment(StringBuffer s){
		int n=0,k=0;
		
		while(k!=-1) {			
			k=s.indexOf("//",k+1);//'//'�� ��ġ �ε�����  k�� ����
			n=s.indexOf("\n",k);// '//'���Ŀ� ó�� ������ '\n'�� ��ġ �ε����� n�� ����
			s.delete(k,n);// "//"�� "\n"���̸� delete����
		}
	}
	//����������� StringBuffer�� ���� �� {,(,),},;� ��� ��ūȭ����
	public ArrayList<String> tokenizer(StringBuffer sb){
		String s=sb.toString();
		
		//���鹮�ڸ� �������� tokenizer
		StringTokenizer st=new StringTokenizer(s);
		ArrayList<String> res=new ArrayList<String>();//���� token�� ArrayList�� ����
		for(int i=0; i<st.countTokens();i++) {
			res.add(st.nextToken());
		}
		
		//���� �̿� ��� ��ūȭ
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
