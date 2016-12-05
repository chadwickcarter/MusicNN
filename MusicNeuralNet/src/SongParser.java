import java.util.*;
import java.io.*;

public class SongParser
{
	public SongParser()
	{
		
	}
	
	public void parse() throws IOException
	{
		File[] songs = new File("RawSongs").listFiles();
		
		File outputDirectory = new File("ParsedSongs");
		outputDirectory.mkdir();
		File outputSongs = new File("ParsedSongs\\ParsedSongsDouble.txt");
		
		FileOutputStream fos = new FileOutputStream(outputSongs);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		
		for (int k = 0; k < songs.length; k++)
		{
			Scanner sc = new Scanner(songs[k]);
			String sequence = sc.next();
			
			for (int i = 3; i < sequence.length(); i++)
			{
				for (int j = i-3; j < i; j++)
				{
					bw.write((Character.getNumericValue(sequence.charAt(j))/10.0) + " ");
				}
				bw.write((Character.getNumericValue(sequence.charAt(i))/10.0) + "");
				if ((k == (songs.length - 1)) && (i == sequence.length() - 1))
				{
				}
				else
				{
					bw.newLine();
				}
			}
			sc.close();
		}
		
		bw.close();
	}
	
	public void parseSteps() throws IOException
	{
		
		File outputFile = new File("ParsedSongs\\StepSongs.txt");
		
		FileOutputStream fos = new FileOutputStream(outputFile);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		
		
		FileReader fin = new FileReader("songsteps_onlyQ.txt");
		Scanner scan = new Scanner(fin);
		
		while(scan.hasNextLine())
		{
			String sequence = scan.next();
			for (int i = 0; i < sequence.length()-1; i++)
			{
				//System.out.println(sequence.charAt(i)+ " "+sequence.charAt(i+1)+"\n");
				bw.write((Character.getNumericValue(sequence.charAt(i))/10.0)+ " "+(Character.getNumericValue(sequence.charAt(i+1))/10.0)+"\n");
				
			}
			
		}
		scan.close();
		bw.close();
	}
	
	
	public static void main(String[] args) throws IOException
	{
		SongParser parser = new SongParser();
		parser.parse();
		parser.parseSteps();
	}
}