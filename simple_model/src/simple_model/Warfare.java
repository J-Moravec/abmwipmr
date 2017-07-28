/**
 * 
 */
package simple_model;
/**
 * @author jmoravec
 *
 */
public class Warfare {
	
	/* Gets warfare loses for both pairs and male cohorts
	 * since pairs represents married males (and females), while male cohort represents unmarried
	 * males.
	 */
	public Loses warfare_loses(Village village){
		int[] single_warriors = get_warrior_cohorts(village.cohorts_male);
		int[] married_warriors = get_warrior_cohorts(village.cohorts_pairs);	
		int total_loses = Utils.prob_round(Constants.warfare_loses);
		
		Loses warfare_loses = new Loses(single_warriors, married_warriors, total_loses);
		return(warfare_loses);
	}
	
	
	public int[] get_warrior_cohorts(int[] cohorts_population){
		int length = cohorts_population.length;
		int[] warrior_cohorts = new int[length];
		for(int i = 0; i < length; i++){
			warrior_cohorts[i] = cohorts_population[i] * Constants.warrior_cohorts[i];
		}
		return warrior_cohorts;
	}

	
	public int total_warriors(Village village){
		int[] single_warriors = get_warrior_cohorts(village.cohorts_male);
		int[] married_warriors = get_warrior_cohorts(village.cohorts_pairs);
		return(Utils.sum_vec(single_warriors) + Utils.sum_vec(married_warriors));
	}


	public static double warfare_mortality(){
		return Warfare.warfare_pressure()*Constants.warfare_mortality;
	}
	
	
	public static double warfare_pressure(){
		int villages_in_zone = Village.village_list.num_villages_in_zone();
		return Constants.warfare_pressure/(double) villages_in_zone;
	}
	
}
