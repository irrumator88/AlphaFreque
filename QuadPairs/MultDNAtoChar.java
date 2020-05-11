package bytepairs;

/* This program converts protein strings into 2-bit encoding, then into 8-bit ASCII
 * 00=A, 01=C, 10=G, 11=T
 * It reads multiple proteins from a file (1st argument), then human, ecoli, or random for 2nd argument
 * It assumes the file is in peptide coding, with no headers (>)
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class MultDNAtoChar
{
	static Scanner textScanner = null;
	static FileWriter writer = null;
	static Boolean empty = true;
	static String nextLine = null;
	static StringBuilder sb = new StringBuilder();
	static String protein = null;
	static String bits = null;
	static String ascii = null;
	static Character inputChar = null;
	static String outputChar = null;
	static String bitOutput = null;
	static String bitInput = null;
	static int asciiOutput = 0;
	static String nucleotides = "acgt";
	static final String testInput1 = "acgtaaaaccccggggttttacgt";
	static final String testInput2 = "tcgagagaggtgtactttttacgtacgt";
	static final String testInput3 = "tcgagagaggtgtactttttacgtacgta";
	static final String testInput4 = "tcgagagaggtgtactttttacgtacgtag";
	static final String testInput5 = "tcgagagaggtgtactttttacgtacgtagt";
	static final ArrayList<String> testInputs = new ArrayList<String>();
	static String testOutput = null;

	static HashMap<String, ArrayList<String>> peptideMap = new HashMap<String, ArrayList<String>>();
	static HashMap<Character, String> dnaMap = new HashMap<Character, String>();
	static List<List<String>> output = new ArrayList<List<String>>();
	static HashMap<Character, String> ecoliMap = new HashMap<Character, String>();
	static HashMap<Character, String> humanMap = new HashMap<Character, String>();

	static ArrayList<String> aList = new ArrayList<String>();
	static ArrayList<String> rList = new ArrayList<String>();
	static ArrayList<String> nList = new ArrayList<String>();
	static ArrayList<String> dList = new ArrayList<String>();
	static ArrayList<String> cList = new ArrayList<String>();
	static ArrayList<String> qList = new ArrayList<String>();
	static ArrayList<String> eList = new ArrayList<String>();
	static ArrayList<String> gList = new ArrayList<String>();
	static ArrayList<String> hList = new ArrayList<String>();
	static ArrayList<String> iList = new ArrayList<String>();
	static ArrayList<String> lList = new ArrayList<String>();
	static ArrayList<String> kList = new ArrayList<String>();
	static ArrayList<String> mList = new ArrayList<String>();
	static ArrayList<String> fList = new ArrayList<String>();
	static ArrayList<String> pList = new ArrayList<String>();
	static ArrayList<String> sList = new ArrayList<String>();
	static ArrayList<String> tList = new ArrayList<String>();
	static ArrayList<String> wList = new ArrayList<String>();
	static ArrayList<String> yList = new ArrayList<String>();
	static ArrayList<String> vList = new ArrayList<String>();
	static ArrayList<String> xList = new ArrayList<String>();
	static ArrayList<String> bList = new ArrayList<String>();
	static ArrayList<String> zList = new ArrayList<String>();

	static long startTime = 0;
	static long stopTime = 0;

	protected static void BuildTestInputs()
	{
		testInputs.add(testInput1);
		testInputs.add(testInput2);
		testInputs.add(testInput3);
		testInputs.add(testInput4);
		testInputs.add(testInput5);
	}
	
	protected static void BuildDNAMap()
	{		
		dnaMap.put('a', "00");
		dnaMap.put('A', "00");
		dnaMap.put('c', "01");
		dnaMap.put('C', "01");
		dnaMap.put('g', "10");
		dnaMap.put('G', "10");
		dnaMap.put('t', "11");
		dnaMap.put('T', "11");
	}
	
	protected static void BuildEcoliMap()
	{
		//This chooses the most-common triplet percentage from ecoli optimization table
		ecoliMap.put('A', "GCG");
		ecoliMap.put('R', "CGT");
		ecoliMap.put('N', "AAC");
		ecoliMap.put('D', "GAT");
		ecoliMap.put('C', "TGC");
		ecoliMap.put('Q', "CAG");
		ecoliMap.put('E', "GAA");
		ecoliMap.put('G', "GGC");
		ecoliMap.put('H', "CAT");
		ecoliMap.put('I', "ATT");
		ecoliMap.put('L', "CTG");
		ecoliMap.put('K', "AAA");
		ecoliMap.put('M', "ATG");
		ecoliMap.put('F', "TTT");
		ecoliMap.put('P', "CCG");
		ecoliMap.put('S', "AGC");
		ecoliMap.put('T', "ACC");
		ecoliMap.put('W', "TGG");
		ecoliMap.put('Y', "TAT");
		ecoliMap.put('V', "GTG");

		ecoliMap.put('X', "TAA"); //unknown peptide
		ecoliMap.put('x', "TAA"); //unknown peptide
		ecoliMap.put('B', "TAA"); //unknown peptide
		ecoliMap.put('Z', "TAA"); //unknown peptide
	}
	protected static void BuildHumanMap()
	{
		//This chooses the most-common triplet percentage from human optimization table
		humanMap.put('A', "GCC");
		humanMap.put('R', "CGG");
		humanMap.put('N', "AAC");
		humanMap.put('D', "GAC");
		humanMap.put('C', "TGC");
		humanMap.put('Q', "CAG");
		humanMap.put('E', "GAG");
		humanMap.put('G', "GGC");
		humanMap.put('H', "CAC");
		humanMap.put('I', "ATC");
		humanMap.put('L', "CTG");
		humanMap.put('K', "AAG");
		humanMap.put('M', "ATG");
		humanMap.put('F', "TTC");
		humanMap.put('P', "CCC");
		humanMap.put('S', "AGC");
		humanMap.put('T', "ACC");
		humanMap.put('W', "TGG");
		humanMap.put('Y', "TAC");
		humanMap.put('V', "GTG");

		humanMap.put('X', "TGA"); //unknown peptide
		humanMap.put('x', "TGA"); //unknown peptide
		humanMap.put('B', "TAA"); //unknown peptide
		humanMap.put('Z', "TAA"); //unknown peptide
	}
	
	protected static void BuildPeptideMap()
	{
		peptideMap.put("A", aList);
		peptideMap.put("R", rList);
		peptideMap.put("N", nList);
		peptideMap.put("D", dList);
		peptideMap.put("C", cList);
		peptideMap.put("Q", qList);
		peptideMap.put("E", eList);
		peptideMap.put("G", gList);
		peptideMap.put("H", hList);
		peptideMap.put("I", iList);
		peptideMap.put("L", lList);
		peptideMap.put("K", kList);
		peptideMap.put("M", mList);
		peptideMap.put("F", fList);
		peptideMap.put("P", pList);
		peptideMap.put("S", sList);
		peptideMap.put("T", tList);
		peptideMap.put("W", wList);
		peptideMap.put("Y", yList);
		peptideMap.put("V", vList);

		peptideMap.put("X", xList); //unknown peptide
		peptideMap.put("x", xList); //unknown peptide
		peptideMap.put("B", bList); //unknown peptide
		peptideMap.put("Z", zList); //unknown peptide
		
		//build our 64 mappings
		aList.add("GCT");
		aList.add("GCC");
		aList.add("GCA");
		aList.add("GCG");
		rList.add("CGT");
		rList.add("CGC");
		rList.add("CGA");
		rList.add("CGG");
		rList.add("AGA");
		rList.add("AGG");
		nList.add("ATT");
		nList.add("AAC");
		nList.add("GAC");
		nList.add("CAG");
		dList.add("GAT");
		cList.add("TGT");
		cList.add("TGC");
		qList.add("CAA");
		eList.add("GAA");
		gList.add("GGT");
		gList.add("GGC");
		gList.add("GGA");
		gList.add("GGG");
		hList.add("CAT");
		hList.add("CAC");
		iList.add("ATT");
		iList.add("ATC");
		iList.add("ATA");
		lList.add("TTA");
		lList.add("TTG");
		lList.add("CTT");
		lList.add("CTC");
		lList.add("CTA");
		lList.add("CTG");
		kList.add("AAA");
		kList.add("AAG");
		mList.add("ATG");
		fList.add("TTT");
		fList.add("TTC");
		pList.add("CCT");
		pList.add("CCC");
		pList.add("CCA");
		pList.add("CCG");
		sList.add("TCT");
		sList.add("TCC");
		sList.add("TCA");
		sList.add("TCG");
		sList.add("AGT");
		sList.add("AGC");
		tList.add("ACT");
		tList.add("ACC");
		tList.add("ACA");
		tList.add("ACG");
		wList.add("TGG");
		yList.add("TAT");
		yList.add("TAC");
		vList.add("GTT");
		vList.add("GTC");
		vList.add("GTA");
		vList.add("GTG");
		
		//unknown X peptides
		xList.add("AAA");
		xList.add("CCC");
		xList.add("GGG");
		xList.add("TTT");
		
		bList.add("AAA");
		bList.add("CCC");
		bList.add("GGG");
		bList.add("TTT");
		
		zList.add("AAA");
		zList.add("CCC");
		zList.add("GGG");
		zList.add("TTT");
	}
	
	protected static String PeptidestoDNA(String peptides, String optimization)
	{
		StringBuilder sb = new StringBuilder();
		
		if(optimization.equals("random"))
		{
			Random pick = new Random();
			String tempChar = null;
			String temp = null;
			
			for(int i=0; i<peptides.length(); i++)
			{
				tempChar = String.valueOf(peptides.charAt(i));
				ArrayList<String> workList = peptideMap.get(tempChar);
				//System.out.println(tempChar);
				temp = workList.get(pick.nextInt(workList.size()));
				sb.append(temp);
			}
		}
		
		if(optimization.equals("ecoli"))
		{
			for(int i=0; i<peptides.length(); i++)
			{
				sb.append(ecoliMap.get(peptides.charAt(i)));
			}
		}
		
		if(optimization.equals("human"))
		{
			for(int i=0; i<peptides.length(); i++)
			{
				sb.append(humanMap.get(peptides.charAt(i)));
			}
		}
		return sb.toString(); //the peptides converted into DNA bases
	}
	
	protected static String DNAtoBits(String dnaSequence)
	{
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<dnaSequence.length(); i++)
		{
			inputChar = dnaSequence.charAt(i);
			outputChar = dnaMap.get(inputChar);
			sb.append(outputChar);
		}
		//System.out.println(sb.toString());
		return sb.toString();			
	}
	
	protected static String BitstoAscii(String bitSequence, Boolean modFlag)
	{
		StringBuilder sb = new StringBuilder();
		Integer remainder = bitSequence.length() % 8; //need to account for sequences where (length % 8) is not 0
		if(remainder != 0)
		{
			bitSequence = bitSequence.substring(0, (bitSequence.length()-remainder));	
			//System.out.println(remainder);
			//System.out.println(bitSequence);
		}
		for (int i=0; i<bitSequence.length(); i+=8) //assuming modulo 8
		{
			bitInput = bitSequence.substring(i, i+8);
			asciiOutput = Integer.parseInt(bitInput, 2);
			if(modFlag)
			{
				asciiOutput = (asciiOutput % 94) + 33; //this turns the 0-255 ascii into the 94 "readable" characters used in sequencing confidence
			}
			sb.append((char)asciiOutput);
		}	
		//System.out.println("Ascii code: " + sb.toString());
		//System.out.println(sb.length());
		return sb.toString();	
	}
	
	public static void main(String[] args)
	{
		//BuildTestInputs();
		
		if (args.length != 2)
		{
			System.out.println("Expecting one file name and one optimization type as command-line arguments. Now exiting.");
			System.exit(1);
		}

		String fileName = args[0];
		String optimizationType = args[1];
		BuildPeptideMap();
		BuildDNAMap();
		BuildEcoliMap();
		BuildHumanMap();
		
		//BytePairProg bpcp = new BytePairProg();
		QuadPairProg qpcp = new QuadPairProg();
		//bpcp.ZeroBytePairs();
		qpcp.ZeroBytePairs();
		
		
		Calendar calendar = Calendar.getInstance();
		Timestamp clock = new Timestamp(calendar.getTimeInMillis());
		startTime = clock.getTime();
		
		System.out.println("Reading file.");
		
		try //check for file existence
		{
			textScanner = new Scanner(new File(fileName));
		}
		catch (FileNotFoundException e)
		{
			System.out.println("Error opening " + fileName + " (file not found). Now exiting.");
		    System.exit(1);
		}
		
		File file = new File("MultipleDNAtoASCII.txt");
		try
		{
			file.createNewFile();
			writer = new FileWriter(file);
		}
		catch (IOException e2)
		{
			e2.printStackTrace();
		}
			
		while(textScanner.hasNextLine())
		{
			StringBuilder sb = new StringBuilder();
			String tempBuilder = null;
			
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
//			try
//			{
//				writer.write(sb.toString() + "\n");
//			}
//			catch (IOException e1)
//			{
//				e1.printStackTrace();
//			}
			//System.out.println(sb.toString());
			tempBuilder = PeptidestoDNA(sb.toString(),optimizationType);
			try
			{
				writer.write(tempBuilder + "\n");
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
			//System.out.println(sb.toString());
			bits = DNAtoBits(tempBuilder);
//			try
//			{
//				writer.write(bits + "\n");
//			}
//			catch (IOException e1)
//			{
//				e1.printStackTrace();
//			}
			
			//bpcp.CountBytePairs(bits);
			qpcp.CountBytePairs(bits);
			
			ascii = BitstoAscii(bits, true);	
//			try
//			{
//				writer.write(ascii.toString() + "\n");
//				writer.write("\n");
//			}
//			catch (IOException e1)
//			{
//				e1.printStackTrace();
//			}
			empty = false;
		}		
		textScanner.close();
		
		try
	    {
			writer.flush();
		    writer.close();
		}
	    catch (IOException e)
	    {
			e.printStackTrace();
	    }

		System.out.println("Finished writing MultipleDNAtoASCII.txt file.");
		calendar = Calendar.getInstance();
		clock = new Timestamp(calendar.getTimeInMillis());
		stopTime = clock.getTime();

		Double totalTime = (double) (stopTime - startTime);
		System.out.println("Total time (in seconds): " + (totalTime)/1000);
		
		//bpcp.WriteBytePairCount();
		qpcp.WriteBytePairCount();

	}
}