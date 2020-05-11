package bytepairs;

/*
 * This program is slightly different from the main BytePairCounter.
 * Instead of parsing each string in a sliding window of 16 bits, it
 * will progressively scan (moving over one bit until the final 16 bits
 * are analyzed.
 * 
 * this quadpair is modified to scan windows of 32 bits (16bitx16bit)
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class QuadPairProg
{
	int[][] quadPairs = new int[65536][65536];
	Integer totalBits = 0;
	BufferedWriter writer;
	
	public void ZeroBytePairs()
	{
		for (int[] row : quadPairs)
		{
		    Arrays.fill(row, 0);
		}
	}
	
	public void CountBytePairs(String bitSequence)
	{
		totalBits += bitSequence.length();
		String bitByte1 = null;
		String bitByte2 = null;
		Integer bitInteger1 = 0;
		Integer bitInteger2 = 0;
				
		for (int i=0; i<bitSequence.length(); i++)
		{
			if(bitSequence.length() > i+31) //make sure there are at least four bytes
			{
				bitByte1 = bitSequence.substring(i, i+16);
				bitByte2 = bitSequence.substring(i+16, i+32);
				
				bitInteger1 = Integer.parseInt(bitByte1, 2);
				bitInteger2 = Integer.parseInt(bitByte2, 2);
				
				//need to make sure the matrix is filled correctly here, validate some entries
				
				quadPairs[bitInteger1][bitInteger2] += 1;
			}
		}
	}
	
	public void PrintBytePairCount()
	{
		System.out.println(Arrays.deepToString(quadPairs));
	}
	
	public void WriteBytePairCount()
	{
		try
		{
			writer = new BufferedWriter(new FileWriter("quadPairsProgressive.txt"));
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		for(int i = 0; i < quadPairs.length; i++)//for each row
		{
		   StringBuilder builder = new StringBuilder();
		   for(int j = 0; j < quadPairs.length; j++)//for each column
		   {
		      builder.append(quadPairs[i][j]+"");//append to the output string
		      if(j < quadPairs.length - 1)//if this is not the last row element
		         builder.append(",");//then add comma (or you can use spaces)
		   }
		   builder.append("\n");//append new line at the end of the row
		   try
		   {
			   writer.write(builder.toString());
		   }
		   catch (IOException e)
		   {
			   e.printStackTrace();
		   }
		}		
		try
		{
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		System.out.println("Finished writing quadPairsProgressives.txt, with a total of " + totalBits + " bits.");
	}	
}