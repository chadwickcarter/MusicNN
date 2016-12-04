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
		File outputSongs = new File("ParsedSongs\\ParsedSongs.txt");
		
		FileOutputStream fos = new FileOutputStream(outputSongs);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		
		for (File song : songs)
		{
			Scanner sc = new Scanner(song);
			String sequence = sc.next();
			
			for (int i = 5; i < sequence.length(); i++)
			{
				for (int j = i-5; j < i; j++)
				{
					bw.write(sequence.charAt(j) + " ");
				}
				bw.write(sequence.charAt(i));
				bw.newLine();
			}
			sc.close();
		}
		
		bw.close();
	}
	
	public static void main(String[] args) throws IOException
	{
		SongParser parser = new SongParser();
		parser.parse();
	}
}