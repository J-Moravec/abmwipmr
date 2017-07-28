/**
 * 
 */
package simple_model;

import java.util.Arrays;

import cern.jet.random.Uniform;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.random.RandomHelper;

/** Utils -- class that contains various utilities
 * @author jmoravec
 *
 */
public class Utils {
	public static Uniform uniform = RandomHelper.createUniform(0.0, 1.0);
	
	/** vec_multiply
	 * 
	 * vector multiplication for integer, double and their combination. Returns sum of product
	 * of corresponding elements, type of sum is according to "higher" type.
	 * 
	 */
	public static int vec_multiply(int[] vec1, int[] vec2){
		if(vec1.length != vec2.length){
			throw new IllegalArgumentException("ERROR: Vectors have different length!");
		}
		int length = vec1.length;
		int sum = 0;
		for(int i=0; i < length; i++){
			sum += vec1[i] * vec2[i];
		}
		return sum;
	}
	public static double vec_multiply(double[] vec1, double[] vec2){
		if(vec1.length != vec2.length){
			throw new IllegalArgumentException("ERROR: Vectors have different length!");
		}
		int length = vec1.length;
		double sum = 0;
		for(int i=0; i < length; i++){
			sum += vec1[i] * vec2[i];
		}
		return sum;
	}
	public static double vec_multiply(double[] vec1, int[] vec2){
		if(vec1.length != vec2.length){
			throw new IllegalArgumentException("ERROR: Vectors have different length!");
		}
		int length = vec1.length;
		double sum = 0;
		for(int i = 0; i < length; i++){
			sum += vec1[i] * ((double) vec2[i]);
		}
		return sum;
	}
	
	
	/** scalar_multiply
	 * 
	 * Scalar multiplication for double and int and their combinations. Will return vector
	 * of "higher" type (int only if int * int[]).
	 * 
	 * @return vector of higher type
	 */
	public static int[] scalar_multiply(int scalar, int[] vec){
		int length = vec.length;
		int[] new_vec = new int[length];
		for(int i=0; i < length; i++){
			new_vec[i] = scalar * vec[i];
		}
		return new_vec;
	}
	public static double[] scalar_multiply(double scalar, double[] vec){
		int length = vec.length;
		double[] new_vec = new double[length];
		for(int i=0; i < length; i++){
			new_vec[i] = scalar * vec[i];			
		}
		return new_vec;
	}
	public static double[] scalar_multiply(double scalar, int[] vec){
		int length = vec.length;
		double[] new_vec = new double[length];
		for(int i=0; i < length; i++){
			new_vec[i] = scalar * ((double) vec[i]);
		}
		return new_vec;
	}
	public static double[] scalar_multiply(int scalar, double[] vec){
		int length = vec.length;
		double[] new_vec = new double[length];
		for(int i=0; i < length; i++){
			new_vec[i] = ((double) scalar) * vec[i];
		}
		return new_vec;
	}
	
	
	/** sum_vec
	 * 
	 * Summarise all items in vector (i.e., array). Returns their sum (what else, right).
	 * 
	 */
	public static int sum_vec(int[] vec){
		int length = vec.length;
		int sum = 0;
		for(int i=0; i < length; i++){
			sum += vec[i];
		}
		return sum;
	}
	public static double sum_vec(double[] vec){
		int length = vec.length;
		double sum = 0;
		for(int i=0; i < length; i++){
			sum += vec[i];
		}
		return sum;
	}
	
	
	public static int sum_vec_from(int[] vec, int from){
		int length = vec.length;
		int sum = 0;
		for(int i=from; i< length; i++){
			sum += vec[i];
		}
		return sum;
	}
	
	
	public static double sum_vec_from(double[] vec, int from){
		int length = vec.length;
		double sum = 0;
		for(int i=from; i< length; i++){
			sum += vec[i];
		}
		return sum;
	}
	
	
	/** random_roll
	 * 
	 * simple probability roll against uniform distibution.
	 * 
	 * @return 1 if success, 0 if fail.
	 */
	public static int random_roll(double probability){
		double random_number = uniform.nextDouble();
		if (random_number > probability){
			return 0;
		} else {
			return 1;
		}
	}
	
	
	/** prob_round
	 * 
	 *  Probability rounding. With all the growth and death rates, there would necessarily be
	 *  results that are not whole numbers. But at the same time, our discrete model allows only
	 *  whole ones, not 3 and half baby born or quarter person dyeing in warfare. But simple
	 *  rounding removes too much information from model. One way would be summing up remainders,
	 *  the other is take the remainder as probability. For example, if 3.4 babies are born, this
	 *  means that 3 babies are born and the fourth one is born with probability of 0.4.
	 *  
	 * @param number to be rounded
	 * @return rounded number according to probability of remainder.
	 */
	public static int prob_round(double number){
		int truncated = (int) number;
		double remainder = number - truncated;
		int rounded = truncated + random_roll(remainder);
		return rounded;
	}
	
	
	/** random_roll
	 * 
	 *  Random roll for arrays. Take array, divide each item by sum of array (total probability)
	 *  and then compute cumulative array. Next perform random roll and returns position of
	 *  selected item,
	 *  
	 * @param frequency_array
	 * @return index of selected item
	 */
	public static int random_roll(double[] frequency_array){
		int length = frequency_array.length;
		double total_prob = sum_vec(frequency_array);
		
		//probability array
		double[] prob_array = new double[length];
		for(int i = 0; i < length; i++){
			prob_array[i] = frequency_array[i] / total_prob;
		}
		
		//cumulative probability
		double[] cumsum_array = new double[length];
		cumsum_array[0] = prob_array[0];
		for (int i = 1; i < length; i++){
			cumsum_array[i] = cumsum_array[i-1] + prob_array[i];
		}
		
		//roll itself
		int index = -1;
		double random_number = uniform.nextDouble();
		for(int i=0; i < length; i++){
			if(random_number < cumsum_array[i]){
				index = i;
				break;
			}	
		}
		if(index == -1){
			System.out.println("frequency_array: " + Arrays.toString(frequency_array));
			System.out.println("cumsum_array: " + Arrays.toString(cumsum_array));
			throw new IllegalStateException("ERROR: in random_roll: probability is"
					+ " computed incorrectly!");
		}
		return index;
	}
	
	
	public static int[] multivariate_hypergeometric_distribution(
			int[] input_vector,
			int number_of_samples
			){
		int length = input_vector.length;
		int[] sampling_vector = new int[length];
		int[] sampled_items = new int[length];
		int samples = number_of_samples;
		System.arraycopy(input_vector, 0, sampling_vector, 0, length);
		Arrays.fill(sampled_items, 0);
		
		int index;
		//sample_step
		for(int i=0; i < samples; i++){
			index = sample_step(sampling_vector);
			sampling_vector[index] -= 1;
			sampled_items[index] += 1;
		}
		return sampled_items;
	}
	
	
	public static int sample_step(int[] sampling_vector){
		int length = sampling_vector.length;
		int index = -1;
		int[] cumsum_array = new int[length];
		
		//make cumsum array
		cumsum_array[0] = sampling_vector[0];
		for(int i = 1; i < length; i++){
			cumsum_array[i] = sampling_vector[i] + cumsum_array[i-1];
		}
		
		//random roll for index
		int random = RandomHelper.nextIntFromTo(1, cumsum_array[length-1]);
		for(int i = 0; i < length; i++){
			if (random <= cumsum_array[i]){
				index = i;
				break;
			}
		}
		if(index == -1){
			System.out.println("random roll: " + random);
			System.out.println("cumsum_array: " + Arrays.toString(cumsum_array));
			throw new IllegalStateException("ERROR: in sample_step: probability is"
					+ " computed incorrectly!");
		}
		return index;
	}
	
	
	public static double round(double value, int places){
		double operator = Math.pow(10, places);
		double result = value * operator;
		result = Math.round(result)/operator;
		return result;
	}
	
	public static double get_tick_count(){
		double tick_count = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		return(tick_count);
	}
	
}
