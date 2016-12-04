
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
	int numIn = 5;
	int numOut = 1;
	
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
		this.neuralNet = new MultiLayerPerceptron(TransferFunctionType.TANH,5,3,1);
		//this.neuralNet
		BackPropagation backProp = (BackPropagation)this.neuralNet.getLearningRule();
		backProp.setMaxIterations(10000);
		backProp.setLearningRate(0.01);
		this.neuralNet.setLearningRule(backProp);
		System.out.println("Training neural network...");
		this.neuralNet.learn(this.trainingSet,backProp);
		this.neuralNet.save("src/neuralNet.nnet");
		System.out.println("\nTrained and saved!\n");
	}
	
	public static void main(String[] args) 
	{
		MusicNeuralNet mNN = new MusicNeuralNet("ParsedSongs/ParsedSongs.txt");
	}

}
