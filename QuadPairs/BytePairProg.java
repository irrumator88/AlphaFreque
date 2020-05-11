package bytepairs;

/*
 * This program is slightly different from the main BytePairCounter.
 * Instead of parsing each string in a sliding window of 16 bits, it
 * will progressively scan (moving over one bit until the final 16 bits
 * are analyzed.
 * quadpair should be modified to scan windows of 32 bits
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class BytePairProg
{
	BufferedWriter writer;
	Integer[][] bytePairs = new Integer[256][256];
	Integer totalBits = 0;
	
	public void ZeroBytePairs()
	{
		for (Integer[] row : bytePairs)
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
		
		Integer remainder = bitSequence.length() % 8; //need to account for sequences where (length % 8) is not 0
		if(remainder != 0)
		{
			bitSequence = bitSequence.substring(0, (bitSequence.length()-remainder));	
		}
		
		for (int i=0; i<bitSequence.length(); i++) //here is the sliding window difference between the two counters
		{
			if(bitSequence.length() > i+15) //make sure there are two bytes to check
			{
				bitByte1 = bitSequence.substring(i, i+8);
				bitByte2 = bitSequence.substring(i+8, i+16);
				bitInteger1 = Integer.parseInt(bitByte1, 2);
				bitInteger2 = Integer.parseInt(bitByte2, 2);

				bytePairs[bitInteger1][bitInteger2] += 1;
			}
		}
	}
	
	public void PrintBytePairCount()
	{
		System.out.println(Arrays.deepToString(bytePairs));
	}
	
	public void WriteBytePairCount()
	{
		try
		{
			writer = new BufferedWriter(new FileWriter("bytePairsProgressive.txt"));
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
	
		for(int i = 0; i < bytePairs.length; i++)//for each row
		{
		   StringBuilder builder = new StringBuilder();
		   for(int j = 0; j < bytePairs.length; j++)//for each column
		   {
		      builder.append(bytePairs[i][j]+"");//append to the output string
		      if(j < bytePairs.length - 1)//if this is not the last row element
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
		System.out.println("Finished writing bytePairsProgressives.txt, with a total of " + totalBits + " bits.");
	}
	
}
