import java.util.*;
import java.io.*;

public class SongParser
{
	public SongParser()
	{
		
	}
	
	//Parses raw song files into files of successive note sequences of varying length which are readable by the neural net
	public void parse() throws IOException
	{
		File[] songs = new File("RawSongs").listFiles();
		
		File outputDirectory = new File("ParsedSongs");
		outputDirectory.mkdir();
		File outputSongsThreeBehind = new File("ParsedSongs\\ParsedSongsDoubleThreeBehind.txt");
		File outputSongsFourBehind = new File("ParsedSongs\\ParsedSongsDoubleFourBehind.txt");
		
		FileOutputStream fos = new FileOutputStream(outputSongsThreeBehind);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		
		FileOutputStream fos2 = new FileOutputStream(outputSongsFourBehind);
		BufferedWriter bw2 = new BufferedWriter(new OutputStreamWriter(fos2));
		
		for (int k = 0; k < songs.length; k++)
		{
			Scanner sc = new Scanner(songs[k]);
			String sequence = sc.next();
			
			Scanner sc2 = new Scanner(songs[k]);
			String sequence2 = sc2.next();
			
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
			
			for (int i = 4; i < sequence2.length(); i++)
			{
				for (int j = i-4; j < i; j++)
				{
					bw2.write((Character.getNumericValue(sequence2.charAt(j))/10.0) + " ");
				}
				bw2.write((Character.getNumericValue(sequence2.charAt(i))/10.0) + "");
				if ((k == (songs.length - 1)) && (i == sequence2.length() - 1))
				{
				}
				else
				{
					bw2.newLine();
				}
			}
			sc.close();
			sc2.close();
		}
		
		bw.close();
		bw2.close();
	}
	
	//Parses raw song files into sequences of two consecutive notes which are readable by the neural net
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
	
	//Creates a new parser which analyzes raw song data and writes output to files readable by neural net
	public static void main(String[] args) throws IOException
	{
		SongParser parser = new SongParser();
		parser.parse();
		parser.parseSteps();
	}
}