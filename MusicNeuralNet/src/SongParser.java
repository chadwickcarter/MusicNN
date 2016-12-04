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
		
		for (int k = 0; k < songs.length; k++)
		{
			Scanner sc = new Scanner(songs[k]);
			String sequence = sc.next();
			
			for (int i = 5; i < sequence.length(); i++)
			{
				for (int j = i-5; j < i; j++)
				{
					bw.write(sequence.charAt(j) + " ");
				}
				bw.write(sequence.charAt(i));
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
	
	public static void main(String[] args) throws IOException
	{
		SongParser parser = new SongParser();
		parser.parse();
	}
}