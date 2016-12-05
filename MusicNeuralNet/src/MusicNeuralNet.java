import java.util.Arrays;
import java.lang.Math;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.*;
import org.neuroph.util.TransferFunctionType;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.Perceptron;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.nnet.learning.BackPropagation;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import org.jfugue.player.Player;

public class MusicNeuralNet 
{
	String filename;
	DataSet trainingSet; //DataSet object is input to network
	MultiLayerPerceptron neuralNet; //MLP neural network
	BackPropagation backProp; //Uses BackPropagation algorithm to learn
	int numIn = 4; //How many previous notes are used as input
	int numOut = 1; //Output one note
	int maxIterations = 100000; //Limits training time
	double learningRate = 0.01; //Best results with this rate so far
	String netFilename = "src/test.nnet";
	String savedFilename = "src/neurNet.nnet";
	String[] noteVals = {"C","C#","D","D#","E","F","F#","G","G#","A","A#","B","C"}; //mapping of int index to note names
	
	//Constructor passes filename for data and creates a blank neural network
	public MusicNeuralNet(String fname)
	{
		System.out.println("\nMusic Composition Neural Network\n");
		this.filename = fname;
		this.createTrainingData();
		this.createNeuralNet();
	}
	
	//creates training data from specified filename
	public void createTrainingData()
	{
		this.trainingSet = new DataSet(this.numIn,this.numOut);
	
		try
		{
			FileReader f = new FileReader(this.filename);
			Scanner scan = new Scanner(f);
			while(scan.hasNextLine())
			{
				double[] inputs = new double[this.numIn];
				for(int i=0;i<this.numIn;++i)
				{
					inputs[i]=scan.nextDouble();
				}
				double[] outputs = new double [this.numOut];
				for(int j=0;j<this.numOut;++j)
				{
					outputs[j]=scan.nextDouble();
				}
				//add row to training data
				DataSetRow row = new DataSetRow(inputs,outputs);
				this.trainingSet.addRow(row);
				
			}
			
		}
		catch(FileNotFoundException e)
		{
			System.out.println(e.getMessage());
		}
		
		
	}
	
	//Creates a blank neural net with the BackPropagation learning rule
	public void createNeuralNet()
	{
		//create multi layer perceptron
		this.neuralNet = new MultiLayerPerceptron(TransferFunctionType.SIGMOID,this.numIn,5,5,this.numOut);
		this.backProp = (BackPropagation)this.neuralNet.getLearningRule();
		this.backProp.setMaxIterations(this.maxIterations);
		this.backProp.setLearningRate(this.learningRate);
		this.neuralNet.setLearningRule(this.backProp);
		
	}
	
	//Trains the neural net
	//Its use in main can be commented out to avoid the time cost of training,
	//and we can load a saved network
	public void trainNeuralNet()
	{
		System.out.println("Training neural network...");
		this.neuralNet.learn(this.trainingSet,this.backProp);
		this.neuralNet.save(this.netFilename);
		System.out.println("\nTrained and saved!\n");
	}
	
	//Outputs the specified number of notes from the saved neural network
	public void outputNotes(int numNotes)
	{
		NeuralNetwork loadedNet = NeuralNetwork.createFromFile(this.savedFilename);
		this.neuralNet = (MultiLayerPerceptron)loadedNet;
		ArrayBlockingQueue queue = new ArrayBlockingQueue<>(this.numIn);
		
		double[] exampleInput = {0.4,0.7,0.9,0.0,0.4,0.7,0.4};
		
		String song = "";
		for(int k=0;k<this.numIn;++k)
		{
			queue.add((double)exampleInput[k]);
			song+=(this.noteVals[(int)(exampleInput[k]*10)]+" ");
		}
		
		for(int i=0;i<numNotes;++i)
		{
			Iterator iterator = queue.iterator();
			double[] inputs = new double[this.numIn];
			for(int j=0;j<this.numIn;++j)
			{
				inputs[j]= (double)iterator.next();
				
			}
			this.neuralNet.setInput(inputs);
			this.neuralNet.calculate();
			double[]networkOut = this.neuralNet.getOutput();
			double outputVal = networkOut[0];
			//System.out.println("OUT: "+outputVal);
			int outputNote = (int) Math.round(outputVal*10);
			//System.out.println("Note: "+outputNote);
			
			song+=(this.noteVals[outputNote]+" ");
			
			queue.remove();
			
			queue.add(outputVal);
			
		}
		System.out.println("\n"+song);
		
		//Play the generated song
		Player player = new Player();
		player.play(song);
			
	}

	//Main function creates neural network object, trains (optionally), and outputs notes.
	public static void main(String[] args) 
	{
		MusicNeuralNet mNN = new MusicNeuralNet("ParsedSongs/ParsedSongsDoubleFourBehind.txt");
		//mNN.trainNeuralNet();
		mNN.outputNotes(12);
	}

}
