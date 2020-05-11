package octet;

/*
 * The scan is progressive of a window size of eight peptides, 20^8=25.6 billion
 * Using 320000x320000, we scan peptide encoded files for peptide octets
 * 4 bytes per Integer requires over 93GB RAM
 * Assumes each protein is at least 8 peptides
 * Must encode in base-20
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.stream.Collectors;

public class OctetProg
{
	Integer[][] octetGrid = new Integer[320000][320000];
	BufferedWriter writer;
	String[] peptideLetters = new String[8];
	Integer[] peptideInt = new Integer[8];
	Integer rowTotal = 0;
	Integer columnTotal = 0;
	Random pick = new Random();
	Integer nonzero = 0;
	Integer frequencyTotal = 0;
	Integer totalPeptides = 0;
	Double nonzeroAverage = 0.0;
	String octet = null;
	
	static HashMap<String, Integer> peptideMap = new HashMap<String, Integer>();
	static HashMap<String, Integer> nonzeroMap = new HashMap<String, Integer>();
	static List<Entry<String, Integer>> sortedList = new ArrayList<Entry<String,Integer>>();

	
	protected void ZeroTripletPairs()
	{
		for (Integer[] row : octetGrid)
		{
		    Arrays.fill(row, 0);
		}
	}
	
	protected void BuildPeptideMap()
	{
		peptideMap.put("A", 0);
		peptideMap.put("R", 1);
		peptideMap.put("N", 2);
		peptideMap.put("D", 3);
		peptideMap.put("C", 4);
		peptideMap.put("Q", 5);
		peptideMap.put("E", 6);
		peptideMap.put("G", 7);
		peptideMap.put("H", 8);
		peptideMap.put("I", 9);
		peptideMap.put("L", 10);
		peptideMap.put("K", 11);
		peptideMap.put("M", 12);
		peptideMap.put("F", 13);
		peptideMap.put("P", 14);
		peptideMap.put("S", 15);
		peptideMap.put("T", 16);
		peptideMap.put("W", 17);
		peptideMap.put("Y", 18);
		peptideMap.put("V", 19);

		peptideMap.put("X", 20); //unknown peptide (0-19)
		peptideMap.put("x", 20); //unknown peptide (0-19)
		peptideMap.put("B", 21); // is N or D (2 or 3)
		peptideMap.put("Z", 22); // is Q or E (5 or 6)
	}
	
	public void CountOctets(String peptideSequence)
	{
		totalPeptides += peptideSequence.length();
				
		for (int i=0; i<peptideSequence.length()-7; i++)
		{
			rowTotal = 0;
			columnTotal = 0;
			
			for (int j=0; j<8; j++)
			{
				peptideLetters[j] = peptideSequence.substring(i+j, i+j+1);
				peptideInt[j] = peptideMap.get(peptideLetters[j]);
				if (peptideInt[j] == 20)
				{
					peptideInt[j] = pick.nextInt(20);
				}
				if (peptideInt[j] == 21)
				{
					peptideInt[j] = pick.nextInt(2) + 2;
				}
				if (peptideInt[j] == 22)
				{
					peptideInt[j] = pick.nextInt(2) + 5;
				}
			}
			rowTotal = (peptideInt[0]*8000) + (peptideInt[1]*400) + (peptideInt[2]*20) + peptideInt[3];
			columnTotal = (peptideInt[4]*8000) + (peptideInt[5]*400) + (peptideInt[6]*20) + peptideInt[7];
			octetGrid[rowTotal][columnTotal] += 1;
		}
	}
	
	public void GetStats()
	{
		ConvertBase20 cb = new ConvertBase20();
		cb.BuildPeptideMap();
		try
		{
			writer = new BufferedWriter(new FileWriter("xyzOctet.txt"));
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		for(int i=0; i<320000; i++)
		{
			for(int j=0; j<320000; j++)
			{
				if(octetGrid[i][j] != 0)
				{
					nonzero++;
					frequencyTotal+=octetGrid[i][j];
					octet = cb.ParseRowCol(Integer.toString(i), Integer.toString(j));
					nonzeroMap.put(octet, octetGrid[i][j]);
					try
					{
						writer.write(i + " " + j + " " + octetGrid[i][j] + "\n");
					}
					catch (IOException e1)
					{
						e1.printStackTrace();
					}
				}
			}
		}
		try
		{
			writer.flush();
			writer.close();
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		System.out.println("Finished writing xyzOctet.txt");
		nonzeroAverage = (double)frequencyTotal/(double)nonzero;
		
		sortedList = nonzeroMap.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(Collectors.toList());
		
		try
		{
			writer = new BufferedWriter(new FileWriter("octetsDescending.csv"));
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		for(Entry<String,Integer> e: sortedList)
		{
			try
			{
				writer.write(e.getKey() + ";" + e.getValue() + "\n");
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}
		try
		{
			writer.write("\n");
			writer.write("Nonzeros: " + nonzero + "(out of 25,600,000,000\n");
			writer.write("Average nonzero count: " + nonzeroAverage + "\n");
			writer.flush();
			writer.close();
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}		
		
		//Stream<Map.Entry<String, Integer>> sorted = nonzeroMap.entrySet().stream().sorted(Map.Entry.comparingByValue());
		//nonzeroMap.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(System.out::println);
		System.out.println("Finished writing octetsDescending.csv");
		System.out.println("Nonzeros: " + nonzero + "(out of 25,600,000,000");
		System.out.println("Nonzero total counts: " + frequencyTotal);
		System.out.printf("Average nonzero count: %.5f \n", nonzeroAverage);
	}	
}
