package simple_model;

import java.util.ArrayList;
import java.util.List;

public class Zone {
	private boolean zone;

	
	public Zone(){
		this.zone = false;
	}
	
	public boolean is_in_zone() {
		return zone;
	}

	
	public void set_zone(boolean zone){
		this.zone = zone;
	}
	
	
	public void move_zone(Village village){
		Village new_zone;
		int coord_x = village.coord_x + 1;
		List<Village> column = get_villages_in_column(village.coord_y);
		while(coord_x < Constants.X_communities){
			new_zone = find_village(coord_x, column);
			if(new_zone == null){
				coord_x += 1;
			} else {
				new_zone.set_zone(true);
				break;
			}
		}
		
	}
	
	
	private List<Village> get_villages_in_column(int coord_y) {
		List<Village> column = new ArrayList<Village>();
		for(Village village : Village.village_list){
			if(village.coord_y == coord_y && village.total_pop() > 0){
				column.add(village);
			}
		}
		return column;
	}
	
	
	private Village find_village(int coord_x, List<Village> village_list){
		Village output = null;
		
		for(Village village : village_list){
			if(village.coord_x == coord_x){
				output = village;
				break;
			}
		}
		return output;
	}
}