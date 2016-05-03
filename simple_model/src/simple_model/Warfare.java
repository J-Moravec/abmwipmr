/**
 * 
 */
package simple_model;

import java.util.ArrayList;
import java.util.List;
/**
 * @author jmoravec
 *
 */
public class Warfare {
	
	
	public static int[] warfare_loses(Village village){
		int[] village_warriors = warrior_cohorts(village);
		int total_loses = Utils.prob_round(Constants.warfare_loses);
		int total_warriors = total_warriors(village);
		int[] warfare_loses = new int[village_warriors.length];
		if(total_warriors > total_loses){
			warfare_loses = Utils.multivariate_hypergeometric_distribution(
					village_warriors, 
					Utils.prob_round(Constants.warfare_mortality)
					);
		} else {
			System.arraycopy(village_warriors, 0, warfare_loses, 0, village_warriors.length);
			//TODO Shouldn't I kill whole population here? Or give some info about that?:
		}
		return warfare_loses;
	}
	
	
	public static int[] warrior_cohorts(Village village){
		int length = village.cohorts_male.length;
		int[] warrior_cohorts = new int[length];
		for(int i = 0; i < length; i++){
			warrior_cohorts[i] = village.cohorts_male[i] * Constants.warrior_cohorts[i];
		}
		return warrior_cohorts;
	}

	
	public static int total_warriors(Village village){
		return(Utils.sum_vec(warrior_cohorts(village)));
	}
	

	public static void destroy_empty(List<Village> village_list) {
		List<Village> villages_to_remove = new ArrayList<Village>();
		for (Village village : village_list){
			if (village.power() == 0 ){
				village.kill();
				move_zone(village.coord_x + 1, village.coord_y, village_list);
				villages_to_remove.add(village);
			}
		}
		village_list.removeAll(villages_to_remove);
	}


	private static void move_zone(int coord_x, int coord_y, List<Village> village_list) {
		for(Village village : village_list){
			if(village.coord_x == coord_x && village.coord_y == coord_y){
				village.in_zone = true;
				break;
			}
		}
	}
	
}
