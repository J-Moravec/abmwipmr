package simple_model;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.essentials.RepastEssentials;

public class Stats {
	public static int first_total_switch = 0;
	public static int time_of_extinction = 0;
	public static List<List<Village>> cluster_list;
	public static List<Cluster> list_of_clusters;
	
	public Stats(){
		//because Repast do not like static functions // WTF was this supposed to mean?
	}

	public static void every_step_stats(List<Village> village_list) {
		//calculate_first_total_switch(village_list);
		//calculate_time_of_extinction(village_list);
		cluster_list = get_networks();
		list_of_clusters = make_list_of_clusters(cluster_list);
	}
	
	private static List<Cluster> make_list_of_clusters(
			List<List<Village>> cluster_list2) {
		List<Cluster> list_of_clusters = new ArrayList<Cluster>();
		for(List<Village> village_list : cluster_list2){
			list_of_clusters.add(new Cluster(village_list));
		}
		return list_of_clusters;
	}

	public static void calculate_first_total_switch(List<Village> village_list){
		if(Stats.first_total_switch == 0){
			boolean patrilocal = false;
			//go through all villages and test if they are matrilocal
			for(Village village : village_list){
				if(village.get_residence() == Residence.PATRILOCAL){
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
	
	public double mean_pop_in_zone(){
		double villages_in_zone = 0;
		double sum_pop_villages_in_zone = 0;
		double mean_pop_in_zone = 0;
		
		for(Village village : Village.village_list){
			if(village.is_in_zone()){
				villages_in_zone += 1;
				sum_pop_villages_in_zone = village.total_pop();
			}
		}
		
		if(villages_in_zone != 0){
			mean_pop_in_zone = sum_pop_villages_in_zone/villages_in_zone;
		}
		return mean_pop_in_zone;
	}
	
	
	public double mean_pop_not_in_zone(){
		double villages_not_in_zone = 0;
		double sum_pop_villages_not_in_zone = 0;
		double mean_pop_not_in_zone = 0;
		
		for(Village village : Village.village_list){
			if(!village.is_in_zone()){
				villages_not_in_zone +=1;
				sum_pop_villages_not_in_zone = 0;
			}
		}
		
		if(villages_not_in_zone != 0){
			mean_pop_not_in_zone = sum_pop_villages_not_in_zone / villages_not_in_zone;
		}
		
		return mean_pop_not_in_zone;
	}
	
	
	public double mean_distance_to_zone(){
		if(Village.village_list.size() == 0){
			return 0;
		}
		
		double x_sum = 0;
		for(Village village : Village.village_list){
			if(village.is_in_zone()){
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
			if(village.is_in_zone()){
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
		if(Village.village_list.size() == 0){
			return 0;
		}
		
		double n_matri = 0;
		for(Village village : Village.village_list){
			if(village.get_residence() == Residence.MATRILOCAL){
				n_matri +=1;
			}
		}
		return Utils.round(n_matri/village_count(), 2);
	}
	
	
	public double proportion_of_males(){
		if(Village.village_list.size() == 0){
			return 0;
		}
		
		double n_males = 0;
		for(Village village : Village.village_list){
			n_males += Utils.sum_vec(village.cohorts_male) + Utils.sum_vec(village.cohorts_pairs);
		}
		
		return Utils.round(n_males/total_population(),2);
	}
	
	
	public static List<Village> build_network(List<Village> unassigned_villages){
		List<Village> network = new ArrayList<Village>();
		// assign first item from unassigned_villages into network
		//and delete it from unassigned_village
		network.add(unassigned_villages.get(0));
		unassigned_villages.remove(0);
		
		while (true) {
			List<Village> neighbours = get_all_neighbours(network);
			List<Village> new_items = search_for_neighbours(neighbours, unassigned_villages);
			if(new_items.size() == 0){
				break;
			}
			network.addAll(new_items);
			unassigned_villages.removeAll(new_items);
		}
		return network;
	}
	
	
	private static List<Village> search_for_neighbours(
			List<Village> neighbours, List<Village> unassigned_villages
			) {
		List<Village> new_items = new ArrayList<Village>();
		for(Village village : unassigned_villages){
			if(neighbours.contains(village)){
				new_items.add(village);
			}
		}
		return new_items;
	}

	
	private static List<Village> get_all_neighbours(List<Village> network) {
		List<Village> neighbours = new ArrayList<Village>();
		for(Village village : network){
			for(Village neighbour : village.neighbours){
				if(!neighbours.contains(neighbour) && neighbour.total_pop() > 0){
					neighbours.add(neighbour);
				}
			}	
		}
		return neighbours;
	}

	
	
	public static List<List<Village>> get_networks(){
		List<Village> unassigned_villages = new ArrayList<Village>(Village.village_list);
		List<List<Village>> networks = new ArrayList<List<Village>>();
		while (unassigned_villages.size() > 0){
			networks.add(build_network(unassigned_villages));
		}
		return networks;
	}
	
	public int number_of_networks(){
		return(cluster_list.size());
	}
	
	public double proportion_of_warriors(){
		if(Village.village_list.size() == 0){
			return 0;
		}
		
		double n_warriors = 0;
		for(Village village : Village.village_list){
			n_warriors += village.get_total_warriors();
		}
		return Utils.round(n_warriors/total_population(), 2);
	}
}