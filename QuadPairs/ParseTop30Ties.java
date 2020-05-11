package bytepairs;

/*
 * This program expects two input files with names of top30row.csv and top30col.csv
 * The output from Matlab can include ties for the 30th
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class ParseTop30Ties
{
	static String rowFile = "top30row.csv";
	static String colFile = "top30col.csv";
	static HashMap<String, String> bitMap = new HashMap<String, String>();
	static Scanner textScanner1 = null;
	static Scanner textScanner2 = null;
	static FileWriter writer = null;
	static String tempLineLeft = null;
	static String tempLineRight = null;
	static String tempLeftBits = null;
	static String tempRightBits = null;
	static StringBuilder sb1 = new StringBuilder();
	static StringBuilder sb2 = new StringBuilder();
	static StringBuilder sb3 = new StringBuilder();
	static StringBuilder sb4 = new StringBuilder();
	
	public static void main(String[] args)
	{
		String outputFile = "top30.txt";
		File file = new File(outputFile);
		
		bitMap.put("00", "A");
		bitMap.put("01", "C");
		bitMap.put("10", "G");
		bitMap.put("11", "T");
		
		try
		{
			file.createNewFile();
			writer = new FileWriter(file);
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
	
		try //check for file existence
		{
			textScanner1 = new Scanner(new File(rowFile));
			textScanner2 = new Scanner(new File(colFile));
		}				
		catch (FileNotFoundException e)
		{
			System.out.println("Error opening " + rowFile + " or " + colFile + " (file not found). Now exiting.");
		    System.exit(1);
		}
		
		System.out.println("Starting reading input files.");
		
		while (textScanner1.hasNextLine())
		{
			tempLineLeft = textScanner1.nextLine();
			tempLineRight = textScanner2.nextLine();
			
			tempLeftBits = Integer.toBinaryString(Integer.parseInt(tempLineLeft));
			tempRightBits = Integer.toBinaryString(Integer.parseInt(tempLineRight));
			sb1.setLength(0);
			sb2.setLength(0);
				
				if(tempLeftBits.length() < 16)
				{
					for(int j=0; j<16-tempLeftBits.length(); j++)
					{
						sb1.append("0");
					}
				}
				sb1.append(tempLeftBits);
				
				if(tempRightBits.length() < 16)
				{
					for(int j=0; j<16-tempRightBits.length(); j++)
					{
						sb2.append("0");
					}
				}
				sb2.append(tempRightBits);
				
				//convert to A,C,G,T
				sb3.setLength(0);
				for(int k=0; k<16; k+=2)
				{
					sb3.append(bitMap.get(sb1.subSequence(k, k+2)));
				}
				sb4.setLength(0);
				for(int k=0; k<16; k+=2)
				{
					sb4.append(bitMap.get(sb2.subSequence(k, k+2)));
				}
				
				try
				{
					writer.write(sb3.toString() + sb4.toString() + "\n");
				}
			    catch (IOException e)
				{
					e.printStackTrace();
				}
			}		
		try
	    {
			writer.flush();
		    writer.close();
		}
	    catch (IOException e)
	    {
			e.printStackTrace();
		}		
		System.out.println("Finished reading files. Wrote " + outputFile);
	}
}