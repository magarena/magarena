package magic.data;
import java.io.*;
import java.util.Scanner;

import magic.MagicMain;

public class ReadMeFile {
	private final String fileName;
	public ReadMeFile(final String fileName){
			this.fileName=fileName;
	}
	public String getDataFromFile() throws IOException{
		 StringBuilder text = new StringBuilder();
		 String NL = System.getProperty("line.separator");
		 Scanner scanner = new Scanner(new FileInputStream(fileName));
		 try{
		      while (scanner.hasNextLine()){
		        text.append(scanner.nextLine() + NL);
		      }
		 }
		 finally{
		      scanner.close();
		 } 
		return text.toString();
	}
}
