package peptides;

import java.io.BufferedWriter;

/* Splits FASTA input files (no headers) randomly based on a percentage 50/50 default.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class PeptideSplitterDemo
{
	static Scanner textScanner = null;
	static Boolean empty = true;
	static String nextLine = null;
	static StringBuilder sb = new StringBuilder();
	static String peptides = null;
	static Integer ratio = 0;
	static BufferedWriter writer1;
	static BufferedWriter writer2;
				
	public static void main(String[] args)
	{		
		if (args.length != 2)
		{
			System.out.println("Expecting one command-line arguments (filename and 0-50 split ratio). Now exiting PeptideSplitter.");
			System.exit(1);
		}

		String fileName = args[0];
		Integer ratio = Integer.parseInt(args[1]);
		Random pick = new Random();
		
		System.out.println("Reading file: " + fileName);
				
		try //check for file existence
		{
			textScanner = new Scanner(new File(fileName));
		}
		catch (FileNotFoundException e)
		{
			System.out.println("Error opening " + fileName + " (file not found). Now exiting.");
		    System.exit(1);
		}
		
		try
		{
			writer1 = new BufferedWriter(new FileWriter("left-side.txt"));
			writer2 = new BufferedWriter(new FileWriter("right-side.txt"));
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		
		while(textScanner.hasNextLine())
		{
			StringBuilder sb = new StringBuilder();
			
			while (!empty && textScanner.hasNextLine())
			{
				nextLine = textScanner.nextLine();
				if(!nextLine.isEmpty())
				{
					sb = sb.append(nextLine);
				}
				else
				{
					empty = true;
				}
			}
			peptides = sb.toString();
			if(pick.nextInt(100) < ratio)
			{
				try
				{
					writer1.write(peptides + "\n\n");
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}
			}
			else
			{
				try
				{
					writer2.write(peptides + "\n\n");
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}
			}
			empty = false;
		}
		textScanner.close();
		System.out.println("Finished writing left-half.txt and right-half.txt");
	}
}
