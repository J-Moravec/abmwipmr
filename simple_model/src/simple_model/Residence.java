package simple_model;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.engine.environment.RunEnvironment;

public enum Residence {
	MATRILOCAL, PATRILOCAL;
	
	public static int num_changes = 0;
	
	public static Residence other(Residence residence){
		Residence other_residence;
		if(residence == Residence.MATRILOCAL){
			other_residence = Residence.PATRILOCAL;
		} else {
			other_residence = Residence.MATRILOCAL;
		}
		return other_residence;
	}
	
	
	/** choice
	 * 
	 * Perform random roll to chose marriage residence according to marriage preferences.
	 * 
	 * returns residence of marriage
	 */
	public static Residence choice(Residence residence){
		Residence marriage_residence;
		//might want to change random roll to TRUE and FALSE
		int random_roll = Utils.random_roll(Constants.marriage_preferences[0]);
		if(random_roll == 1){
			marriage_residence = residence;
		} else {
			marriage_residence = other(residence);
		}
		return marriage_residence;
	}
	
	/** weight
	 * 
	 * Marriage weight between two residences. If residence weights are the same, the weight
	 * is the same like the preferential marriage weight. If weights are different, then they
	 * marry according to alternative marriage.
	 */
	public static double weight(Residence residence1, Residence residence2){
		double marriage_weight;
		if(residence1 == residence2){
			marriage_weight = Constants.marriage_preferences[0];
		} else {
			marriage_weight = Constants.marriage_preferences[1];
		}	
		return marriage_weight;
	}
	
	/** change_all
	 * 
	 *  Function describe change of residence for all villages in community.
	 *  First, change for every individual village is tested and if test is true, residences are
	 *  later changed. This is done in two cycles as change_residence_test is dependent on
	 *  neighbours.
	 */
	public static void change_all(List<Village> village_list) {
		List<Village> changing_residence = new ArrayList<Village>();
		for(Village village : village_list){
			if(village.change_residence_test()){
				changing_residence.add(village);
			}
		}
		
		for(Village village : changing_residence){
			village.change_residence();
		}
	}

	/** change_residence_test
	 * 
	 * Function that test for change of residence. If residence was changed some number of steps
	 * before, no test is performed. Test is then divided into specific functions:
	 * 
	 * change_residence_test_warfare -- testing for change due to warfare pressure
	 * change_residence_test_partners -- testing for change due to larger significantly larger
	 *     amount of potential partners with alternative residence.
	 * 
	 */
	public static boolean change_residence_test(Village village) {
		boolean change = false;
		if(village.changed_residence > 0){
			village.changed_residence -=1;
		} else {
			boolean change_warfare = false;
			boolean change_marriage_partners = false;
			
			change_warfare = change_residence_test_warfare(village);
			change_marriage_partners = change_residence_test_partners(village);
			
			change = change_warfare || change_marriage_partners;
		}
		
		return change;
	}

	/** change_residence_test_warfare
	 * 
	 * This change is performed only for patrilocal villages in zone.
	 */
	private static boolean change_residence_test_warfare(Village village) {
		boolean result = false;
		if(village.in_zone && village.residence == Residence.PATRILOCAL && RunEnvironment.getInstance().getCurrentSchedule().getTickCount() > Constants.warming_phase){
			double prob = Constants.warfare_loses / ((double) Warfare.total_warriors(village));
			prob = Math.min(1, prob);
			if(Utils.random_roll(prob) == 1){
				result = true;
			}
		}
		return result;
	}
	
	
	private static boolean change_residence_test_partners(Village village){
		boolean result = false;
		
		double total_male_partners = 0;
		double total_female_partners = 0;
		double total_male_partners_alt = 0;
		double total_female_partners_alt = 0;
		double partners;
		double partners_alt;
		
		double vil_males = village.cohorts_male[Constants.marry_cohort];
		double vil_females = village.cohorts_female[Constants.marry_cohort];
		
		for(Village neighbour : village.neighbours){
			double marriage_weight =
					Marriage.marriage_weight(village.residence, neighbour.residence);
			total_female_partners +=
					neighbour.cohorts_female[Constants.marry_cohort] * marriage_weight;
			total_male_partners +=
					neighbour.cohorts_male[Constants.marry_cohort] * marriage_weight;
			double marriage_weight_alt =
					Marriage.marriage_weight(Residence.other(village.residence), neighbour.residence);
			total_female_partners_alt +=
					neighbour.cohorts_female[Constants.marry_cohort] * marriage_weight_alt;
			total_male_partners_alt +=
					neighbour.cohorts_male[Constants.marry_cohort]* marriage_weight_alt; 
		}
		partners = total_male_partners / vil_females + total_female_partners / vil_males;
		partners_alt = total_male_partners_alt / vil_females
				+ total_female_partners_alt / vil_males;
		
		double prob = 1 - partners/partners_alt;
		if(prob > 0){
			int roll = Utils.random_roll(prob);
			if(roll == 1){
				result = true;
			}
		}
		return result;
	}


	public static void change(Village village) {
		village.residence = other(village.residence);
		village.changed_residence = Constants.change_residence_pause;
		Residence.num_changes +=1;
	}
}
