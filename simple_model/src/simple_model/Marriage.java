package simple_model;

import java.util.List;

import simple_model.Gender;
import simple_model.Utils;
import simple_model.Residence;

public class Marriage {

	
	
	public Village marry(Village village){
		// village X was already chosen for marriage. Now I need to chose type of marriage
		// and then the rest, you know....
		
		//random roll for marriage residence
		Residence residence = Residence.choice(village.get_residence());
		//random roll for marriage gender
		Gender gender = get_marriage_gender(village);

		int cohort = village.get_marriable_cohort(gender);
		//random roll for neighbouring village according to gender and residence of marriage
		Village target = get_marriage_village(
				village.neighbours,
				residence,
				cohort,
				Gender.other(gender)
				);
		perform_marriage(village, target, residence, cohort, gender);
		return target;
	}

	
	private void perform_marriage(Village source, Village target, Residence residence, int cohort, Gender gender){
		if(target == null){
			source.get_cohort(gender)[cohort] -= 1;
			if(source.get_cohort(gender)[cohort] < 0){
				throw new IllegalStateException("ERROR: cohort is negative!\n" + source.toString());
			}
			source.get_temp_cohort(gender)[cohort] += 1;
		} else {
			source.get_cohort(gender)[cohort] -= 1;
			target.get_cohort(Gender.other(gender))[cohort] -=1;
			if(source.get_cohort(gender)[cohort] < 0){
				throw new IllegalStateException("ERROR: cohort is negative!\n" + source.toString());
			}
			if(target.get_cohort(gender)[cohort] < 0){
				throw new IllegalStateException("ERROR: cohort is negative!\n" + target.toString());
			}
			
			if((residence == Residence.PATRILOCAL && gender == Gender.MALE)
				|| (residence == Residence.MATRILOCAL && gender == Gender.FEMALE)){
				source.cohorts_pairs[cohort] +=1;
			} else {
				target.cohorts_pairs[cohort] +=1;
			}
		}
	}
	
	
	/** marriage_gender
	 * 
	 *  Random roll to determine which gender of chosen village will marry.
	 * @param village that was determined to marry
	 * @return marriage_gender of this marriage
	 */
	private Gender get_marriage_gender(Village village){
		Gender marriage_gender;
		int marriageable_males = Utils.sum_vec_from(
				village.get_cohort(Gender.MALE), Constants.marry_cohort
				);
		int marriageable_females = Utils.sum_vec_from(
				village.get_cohort(Gender.FEMALE), Constants.marry_cohort
				);
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
	private Village get_marriage_village(
			List<Village> neighbours,
			Residence marriage_residence,
			int cohort,
			Gender gender){
		//is village in it's own neighbour? If not, there might be a small change.
		int length = neighbours.size();
		double[] frequency_array = new double[neighbours.size()];
		for(int i = 0; i < length; i++){
			Village village = neighbours.get(i);
			double weight = marriage_weight(marriage_residence, village.get_residence());
			frequency_array[i] = weight * village.get_cohort(gender, cohort);			
		}
		
		//frequency_array is empty:
		if(Utils.sum_vec(frequency_array) == 0.0){
			return null;
		} else {
			int index = Utils.random_roll(frequency_array);
			return neighbours.get(index);
		}
	}
	
	public static double marriage_weight(Residence target, Residence source){
		double weight = Residence.weight(target, source);
		return weight;
	}
}
