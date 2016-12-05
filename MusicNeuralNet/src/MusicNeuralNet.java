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

public class MusicNeuralNet 
{
	String filename;
	DataSet trainingSet;
	MultiLayerPerceptron neuralNet;
	BackPropagation backProp;
	int numIn = 5;
	int numOut = 1;
	int maxIterations = 100000;
	double learningRate = 0.01;
	String netFilename = "src/neurNet.nnet";
	
	public MusicNeuralNet(String fname)
	{
		System.out.println("\nMusic Composition Neural Network\n");
		this.filename = fname;
		this.createTrainingData();
		this.createNeuralNet();
	}
	
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
	
	public void createNeuralNet()
	{
		//create multi layer perceptron
		this.neuralNet = new MultiLayerPerceptron(TransferFunctionType.SIGMOID,this.numIn,3,3,this.numOut);
		this.backProp = (BackPropagation)this.neuralNet.getLearningRule();
		this.backProp.setMaxIterations(this.maxIterations);
		this.backProp.setLearningRate(this.learningRate);
		this.neuralNet.setLearningRule(this.backProp);
		
	}
	
	public void trainNeuralNet()
	{
		System.out.println("Training neural network...");
		this.neuralNet.learn(this.trainingSet,this.backProp);
		this.neuralNet.save(this.netFilename);
		System.out.println("\nTrained and saved!\n");
	}
	
	public void outputNotes(int numNotes)
	{
		NeuralNetwork loadedNet = NeuralNetwork.createFromFile(this.netFilename);
		
		ArrayBlockingQueue queue = new ArrayBlockingQueue<>(this.numIn);
		queue.add(0.0);
		queue.add(0.2);
		queue.add(0.4);
		queue.add(0.5);
		queue.add(0.7);
		
		
		for(int i=0;i<numNotes;++i)
		{
			Iterator iterator = queue.iterator();
			double[] inputs = new double[this.numIn];
			for(int j=0;j<this.numIn;++j)
			{
				inputs[j]= (double)iterator.next();
				System.out.println(" IN: "+inputs[j]);
			}
			this.neuralNet.setInput(inputs);
			this.neuralNet.calculate();
			double[]networkOut = this.neuralNet.getOutput();
			double outputVal = networkOut[0];
			
			int outputNote = (int) Math.round(outputVal*10);
			System.out.println("OUT: "+outputVal);
			System.out.println("					Note: "+outputNote);
			System.out.println("REMOVED: "+queue.remove());
			queue.add(outputVal);
			System.out.println("ENQUEUED: "+outputVal+"\n");
	
			
		}
			
	}

	public static void main(String[] args) 
	{
		MusicNeuralNet mNN = new MusicNeuralNet("ParsedSongs/ParsedSongsDouble.txt");
		mNN.trainNeuralNet();
		mNN.outputNotes(10);
	}

}
