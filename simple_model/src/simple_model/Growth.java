/**
 * 
 */
package simple_model;

import cern.jet.random.Binomial;
import repast.simphony.random.RandomHelper;

/**
 * @author jmoravec
 * 
 */
public class Growth {	
	/**
	 * growth function
	 * 
	 * Perform growth according to age-structured logistic growth model.
	 * 
	 * Male and female cohorts are changing nearly independently, only newly borns are sampled
	 * for their gender (according to binomial distribution), which cause dependency.
	 * 
	 * Generally, the change is according to this equation:
	 * 
	 * m_{T+1} = survival * m_T + growth_rate * (1 - N_T / K) - death_rate * m_T
	 * 
	 *  Finally, because number of people is integer, but births and deaths can produce doubles
	 *  the floor of double is taken as whole unit and the rest as probability. Eg.:
	 *  
	 *  death_rate * m_1_T = 3.4 people.
	 *  This means minimally 3 people will die and fourth person will die with probability:
	 *  p = 0.4
	 *  
	 *  However, since we don't have natural death rates (except at 50 age), but only warfare death
	 *  rates, and they are dependent only on enemy population, under Lanchester's Square law,
	 *  it is a little bit
	 *  different.
	 */
	public void growth(Village village) {
		int[] new_cohorts_male = new int[Constants.cohorts_num];
		int[] new_cohorts_female = new int[Constants.cohorts_num];
		int[] new_cohorts_pairs = new int[Constants.cohorts_num];
		
		//newborns
		int newborns = get_newborns(village);
		int newborn_males = get_male_newborns(newborns);
		int newborn_females = newborns - newborn_males;
		new_cohorts_male[0] = newborn_males;
		new_cohorts_female[0] = newborn_females;
		
		//others
		// Before warming phase, there are no warfare deaths. After warming phase, warfare deaths are allowed:
		if(village.in_zone && Utils.get_tick_count() > Constants.warming_phase){
			Loses warfare_loses = village.get_warfare_loses();
			for(int i=1; i < Constants.cohorts_num; i++){
				new_cohorts_male[i] = village.cohorts_male[i-1] - warfare_loses.single_warrior_loses[i-1];
				new_cohorts_pairs[i] = village.cohorts_pairs[i-1] - warfare_loses.married_warrior_loses[i-1];
				new_cohorts_female[i] = village.cohorts_female[i-1] + warfare_loses.married_warrior_loses[i-1];
				// pair transform into single females when male from pair dies
			}
		} else {
			for(int i = 1; i < Constants.cohorts_num; i++){
				new_cohorts_male[i] = village.cohorts_male[i-1];
				new_cohorts_female[i] = village.cohorts_female[i-1];
				new_cohorts_pairs[i] = village.cohorts_pairs[i-1];
			}
		}
		village.cohorts_male = new_cohorts_male;
		village.cohorts_female = new_cohorts_female;
		village.cohorts_pairs = new_cohorts_pairs;
	}

	
	private int get_newborns(Village village){
		int total_pop = village.total_pop();
		int newborns = Utils.prob_round(
				Utils.vec_multiply(Constants.growth_rate_vec, village.cohorts_pairs)
				* (1 - ((double) total_pop) / Constants.carrying_capacity)
				);
		return(newborns);
	}
	
	/** newborn_males
	 * 
	 * Sample gender of newborn individuals according to binomial distribution. This enables
	 * to have a bit more variable marriage situation later.
	 * Newborn gender bias is caused probably by female infanticity.
	 * 
	 *  Returns number of newborn males. Number of newborn females is calculated later as:
	 *  newborn_females = total_newborns - newborn_males
	 */
	public int get_male_newborns(int total_newborns){
		if(total_newborns == 0){
			return 0;
		}
		double males_prob = Constants.newborn_gender_percentage[0];
		//System.out.println(total_newborns);
		Binomial binomial = RandomHelper.createBinomial(total_newborns, males_prob);
		int num_males = binomial.nextInt();
		return num_males;
	}
}
