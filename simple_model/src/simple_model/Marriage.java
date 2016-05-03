package simple_model;

import java.util.ArrayList;
import java.util.List;

import simple_model.Gender;
import simple_model.Utils;
import simple_model.Residence;

public class Marriage {

	
	
	public static Village marry(Village village){
		// village X was already chosen for marriage. Now I need to chose type of marriage
		// and then the rest, you know....
		
		//random roll for marriage residence
		Residence marriage_residence = Residence.choice(village.residence);
		//random roll for marriage gender
		Gender marriage_gender = marriage_gender(village);
		//random roll for neighbouring village according to gender and residence of marriage
		Village marriage_village = marriage_village(
				village.neighbours,
				marriage_residence,
				Gender.other(marriage_gender)
				);
		
		if(marriage_village == null){
			// no partner exist
			village.cohort(marriage_gender)[Constants.marry_cohort] -= 1;
			if(marriage_gender == Gender.MALE){
				village.temp_cohort_male += 1;
			} else {
				village.temp_cohort_female += 1;
			}
		} else {
			//perform marriage movement
			village.cohort(marriage_gender)[Constants.marry_cohort] -= 1;
			marriage_village.cohort(Gender.other(marriage_gender))[Constants.marry_cohort] -= 1; 
			
			
			if((marriage_residence == Residence.PATRILOCAL && marriage_gender == Gender.MALE) ||
					(marriage_residence == Residence.MATRILOCAL && marriage_gender == Gender.FEMALE)){
				village.temp_cohort_male += 1;
				village.temp_cohort_female += 1;
			} else {
				marriage_village.temp_cohort_male += 1;
				marriage_village.temp_cohort_female += 1;
			}
		}
		return marriage_village;
	}
	
	/** marriage_gender
	 * 
	 *  Random roll to determine which gender of chosen village will marry.
	 * @param village that was determined to marry
	 * @return marriage_gender of this marriage
	 */
	public static Gender marriage_gender(Village village){
		Gender marriage_gender;
		int marriageable_males = village.cohort(Gender.MALE, Constants.marry_cohort);
		int marriageable_females = village.cohort(Gender.FEMALE, Constants.marry_cohort);
		double prob_males = ((double) marriageable_males) /
				(marriageable_males + marriageable_females);
		int random_roll = Utils.random_roll(prob_males);
		if(random_roll == 1){
			marriage_gender = Gender.MALE;
		} else {
			marriage_gender = Gender.FEMALE;
		}
		return marriage_gender;
	}
	
	/** marriage_village
	 * 
	 *  random roll to pick of village. Probability is given as size of marriageable cohort times
	 *  the marriage weight divided by total probability. Probability of no marriage is not yet
	 *  implemented.
	 *  
	 *  @returns chosen village to marry
	 */
	public static Village marriage_village(
			List<Village> neighbours,
			Residence marriage_residence,
			Gender gender){
		//is village in it's own neighbour? If not, there might be a small change.
		int length = neighbours.size();
		double[] frequency_array = new double[neighbours.size()];
		for(int i = 0; i < length; i++){
			Village village = neighbours.get(i);
			double weight = marriage_weight(marriage_residence, village.residence);
			frequency_array[i] = weight * village.cohort(gender, Constants.marry_cohort);			
		}
		
		//frequency_array is empty:
		if(Utils.sum_vec(frequency_array) == 0.0){
			return null;
		} else {
			int index = Utils.random_roll(frequency_array);
			return neighbours.get(index);
		}
	}
	
	public static double marriage_weight(Residence marriage_residence, Residence village_residence){
		double weight = Residence.weight(marriage_residence, village_residence);
		return weight;
	}

	public static void marry_all(List<Village> village_list) {
		List<Village> marriage_list = new ArrayList<Village>(village_list);
		//first filter marriage list
		List<Village> empty_villages = new ArrayList<Village>();
		for(Village village : marriage_list){
			if(!village.any_marriage_cohorts_left()){
				empty_villages.add(village);
			}
		}
		marriage_list.removeAll(empty_villages);
		
		//repeat until no village to marry
		while(marriage_list.isEmpty() == false){
			int length = marriage_list.size();
			double[] frequency_array = new double[length];
			for(int i = 0; i < length; i++){
				frequency_array[i] = marriage_list.get(i).cohorts_male[Constants.marry_cohort] 
						+ marriage_list.get(i).cohorts_female[Constants.marry_cohort]; 
			}
			//System.out.println("frequency_array" + Arrays.toString(frequency_array));
			int index = Utils.random_roll(frequency_array);
			Village marry_village = marriage_list.get(index);
			Village married_village = marry_village.marry();
			/*
			if (married_village == null){
				System.out.println(marry_village.name() + " could not find suitable partner");
			} else {
				System.out.println("Married " + marry_village.name());
				System.out.println("with " + married_village.name());
			}
			*/
			if(!marry_village.any_marriage_cohorts_left()){
				marriage_list.remove(marry_village);
			}
			if(married_village != null && !married_village.any_marriage_cohorts_left()){
				marriage_list.remove(married_village);
			}
			
			
		}
		for (Village village : village_list){
			village.remove_temp();
		}
	}	
}
