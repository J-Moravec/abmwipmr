package simple_model;

import cern.colt.Arrays;

public class Loses {
	public int[] single_warrior_loses;
	public int[] married_warrior_loses;
	private int[] single_warriors;
	private int[] married_warriors;
	public int total_loses;
	
	public Loses(int[] single_warriors, int[] married_warriors, int total_loses){
		this.single_warriors = single_warriors;
		this.married_warriors = married_warriors;
		this.total_loses = total_loses;
		this.single_warrior_loses = new int[single_warriors.length];
		this.married_warrior_loses = new int [married_warriors.length];
		this.get_loses();
	}
	
	/* get_loses
	 * 
	 * First connects both vectors
	 * 
	 */
	private void get_loses(){
		int[] cohorts_total_warriors = get_cohorts_total_warriors(
				this.single_warriors, this.married_warriors
				);
		int total_warriors = Utils.sum_vec(cohorts_total_warriors);
		if(this.total_loses >= total_warriors){
			this.everyone_dies();			
		} else {
			this.some_dies(cohorts_total_warriors);
		}
	}
	
	
	private void everyone_dies(){
		System.arraycopy(
				this.single_warriors, 0,
				this.single_warrior_loses, 0,
				this.single_warriors.length
				);
		System.arraycopy(
				this.married_warriors, 0,
				this.married_warrior_loses, 0,
				this.married_warriors.length
				);
	}
	
	
	private void some_dies(int[] cohorts_total_warriors){
		int[] cohorts_total_loses = Utils.multivariate_hypergeometric_distribution(
				cohorts_total_warriors, total_loses
				);
		try {
		System.arraycopy(
				cohorts_total_loses,
				0,
				this.single_warrior_loses,
				0,
				this.single_warrior_loses.length
				);
		System.arraycopy(
				cohorts_total_loses,
				this.single_warrior_loses.length,
				this.married_warrior_loses,
				0,
				this.married_warrior_loses.length
				);
		} catch (ArrayIndexOutOfBoundsException exception){
			System.err.println(
					"Error: Out ArrayIndexOutOfBoundsException\n"
					+ "  cohorts_total_loses: " + Arrays.toString(cohorts_total_loses)
					+ "\n  single_warrior_loses: " + Arrays.toString(this.single_warrior_loses)
					+ "\n  married_warrior_loses: " + Arrays.toString(this.married_warrior_loses)
					+ "\n"
					);
			throw exception;
			
		}
	}
	
	
	private int[] get_cohorts_total_warriors(int[] single_warriors, int[] married_warriors){
		int[] cohorts_total_warriors = new int[single_warriors.length + married_warriors.length];
		System.arraycopy(single_warriors, 0, cohorts_total_warriors, 0, single_warriors.length);
		System.arraycopy(
				married_warriors,
				0,
				cohorts_total_warriors,
				single_warriors.length,
				married_warriors.length
				);
		return cohorts_total_warriors;
	}
}