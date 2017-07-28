package simple_model;

import java.util.List;

import repast.simphony.context.Context;

public class Cluster {
	public List<Village> cluster_list;
	
	Cluster(List<Village> cluster_list){
		this.cluster_list = cluster_list;
	}
	
	public int total_population(){
		int total_pop = 0;
		for(Village village : this.cluster_list){
			total_pop += village.total_pop();
		}
		return total_pop;
	}
	
	public int village_count(){
		return this.cluster_list.size();
	}
	
	
	public double mean_distance_to_zone(){
		double x_sum = 0;
		int num_in_zone = 0;
		for(Village village : this.cluster_list){
			if(village.in_zone){
				x_sum += Constants.X_communities - village.coord_x;
				num_in_zone += 1;
			}
		}
		return Utils.round(x_sum/num_in_zone, 2);
	}
	
	
	public double var_distance_to_zone(){
		if(this.cluster_list.size() == 1){
			return 0;
		}
		double mean = this.mean_distance_to_zone();
		double var = 0;
		int num_in_zone = 0;
		for(Village village : this.cluster_list){
			if(village.in_zone){
				var += Math.pow(Constants.X_communities - village.coord_x - mean, 2);
				num_in_zone += 1;
			}
		}
		return Utils.round(var/(num_in_zone - 1), 2);
	}
	
	
	public double proportion_of_matrilocal(){
		double n_matri = 0;
		for(Village village : this.cluster_list){
			if(village.get_residence() == Residence.MATRILOCAL){
				n_matri +=1;
			}
		}
		return Utils.round(n_matri/this.cluster_list.size(), 2);
	}
	
	
	public double proportion_of_males(){
		double n_males = 0;
		for(Village village : this.cluster_list){
			n_males += Utils.sum_vec(village.cohorts_male);
		}
		return Utils.round(n_males/this.total_population(), 2);
	}
	
	
	public double proportion_of_warriors(){
		double n_warriors = 0;
		for(Village village : this.cluster_list){
			n_warriors += village.get_total_warriors();
		}
		return Utils.round(n_warriors/this.total_population(), 2);
	}

	public static void remove_from_context(Context<Object> context) {
		if(Stats.list_of_clusters != null){
			context.removeAll(Stats.list_of_clusters);
			Stats.list_of_clusters = null;
			Stats.cluster_list = null;
		}
	}

	public static void add_to_context(Context<Object> context) {
		context.addAll(Stats.list_of_clusters);
		
	}
	
	
	public double mean_x(){
		double mean_x = 0;
		for(Village village : this.cluster_list){
			mean_x += village.coord_x;
		}
		return(Utils.round(mean_x/this.cluster_list.size(), 2));
	}
	
	
	public double mean_y(){
		double mean_y = 0;
		for(Village village : this.cluster_list){
			mean_y += village.coord_y;
		}
		return(Utils.round(mean_y/this.cluster_list.size(), 2));
	}
}
