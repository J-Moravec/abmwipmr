/**
 * 
 */
package simple_model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author jmoravec
 *
 */
public class Village {
	public static Village_list village_list; // list of surviving villages
	public int coord_x;
	public int coord_y;
	public int changed_residence;
	public List<Village> neighbours;
	public int[] cohorts_male = new int[Constants.cohorts_num];
	public int[] cohorts_female = new int[Constants.cohorts_num];
	public int[] cohorts_pairs = new int[Constants.cohorts_num];
	private int[] temp_cohorts_male = new int[Constants.cohorts_num];
	private int[] temp_cohorts_female = new int[Constants.cohorts_num];
	private Zone zone;
	private Residence residence;
	private Warfare warfare;
	private Growth growth;
	private Marriage marriage;
	
	public Village(int coord_x, int coord_y){
		this.coord_x = coord_x;
		this.coord_y = coord_y;
		//this.space = space2;
		Arrays.fill(this.cohorts_male, 0);
		Arrays.fill(this.cohorts_female, 0);
		Arrays.fill(this.cohorts_pairs, 0);
		Arrays.fill(this.temp_cohorts_male, 0);
		Arrays.fill(this.temp_cohorts_female, 0);
		this.set_residence(Residence.PATRILOCAL);
		this.neighbours = new ArrayList<Village>();
		this.changed_residence = 0;
		this.zone = new Zone();
		this.warfare = new Warfare();
		this.growth = new Growth();
		this.marriage = new Marriage();
	}
	
	public String toString(){
		StringBuilder string = new StringBuilder();
		string.append("Village: {\n");
		string.append("  Primary variables: {\n");
		string.append("    name: " + this.name() + "\n");
		string.append("    cohorts_male: " + Arrays.toString(this.cohorts_male) + "\n");
		string.append("    cohorts_female: " + Arrays.toString(this.cohorts_female) + "\n");
		string.append("    cohorts_pairs: " + Arrays.toString(this.cohorts_pairs) + "\n");
		string.append("    temp_cohorts_male: " + Arrays.toString(temp_cohorts_male) + "\n");
		string.append("    temp_cohorts_female: " + Arrays.toString(this.temp_cohorts_female) + "\n");
		string.append("    residence: " + this.residence.toString() + "\n");
		string.append("    in_zone: " + this.is_in_zone() + "\n");
		string.append("  }\n");
		string.append("  Derived variables: {\n");
		string.append("    power: " + this.power() + "\n");
		string.append("    total warriors: " + this.get_total_warriors() + "\n");
		string.append("    total_pop: " + this.total_pop() + "\n");
		string.append("    marriable people: " + this.get_marriable_people() + "\n");
		string.append("  }\n}\n");
		return string.toString();
	}
	
	
	public boolean is_in_zone() {
		return this.zone.is_in_zone();
	}

	
	public void set_zone(boolean zone){
		this.zone.set_zone(zone);
	}
	
	
	public void grow(){
		this.growth.growth(this);
	}

	
	public Village marry(){
		Village married_village = this.marriage.marry(this);
		return(married_village);
	}
	
	
	public int total_pop() {
		int sum = Utils.sum_vec(this.cohorts_female)
				+ Utils.sum_vec(this.cohorts_male)
				+ 2*Utils.sum_vec(this.cohorts_pairs);
		return sum;
	}
	
	
	public int power() {
		return this.warfare.total_warriors(this);
	}

	
	public void kill() {
		if(this.is_in_zone()){
			this.set_zone(false);
			this.move_zone();
		}
		
		for(int i = 0; i < Constants.cohorts_num; i++){
			this.cohorts_male[i] = 0;
			this.cohorts_female[i] = 0;
			this.cohorts_pairs[i] = 0;
		}
	}

	
	private void move_zone(){
		this.zone.move_zone(this);
	}
	
	
	public String name(){
		//String name = "Village at CoordX: " + this.coord_x + " CoordY: " + this.coord_y;
		String name = "X" + this.coord_x + "Y" + this.coord_y;
		return name;
	}
	
	
	public void change_residence(){
		this.get_residence().change(this);
	}
	
	
	public int[] get_cohort(Gender gender){
		int[] cohorts_gender;
		if(gender == Gender.MALE){
			cohorts_gender = this.cohorts_male;
		} else {
			cohorts_gender = this.cohorts_female;
		}
		return cohorts_gender;
	}
	
	
	public int get_cohort(Gender gender, int which){
		int cohort_gender;
		if(gender == Gender.MALE){
			cohort_gender = this.cohorts_male[which];
		} else {
			cohort_gender = this.cohorts_female[which];
		}
		return cohort_gender;
	}

	
	public int[] get_temp_cohort(Gender gender) {
		int[] temp_cohorts_gender;
		if(gender == Gender.MALE){
			temp_cohorts_gender = this.temp_cohorts_male;
		} else {
			temp_cohorts_gender = this.temp_cohorts_female;
		}
		return temp_cohorts_gender;
	}

	
	public Loses get_warfare_loses(){
		return(this.warfare.warfare_loses(this));
	}
	
	
	public int get_total_warriors(){
		return this.warfare.total_warriors(this);
	}
	

	public int get_marriable_people(){
		int marriable_people = Utils.sum_vec_from(this.cohorts_male, Constants.marry_cohort)
				+ Utils.sum_vec_from(this.cohorts_female, Constants.marry_cohort);
		return(marriable_people);
	}
	
	
	public boolean has_marriable_people() {
		int marriable_people = this.get_marriable_people();
		if(marriable_people > 0){
			return true;
		} else if(marriable_people == 0){
			return false;
		} else {
			throw new IllegalStateException(
					"Error: The number of marriable people is negative.\n" + this.toString()
					);
		}
	}


	public Residence get_residence() {
		return this.residence;
	}

	
	public boolean roll_for_residence_change(){
		return this.residence.change_residence_test(this);
	}

	
	public void set_residence(Residence residence) {
		this.residence = residence;
	}


	public int get_marriable_cohort(Gender gender) {
		// Gets first non-empty unmarried cohort
		int[] cohorts = this.get_cohort(gender);
		int index = 0;
		for(int i = Constants.marry_cohort; i < cohorts.length; i++){
			if(cohorts[i] > 0){
				index = i;
				break;
			}
		}
		if(index == 0){
			throw new IllegalStateException(
					" Error: No marriageable person found for this gender. This shouldn't happen." +
					"\n" + this.toString());
		}
		return index;
	}
	
	
	public void remove_temp() {
		for(int cohort = Constants.marry_cohort; cohort < Constants.cohorts_num; cohort++){
			for(Gender gender : Gender.values()){
				if(this.get_cohort(gender)[cohort] != 0){
					throw new IllegalStateException(
							"Error: At this stage, marriable cohorts must be zero!\n"
							+ this.toString()
							);
				} else {
					this.get_cohort(gender)[cohort] = this.get_temp_cohort(gender)[cohort];
					this.get_temp_cohort(gender)[cohort] = 0;
				}
			}
		}
	}
}
