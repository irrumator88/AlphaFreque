/* assumes numbers passed from 0-7999, two files in csv, the row and column of 
 * peptide triplet pairs, and will convert back into six-letter sequences
 */


package peptides;

import java.util.HashMap;

public class Convert6Base20
{
	String[] peptideLetters = new String[8];
	String rowVigesimal = null;
	String colVigesimal = null;
	StringBuilder sb = new StringBuilder();
	String outputSextet = null;
	static HashMap<String, String> peptideMap = new HashMap<String, String>();
	
	protected void BuildPeptideMap()
	{
		peptideMap.put("0","A");
		peptideMap.put("1","R");
		peptideMap.put("2","N");
		peptideMap.put("3","D");
		peptideMap.put("4","C");
		peptideMap.put("5","Q");
		peptideMap.put("6","E");
		peptideMap.put("7","G");
		peptideMap.put("8","H");
		peptideMap.put("9","I");
		peptideMap.put("a","L");
		peptideMap.put("b","K");
		peptideMap.put("c","M");
		peptideMap.put("d","F");
		peptideMap.put("e","P");
		peptideMap.put("f","S");
		peptideMap.put("g","T");
		peptideMap.put("h","W");
		peptideMap.put("i","Y");
		peptideMap.put("j","V");
	}
	
	protected String ParseRowCol(String row, String col)
	{
		rowVigesimal = Integer.toString(Integer.parseInt(row, 10), 20);
		if(rowVigesimal.length() != 3)
		{
			sb.setLength(0);
			for(int i=3; i>rowVigesimal.length(); i--)
			{
				sb.append("0");
			}
			sb.append(rowVigesimal);
			rowVigesimal = sb.toString();
		}
		colVigesimal = Integer.toString(Integer.parseInt(col, 10), 20);
		if(colVigesimal.length() != 3)
		{
			sb.setLength(0);
			for(int i=3; i>colVigesimal.length(); i--)
			{
				sb.append("0");
			}
			sb.append(colVigesimal);
			colVigesimal = sb.toString();
		}
		
		for(int i=0; i<3; i++)
		{
			peptideLetters[i] = peptideMap.get(rowVigesimal.substring(i, i+1));
			peptideLetters[i+3] = peptideMap.get(colVigesimal.substring(i, i+1));
		}
		sb.setLength(0);
		for (int i=0; i<6; i++)
		{
			sb.append(peptideLetters[i]);
		}
		outputSextet = sb.toString();
		return outputSextet;
	}

}