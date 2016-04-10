import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;

public class DictMake {
	public static void main(String[] args){
		ArrayList<String> words = new ArrayList<String>();
		Scanner sc;
		int size1 = 0;
		int size2 = 0;
		
		
		// Open a file, read it, add words to list
		try{
			BufferedReader br = new BufferedReader(new FileReader(args[0]));
			sc = new Scanner(br);
			while(sc.hasNext()){
				words.add(sc.next());
				size1++;
			}
			sc.close();
		}catch (Exception e){
			System.out.println(e);
		}
		
		// List acts as queue, words are written to output if
		// they are at least 5 characters long.
		try {
			PrintWriter writer = new PrintWriter("Fixed_list.txt", "UTF-8");
			while(!words.isEmpty()){
				String s = words.remove(0);
				if(s.length() > 4 && !s.contains("-")){
					writer.println(s);
					size2++;
				}
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		System.out.println("Done! Put in " + size2 + " words out of " + size1);
	}
}
