package octet;

/* Counts a FASTA file of peptide encoded files, assuming no headers
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Scanner;

public class OctetProgDemo
{
	static Scanner textScanner = null;
	static Boolean empty = true;
	static String nextLine = null;
	static StringBuilder sb = new StringBuilder();
	static String peptides = null;
	static long startTime = 0;
	static long stopTime = 0;

				
	public static void main(String[] args)
	{		
		if (args.length != 1)
		{
			System.out.println("Expecting one file name as a command-line argument. Now exiting.");
			System.exit(1);
		}

		String fileName = args[0];
		
		OctetProg op = new OctetProg();
		op.ZeroTripletPairs();
		op.BuildPeptideMap();
		
		System.out.println("Reading file.");
		
		Calendar calendar = Calendar.getInstance();
		Timestamp clock = new Timestamp(calendar.getTimeInMillis());
		startTime = clock.getTime();
		
		try //check for file existence
		{
			textScanner = new Scanner(new File(fileName));
		}
		catch (FileNotFoundException e)
		{
			System.out.println("Error opening " + fileName + " (file not found). Now exiting.");
		    System.exit(1);
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
			op.CountOctets(peptides);
			
			empty = false;
		}
		
		textScanner.close();
				
		calendar = Calendar.getInstance();
		clock = new Timestamp(calendar.getTimeInMillis());
		stopTime = clock.getTime();

		Double totalTime = (double) (stopTime - startTime);
		System.out.println("Total time (in seconds): " + (totalTime)/1000);
		
		op.GetStats();
	}
}