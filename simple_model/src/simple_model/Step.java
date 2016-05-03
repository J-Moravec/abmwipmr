package simple_model;

import java.util.List;

public class Step {
	
	public static void step(List<Village> village_list){
		//marriage
		Marriage.marry_all(village_list);
		
		//growth
		Growth.grow_all(village_list);
		
		//destroy empty communities and move zone
		Warfare.destroy_empty(village_list);
		
		//change PMR
		Residence.change_all(village_list);
		
		//Compute stats
		Stats.every_step_stats(village_list);
		}
	}