import java.io.*;
public class ReadFileData {	
	static StringBuffer sb;

	public static void main(String[] args) {
		int b=0;
		StringBuffer buffer= new StringBuffer();
		FileInputStream file=null;
		try {
			file=new FileInputStream("./Stack.h");
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
