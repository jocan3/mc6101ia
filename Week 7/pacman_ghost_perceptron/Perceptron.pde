class Perceptron {
  //[full] The Perceptron stores its weights and learning constants.
  float[] weights;
  float c = 0.2;
  //[end]

  Perceptron(int n) {
    weights = new float[n];
    //[full] Weights start off random.
    for (int i = 0; i < weights.length; i++) {
      weights[i] = random(0,1);
    }
    //[end]
  }

  //[full] Return an output based on inputs.
  float feedforward(float[] inputs) {
    float sum = 0;
    for (int i = 0; i < weights.length; i++) {
      sum += inputs[i]*weights[i];
    }
    return sum;
  }
  //[end]

  //[full] Output is a +1 or -1.
  int activate(float sum) {
    if (sum > 0) return 1;
    else return -1;
  }
  

  //[end]

  //[full] Train the network against known data.
  void train(float[] inputs, float desired) {
    float guess = feedforward(inputs);
    float error = desired - guess;
    for (int i = 0; i < weights.length; i++) {
      weights[i] += c * error * inputs[i];
    }
  }
  //[end]
}