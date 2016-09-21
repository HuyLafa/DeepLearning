import java.util.Arrays;
import java.util.Random;

public class SimulatedAnnealing {

	FullNeuralNetwork network;
	Random rand = new Random();

	// global values
	int numberOfIterations = 0;
	int maxIterations = 50000;
	double initialTemp = 10000;
	double finalTemp = 0.5;
	double currentError = Double.MAX_VALUE;
	double globalBestError = currentError;
	double[][][] globalBest;

	double[][] inputs;
	double[] outputs;

	public SimulatedAnnealing( FullNeuralNetwork network, double[][] inputs, double[] outputs ) {
		this.network = network;
		this.inputs = inputs;
		this.outputs = outputs;
	}


	public double[][][] train() {
		double[][][] finalW = network.getWeights();
		for ( int i = 0 ; i < maxIterations ; i++ ) {
			iterate( network.getWeights(), 50 );
		}
		finalW = globalBest;
		System.out.println("output weight vector is: ");
		for ( double x : finalW[0][0] ) {
			System.out.print(x + " ");
		}
		System.out.println("global best error is " + globalBestError);
		network.setWeights( finalW );
		return finalW;
	}


	public void iterate( double[][][] weights, int cycles ) {
		numberOfIterations ++;
		double trialError = -1;
		double p = -1;
		double currentTemperature = coolingSchedule();

		for ( int i = 0 ; i < cycles ; i++ ) {
			// backup current state
			double[][][] oldState = cloneWeights( weights );
			// randomize the method
			performRandomize( weights );
			// check new score
			network.setWeights( weights );

			// get output from network
			double[] o = new double[ inputs.length ];
			for ( int j = 0 ; j < inputs.length ; j++ ) {
				network.setInputs( inputs[j] );
				o[j] = network.getOutputs()[0];
			}
			trialError = LossFunction.logLoss( o, outputs );
			boolean keep = trialError < currentError;
			// if this move results in worse score, there is still a probability that we take it
			if ( !keep ) {
				p = calcProbability( currentError, trialError, currentTemperature );
				if ( p > rand.nextDouble() ) {
					keep = true;
				}
			}
			// should we keep this new position?
			if ( keep ) {
				currentError = trialError;
				// better than global error?
				if ( trialError < globalBestError ) {
					globalBestError = trialError;
					oldState = cloneWeights( weights );
					globalBest = cloneWeights( weights );
				}			
			}
			else {
				weights = cloneWeights( oldState );
			}
		}
		System.out.println(
			"Iteration #" + numberOfIterations + 
			" global score =" + globalBestError + 
			" weight = \n"
		);
		for ( double x : weights[0][0] ) {
			System.out.print(x + " ");
		}
		System.out.println();
	}


	public double coolingSchedule() {
		return initialTemp * Math.pow( finalTemp / initialTemp, numberOfIterations * 1.0 / maxIterations);
	}


	public double calcProbability( double currentError, double previousError, double currentTemp ) {
		return Math.exp( (currentError - previousError) / currentTemp );
	}

	public double[][][] cloneWeights( double[][][] weights ) {
		double[][][] cloned = new double[ weights.length ][][];
		for ( int i = 0 ; i < weights.length ; i++ ) {
			cloned[i] = new double[ weights[i].length ][];
			for ( int j = 0 ; j < weights[i].length ; j++ ) {
				cloned[i][j] = new double[ weights[i][j].length ];
				for ( int k = 0 ; k < weights[i][j].length ; k++ ) {
					cloned[i][j][k] = weights[i][j][k];
				}
			}
		}
		return cloned;
	}


	public double[][][] performRandomize( double[][][] weights ) {
		for ( int i = 0 ; i < weights.length ; i++ ) {
			for ( int j = 0 ; j < weights[i].length ; j++ ) {
				for ( int k = 0 ; k < weights[i][j].length ; k++ ) {
					weights[i][j][k] += Main.getRandomNumberInRange(-1, 1);
				}
			}
		}
		return weights;
	}
}