package peptides;

/*
 * The scan is progressive of a window size of eight peptides, 20^6=64 million
 * Using 8000x8000, we scan peptide encoded files for peptide sextets
 * Assumes each protein is at least 6 peptides
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

public class SextetProg
{
	int[][] sextetGrid = new int[8000][8000];
	BufferedWriter writer;
	String[] peptideLetters = new String[6];
	Integer[] peptideInt = new Integer[6];
	Integer rowTotal = 0;
	Integer columnTotal = 0;
	Random pick = new Random();
	Integer nonzero = 0;
	Integer frequencyTotal = 0;
	Integer totalPeptides = 0;
	Double nonzeroAverage = 0.0;
	String sextet = null;
	
	static HashMap<String, Integer> peptideMap = new HashMap<String, Integer>();
	static HashMap<String, Integer> nonzeroMap = new HashMap<String, Integer>();
	static List<Entry<String, Integer>> sortedList = new ArrayList<Entry<String,Integer>>();

	
	protected void ZeroTripletPairs()
	{
		for (int[] row : sextetGrid)
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
	
	public void CountSextets(String peptideSequence)
	{
		totalPeptides += peptideSequence.length();
				
		for (int i=0; i<peptideSequence.length()-7; i++)
		{
			rowTotal = 0;
			columnTotal = 0;
			
			for (int j=0; j<6; j++)
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
			rowTotal = (peptideInt[0]*400) + (peptideInt[1]*20) + peptideInt[2];
			columnTotal = (peptideInt[3]*400) + (peptideInt[4]*20) + peptideInt[5];
			sextetGrid[rowTotal][columnTotal] += 1;
		}
	}
	
	public void GetStats()
	{
		Convert6Base20 cb = new Convert6Base20();
		cb.BuildPeptideMap();
		try
		{
			writer = new BufferedWriter(new FileWriter("xyzSextet.txt"));
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		for(int i=0; i<8000; i++)
		{
			for(int j=0; j<8000; j++)
			{
				if(sextetGrid[i][j] != 0)
				{
					nonzero++;
					frequencyTotal+=sextetGrid[i][j];
					sextet = cb.ParseRowCol(Integer.toString(i), Integer.toString(j));
					nonzeroMap.put(sextet, sextetGrid[i][j]);
					try
					{
						writer.write(i + " " + j + " " + sextetGrid[i][j] + "\n");
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
		System.out.println("Finished writing xyzSextet.txt");
		nonzeroAverage = (double)frequencyTotal/(double)nonzero;
		
		sortedList = nonzeroMap.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(Collectors.toList());
		
		try
		{
			writer = new BufferedWriter(new FileWriter("sextetsDescending.csv"));
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
			writer.write("Nonzeros: " + nonzero + " (out of 64,000,000)\n");
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
		System.out.println("Finished writing sextetsDescending.csv");
		System.out.println("Nonzeros: " + nonzero + " (out of 64,000,000)");
		System.out.println("Nonzero total counts: " + frequencyTotal);
		System.out.printf("Average nonzero count: %.5f \n", nonzeroAverage);
	}	
}