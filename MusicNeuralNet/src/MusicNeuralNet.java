import java.util.Arrays;
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

public class MusicNeuralNet 
{
	String filename;
	DataSet trainingSet;
	MultiLayerPerceptron neuralNet;
	BackPropagation backProp;
	int numIn = 5;
	int numOut = 1;
	int maxIterations = 10000;
	double learningRate=0.01;
	
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
					inputs[i]=scan.nextInt();
				}
				double[] outputs = new double [this.numOut];
				for(int j=0;j<this.numOut;++j)
				{
					outputs[j]=scan.nextInt();
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
		this.neuralNet = new MultiLayerPerceptron(TransferFunctionType.TANH,this.numIn,3,3,this.numOut);
		this.backProp = (BackPropagation)this.neuralNet.getLearningRule();
		this.backProp.setMaxIterations(this.maxIterations);
		this.backProp.setLearningRate(this.learningRate);
		this.neuralNet.setLearningRule(backProp);
		
	}
	
	public void trainNeuralNet()
	{
		System.out.println("Training neural network...");
		this.neuralNet.learn(this.trainingSet,backProp);
		this.neuralNet.save("src/neuralNet.nnet");
		System.out.println("\nTrained and saved!\n");
	}
	
	public void outputSavedNetwork()
	{
		NeuralNetwork loadedNet = NeuralNetwork.createFromFile("src/neuralNet.nnet");
		this.neuralNet.setInput(new double[]{4,2,0,2,4});
		this.neuralNet.calculate();
		double[]networkOut = this.neuralNet.getOutput();
		System.out.println(Arrays.toString(networkOut));
		
	}
	
	public static void main(String[] args) 
	{
		MusicNeuralNet mNN = new MusicNeuralNet("ParsedSongs/ParsedSongs.txt");
		mNN.trainNeuralNet();
		mNN.outputSavedNetwork();
	}

}
