package simple_model;


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
	public boolean change_residence_test(Village village) {
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
	 * This change is performed for patrilocal AND matrilocal villages in zone.
	 */
	private boolean change_residence_test_warfare(Village village) {
		Warfare warfare = new Warfare();
		boolean result = false;
		if(village.is_in_zone() &&
				Utils.get_tick_count() > Constants.warming_phase &&
				Constants.allow_matrilocal == 1){
			double prob = warfare.warfare_loses() / ((double) village.get_total_warriors());
			prob = Math.min(1, prob);
			if(Utils.random_roll(prob) == 1){
				result = true;
			}
		}
		return result;
	}
	
	
	private boolean change_residence_test_partners(Village village){
		boolean result = false;
		if (village.has_marriable_people()){
			double total_ratio = get_potential_partners_total_ratio(village);
			double prob = 1 - total_ratio;
			if(prob > 0){
				int roll = Utils.random_roll(prob);
				if(roll == 1){
					result = true;
				}
			}
		}
		return result;
	}

	
	private double get_potential_partners_total_ratio(Village village){
		double total_ratio = 0;
		for(int cohort = Constants.marry_cohort; cohort < Constants.cohorts_num; cohort++){
			total_ratio += get_potential_partners_ratio(village, Gender.MALE, cohort);
			total_ratio += get_potential_partners_ratio(village, Gender.FEMALE, cohort);
		}
		total_ratio = total_ratio/village.get_marriable_people();
		return(total_ratio);
	}

	
	private double get_potential_partners_ratio(Village source, Gender gender, int cohort){
		double current_partners = 0;
		double alternative_partners = 0;
		Gender other_gender = Gender.other(gender);
		for(Village neighbour : source.neighbours){
			Residence residence = source.get_residence();
			Residence alternative_residence = Residence.other(residence);
			current_partners += get_potential_partners(neighbour, residence, gender, cohort);
			alternative_partners += get_potential_partners(
					neighbour, alternative_residence, gender, cohort
					);
			}
		// Correcting constant +1
		// Since negative number of partners is not possible, this enable having zero partners:
		return source.get_cohort(other_gender, cohort)
				* (current_partners+ 1 )/(alternative_partners + 1);
	}
	
	
	private double get_potential_partners(
			Village target, Residence residence, Gender gender, int cohort
			){
		double marriage_weight = Marriage.marriage_weight(residence, target.get_residence());
		return(target.get_cohort(gender, cohort) * marriage_weight);
	}
	
	
	public void change(Village village) {
		village.set_residence(other(village.get_residence()));
		village.changed_residence = Constants.change_residence_pause;
		Residence.num_changes +=1;
	}
}
