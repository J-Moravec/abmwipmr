/**
 * 
 */
package simple_model;

/**
 * @author jmoravec
 *
 */
public class Constants {
	
	//fixed parameters
	public static int cohorts_num = 10;
	public static double[] growth_cohorts = new double[] {0, 0, 0, 0, 1, 1, 1, 1, 0, 0}; //cohorts that produce offsprings; not used?
	public static int marry_cohort = 4;
	public static int[] warrior_cohorts = new int[] {0, 0, 0, 1, 1, 1, 1, 1, 0, 0}; //cohorts that fight
	public static int start_pop = 10; //needs "heating" period without warfare to get good population structure
	public static double number_of_generations = 1100;
	public static double warming_phase = 100;
	
	//temporally fixed
	public static int carrying_capacity = 500;
	public static int X_communities=5; //fixed, like capacity of environment (possible parametrisation to explore effect of population size)
	public static int Y_communities=10; //fixed, like capacity of environment (possible parametrisation to explore effect of population size and zone size)
		
	
	//variables
	public static double growth_rate = 0.1;
	public static double newborn_male_bias = 116; //number of born males per 100 born females
	public static int warfare_pressure = 2200; //size of enemy population (number of soldiers, to be precise)
	public static double yearly_warfare_mortality = 0.00472;
		//yearly_warfare_mortality and warfare_pressure currently form single derived parameter
	public static double preferred_marriage_weight = 0.9;
	public static int change_residence_pause = 6; //after change, another change is not allowed for this many steps
	
	
	//derived parameters
	public static double[] growth_rate_vec = Utils.scalar_multiply(growth_rate, growth_cohorts);
	public static double[] newborn_gender_proportion = new double[] {newborn_male_bias, 100};
	public static double[] newborn_gender_percentage = newborn_gender_percentage_fun();
	public static double warfare_mortality = 1 - Math.pow(1 - yearly_warfare_mortality, 5); //in five years (cumulative)
	public static double[] marriage_preferences = new double[] {
		preferred_marriage_weight,
		1 - preferred_marriage_weight
		};
	public static double warfare_loses =  warfare_pressure * warfare_mortality;
	
	public static void init_derived_parameter(){
		growth_rate_vec = Utils.scalar_multiply(growth_rate, growth_cohorts);
		newborn_gender_proportion = new double[] {newborn_male_bias, 100};
		newborn_gender_percentage = newborn_gender_percentage_fun();
		warfare_mortality = 1 - Math.pow(1 - yearly_warfare_mortality, 5); //in five years (cumulative)
		marriage_preferences = new double[] {
			preferred_marriage_weight,
			1 - preferred_marriage_weight
			};
		space_size_X=(X_communities-1)*gap_size+2*border_size;
		space_size_Y=(Y_communities-1)*gap_size+2*border_size;
		warfare_loses =  warfare_pressure * warfare_mortality;
	}
	
	
	
	//////////////////////////////////////
	///////////////EXPLANATIONS///////////
	//////////////////////////////////////
	/** growth_rate, growth_cohorts and growth_rate_vec
	 * 
	 *  growth_rate --  is your typical growth rate, amount of surviving children (ignoring high
	 *      mortality) per pair. As we don't have classical death rate other than at the end of age
	 *      and through warfare, there are also absorbed all the other death rates, they are assumed
	 *      as never being born.
	 *  
	 *  growth_cohorts -- although originally signified growth_date_vec, currently they have
	 *  	similar function to warrior_cohorts in that they mark those cohorts that produce
	 *  	offsprings. Possibly could mean distribution of children by age.
	 *  
	 *  growth_rate_vec -- multiplying all growth_cohorts by growth_rate, one gets growth_rate_vec,
	 *  	which is used in calculations.
	 */
	
	
	/** marriage_preferences
	 * 
	 * Array of marriage preferences according to post-marital residence.
	 * Could be also interpreted as frequency of marriages according post-marital residence.
	 * 
	 * The first field represent frequency (probability, preference) according to post-marital
	 * residence of village. Thus if village is patrilocal, then first number represent frequency
	 * of patrilocal marriages. Second field then represent alternative marriage choice, so if
	 * village is patrilocal, that would mean matrilocal preference.
	 */	

	/** death_rates
	 * 
	 *  Death rates comes from death matrix, where death rates form vector bellow main diagonal.
	 *  This means that death_rates have k-1 dimension. So only k-1 first elements are used.
	 */

	
	
	
	//Size of continuous space
	public static int border_size=5;
	public static int gap_size=10;
	public static int space_size_X=(X_communities-1)*gap_size+2*border_size;
	public static int space_size_Y=(Y_communities-1)*gap_size+2*border_size;
	public static int interactionDistance = 1;

	
	
	public static int calculate_point(int grid_value){
		return grid_value * gap_size + border_size;
	}
	
	public static double[] newborn_gender_percentage_fun() {
		double[] ngp = newborn_gender_proportion;
		double[] newborn_gendern_percentage = new double[2];
		newborn_gendern_percentage[0] = ngp[0] / (ngp[0] + ngp[1]);
		newborn_gendern_percentage[1] = 1.0 - newborn_gendern_percentage[0];
		return newborn_gendern_percentage;
	}
	
}
