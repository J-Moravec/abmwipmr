package simple_model;

import java.util.List;

import simple_model.Village;


public class Neighbours {
	
	/** add_neighbours
	 * 
	 *  Add villages as neighbours of other villages.
	 */
	static public void add_neighbours(List<Village> village_list, Village[][] helper_grid){
		for (Village village : village_list){
			int X = village.coord_x;
			int Y = village.coord_y;
			for(
					int rangeX = X - Constants.interactionDistance;
					rangeX < X + Constants.interactionDistance + 1;
					rangeX++){
				if(rangeX < 0 || rangeX > Constants.X_communities - 1) continue;
				for(
						int rangeY = Y - Constants.interactionDistance;
						rangeY < Y + Constants.interactionDistance + 1;
						rangeY++){
					if(rangeY < 0 || rangeY > Constants.Y_communities - 1) continue;
					//if(rangeX == X && rangeY == Y)continue; village is in its own neighbourhood
					village.neighbours.add(helper_grid[rangeX][rangeY]);
				}
				
			}
			
			
		}
	}
}
