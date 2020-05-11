package peptides;

/* Counts a FASTA file of peptide encoded files, assuming no headers
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Scanner;

public class PeptideCounterDemo
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
		if (args.length != 2)
		{
			System.out.println("Expecting two command-line arguments (filename and 6 or 8). Now exiting PeptideCounter.");
			System.exit(1);
		}

		String fileName = args[0];
		Integer kmer = Integer.parseInt(args[1]);
		
		System.out.println("Reading file: " + fileName);
		
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

		if(kmer==6)
		{
			SextetProg sp = new SextetProg();
			sp.ZeroTripletPairs();
			sp.BuildPeptideMap();
					
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
				sp.CountSextets(peptides);	
				empty = false;
			}
			textScanner.close();
			sp.GetStats();
		}
		
		if(kmer==8)
		{
			OctetProg op = new OctetProg();
			op.ZeroQuadPairs();
			op.BuildPeptideMap();
					
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
			op.GetStats();
		}
				
		calendar = Calendar.getInstance();
		clock = new Timestamp(calendar.getTimeInMillis());
		stopTime = clock.getTime();

		Double totalTime = (double) (stopTime - startTime);
		System.out.println("Total time (in seconds): " + (totalTime)/1000);
	}
}