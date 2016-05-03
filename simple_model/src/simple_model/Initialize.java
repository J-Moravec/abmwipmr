package simple_model;

import java.util.List;

public class Initialize {

	
	
	public static void init(List<Village> village_list, Village[][] helper_grid){
		//initialize starting conditions.
		//Villages that are on left are on war zone
		//set population to all villages to Constants.start_pop
		//add neighbours
		Neighbours.add_neighbours(village_list, helper_grid);
		set_war_zone(helper_grid);
		set_population(village_list);
		Village.village_list = village_list;
		Stats.first_total_switch = 0;
		Stats.time_of_extinction = 0;
		Residence.num_changes = 0;
	}
	
	
	
	public static void set_war_zone(Village[][] helper_grid){
		int x = 0;
		for(int y = 0; y < Constants.Y_communities; y++){
			helper_grid[x][y].in_zone = true;
		}
	}
	
	public static void set_population(List<Village> village_list){
		for(Village village : village_list){
			for(int i=0; i < Constants.cohorts_num; i++){
				village.cohorts_female[i] = Constants.start_pop;
				village.cohorts_male[i] = Constants.start_pop;
			}
		}
	}
}
