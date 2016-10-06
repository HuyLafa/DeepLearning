import java.util.Arrays;

public class Neuron {

	double[] inputs;
	double[] weights;
	double [] weightDeltas;
	int activationFunctionType = -1;
	double delta;
	double[] gradients;

	// types of activation function
	public static final int
		LINEAR = 0,
		STEP = 1,
		SIGMOID = 2,
		HYPERBOLIC = 3,
		RELU = 4,
		SOFTMAX = 5;


	public double evaluateLinearCombination() {
		double linearCombination = 0;
		for (int i = 0 ; i < inputs.length; i++ ) {
			linearCombination += inputs[i] * weights[i];
		}
		return linearCombination;
	}


	public double output() {
		// todo. save output to a variable
		switch ( activationFunctionType ) {
			case LINEAR:
				return ActivationFunctions.linearAF( evaluateLinearCombination() );
			case STEP:
				return ActivationFunctions.stepAF( evaluateLinearCombination() );
			case SIGMOID:
				return ActivationFunctions.sigmoidAF( evaluateLinearCombination() );
			case HYPERBOLIC:
				return ActivationFunctions.hyperbolicAF( evaluateLinearCombination() );
			case RELU:
				return ActivationFunctions.reLUAF( evaluateLinearCombination() );
			default:
				assert false : "Error. Unrecognized activation function.";
				return -1;
		}
	}


	public double getAFDerivative() {
		switch ( activationFunctionType ) {
			case LINEAR:
				return ActivationFunctions.d_linearAF( evaluateLinearCombination() );
			case SIGMOID:
				return ActivationFunctions.d_sigmoidAF( evaluateLinearCombination() );
			case HYPERBOLIC:
				return ActivationFunctions.d_hyperbolicAF( evaluateLinearCombination() );
			case RELU:
				return ActivationFunctions.d_reLUAF( evaluateLinearCombination() );
			default:
				assert false : "Error. Unrecognized activation function.";
				return -1;
		}
	}


	/***** GETTER AND SETTER *********/
	public int getAFType() {
		return activationFunctionType;
	}


	public void setWeight( int index, double w ) {
		weights[index ] = w;
	}


	public void setWeights( double[] w ) {
		weights = w;
		if ( weightDeltas == null ) {
			weightDeltas = new double[ w.length ];
			Arrays.fill( weightDeltas, 0);
		}
		if ( gradients == null ) {
			gradients = new double[ w.length ];
			Arrays.fill( gradients, 0);
		}
	}


	public double getWeight( int index ) {
		return weights[index];
	}
	

	public double[] getWeights() {
		return weights;
	}


	public void setAFType( int type ) {
		this.activationFunctionType = type;
	}


	public void setDelta( double delta ) {
		this.delta = delta;
	}


	public double getDelta() {
		return delta;
	}


	public double getWeightDelta( int index ) {
		return weightDeltas[index];
	}


	public void setWeightDelta( int index, double delta ) {
		weightDeltas[index] = delta;
	}


	public void setInput( double[] inputs) {
		this.inputs = inputs;
	}


	public void setGradient( int index, double g ) {
		gradients[index] = g;
	}


	public double getGradient( int index ) {
		return gradients[index];
	}


	public void clearData() {
		delta = 0;
	}
}