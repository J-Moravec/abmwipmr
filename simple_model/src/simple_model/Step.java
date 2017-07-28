package simple_model;

import repast.simphony.context.Context;


public class Step {
	public static Context<Object> context;
	
	public static void step(Village_list village_list){
		//remove clusters from context
		Cluster.remove_from_context(context);
		
		//marriage
		village_list.marry();
		
		//growth
		village_list.grow();
		
		//destroy empty communities and move zone
		village_list.destroy_empty();
		
		//change PMR
		village_list.change_residence();
		
		//Compute stats
		Stats.every_step_stats(village_list);
		Stats stats = new Stats();
		
		for(Village village : village_list){
			if(village.total_pop() <= 0){
				System.out.println("This shouldn't happen!");
			}
			if(village.get_total_warriors() <=0){
				System.out.println("Warriors are zero? This shouldn't happen!");
			}
		}
		System.out.println(village_list.size()
				+ " communities, distance to zone: "
				+ stats.mean_distance_to_zone() );
		
		//add all clusters to context
		Cluster.add_to_context(context);
		}
	}