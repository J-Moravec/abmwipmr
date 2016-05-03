package simple_model;

import java.util.List;

import repast.simphony.essentials.RepastEssentials;

public class Stats {
	public static int first_total_switch = 0;
	public static int time_of_extinction = 0;
	
	public Stats(){
		//because Repast do not like static functions
	}

	public static void every_step_stats(List<Village> village_list) {
		calculate_first_total_switch(village_list);
		calculate_time_of_extinction(village_list);

	}
	
	public static void calculate_first_total_switch(List<Village> village_list){
		if(Stats.first_total_switch == 0){
			boolean patrilocal = false;
			//go through all villages and test if they are matrilocal
			for(Village village : village_list){
				if(village.residence == Residence.PATRILOCAL){
					patrilocal = true;
					break;
				}
			}
			
			if(!patrilocal){
				Stats.first_total_switch = (int) RepastEssentials.GetTickCount();
			}
		}
		
	}
	
	
	public static void calculate_time_of_extinction(List<Village> village_list){
		if(Stats.time_of_extinction == 0){
			if(village_list.size() == 0){
				Stats.time_of_extinction = (int) RepastEssentials.GetTickCount();
			}
		}
	}
	
	
	//one_time_stats
	public int total_population(){
		int total_pop = 0;
		for(Village village : Village.village_list){
			total_pop += village.total_pop();
		}
		return total_pop;
	}
	
	
	public int number_of_switches(){
		return Residence.num_changes;
	}
	
	
	public int village_count(){
		return Village.village_list.size();
	}
	
	
	public double mean_distance_to_zone(){
		if(Village.village_list.size() == 0){
			return 0;
		}
		
		double x_sum = 0;
		for(Village village : Village.village_list){
			if(village.in_zone){
				// coords starts from 0 but are in opposite numbering
				// to transform it, we must take:
				// X_communities - coord_x
				x_sum += Constants.X_communities - village.coord_x;
			}
		}
		return Utils.round(x_sum/Constants.Y_communities,2); //starts from 1
	}
	
	
	public double var_distance_to_zone(){
		//computing sample variance, which has 1/(n-1) instead of 1/n
		if(Village.village_list.size() == 0){
			return 0;
		}
		
		double mean = mean_distance_to_zone();
		double var = 0;
		for(Village village : Village.village_list){
			if(village.in_zone){
				var += Math.pow(Constants.X_communities - village.coord_x - mean, 2);
			}
			
		}
		return Utils.round(var/(Constants.Y_communities - 1),2);
	}
	
	
	public int get_first_total_switch(){
		return Stats.first_total_switch;
	}
	
	public int get_time_of_extinction(){
		return Stats.time_of_extinction;
	}
	
	
	public double proportion_of_matrilocal(){
		double n_matri = 0;
		for(Village village : Village.village_list){
			if(village.residence == Residence.MATRILOCAL){
				n_matri +=1;
			}
		}
		return Utils.round(n_matri/village_count(), 2);
	}
	
	
	public double proportion_of_males(){
		double n_males = 0;
		for(Village village : Village.village_list){
			n_males += Utils.sum_vec(village.cohorts_male);
		}
		
		return Utils.round(n_males/total_population(),2);
	}
}