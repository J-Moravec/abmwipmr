/**
 * 
 */
package simple_model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import repast.simphony.space.continuous.ContinuousSpace;
import simple_model.Residence;
/**
 * @author jmoravec
 *
 */
public class Village {
	public static List<Village> village_list; // list of surviving villages
	public int[] cohorts_male = new int[Constants.cohorts_num];
	public int[] cohorts_female = new int[Constants.cohorts_num];
	public int coord_x;
	public int coord_y;
	public ContinuousSpace<Object> space;
	public Residence residence;
	public List<Village> neighbours;
	public int temp_cohort_male;
	public int temp_cohort_female;
	public boolean in_zone;
	public int changed_residence;
	
	public Village(ContinuousSpace<Object> space2, int coord_x, int coord_y){
		this.space = space2;
		this.coord_x = coord_x;
		this.coord_y = coord_y;
		Arrays.fill(this.cohorts_male, 0);
		Arrays.fill(this.cohorts_female, 0);
		this.residence = Residence.PATRILOCAL;
		temp_cohort_male = 0;
		temp_cohort_female = 0;
		this.neighbours = new ArrayList<Village>();
		this.changed_residence = 0;
	}
	
	public void grow(){
		Growth.growth(this);
	}

	public int total_pop() {
		int sum = Utils.sum_vec(this.cohorts_female) + Utils.sum_vec(this.cohorts_male);
		return sum;
	}

	public int[] full_pairs() {
		int length = this.cohorts_male.length;
		int[] full_pairs = new int[length];
		for(int i = 0; i < length; i++){
			full_pairs[i] = Math.min(this.cohorts_male[i], this.cohorts_female[i]);
		}
		return full_pairs;
	}
	
	public Village marry(){
		Village married_village = Marriage.marry(this);
		return(married_village);
	}
	
	public int[] cohort(Gender gender){
		int[] cohorts_gender;
		if(gender == Gender.MALE){
			cohorts_gender = this.cohorts_male;
		} else {
			cohorts_gender = this.cohorts_female;
		}
		return cohorts_gender;
	}
	
	public int cohort(Gender gender, int which){
		int cohort_gender;
		if(gender == Gender.MALE){
			cohort_gender = this.cohorts_male[which];
		} else {
			cohort_gender = this.cohorts_female[which];
		}
		return cohort_gender;
	}

	public boolean any_marriage_cohorts_left() {
		int marriageable_cohorts = this.cohorts_male[Constants.marry_cohort]
				+ this.cohorts_female[Constants.marry_cohort];
		if(marriageable_cohorts == 0){
			return false;
		} else {
			return true;
		}
	}

	public void remove_temp() {
		if(this.cohorts_male[Constants.marry_cohort] != 0){
			throw new IllegalStateException("ERROR: in this stage marriageable cohorts must"
					+ " be zero!");
		}
		if(this.cohorts_female[Constants.marry_cohort] != 0){
			throw new IllegalStateException("ERROR: in this stage marriageable cohorts must"
					+ " be zero!");
		}
		this.cohorts_male[Constants.marry_cohort] = this.temp_cohort_male;
		this.cohorts_female[Constants.marry_cohort] = this.temp_cohort_female;
		this.temp_cohort_female = 0;
		this.temp_cohort_male = 0;
	}

	public int power() {
		return Warfare.total_warriors(this);
	}

	public void kill() {
		this.in_zone = false;
		for(int i = 0; i < Constants.cohorts_num; i++){
			this.cohorts_male[i] = 0;
			this.cohorts_female[i] = 0;
		}
	}
	
	public String name(){
		String name = "Village at CoordX: " + this.coord_x + " CoordY: " + this.coord_y;
		return name;
	}
	
	
	public boolean change_residence_test(){
		boolean change;
		change = Residence.change_residence_test(this);
		return change;
	}
	
	
	public void change_residence(){
		Residence.change(this);
	}
}
