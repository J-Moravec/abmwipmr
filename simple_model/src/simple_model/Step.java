package simple_model;

import java.util.List;
import repast.simphony.context.Context;


public class Step {
	public static Context<Object> context;
	
	public static void step(List<Village> village_list){
		//remove clusters from context
		Cluster.remove_from_context(context);
		
		
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
		
		//add all clusters to context
		Cluster.add_to_context(context);
		}
	}