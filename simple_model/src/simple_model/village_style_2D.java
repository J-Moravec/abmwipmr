package simple_model;

import java.awt.Color;
import simple_model.Constants;
import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;
import saf.v3d.scene.VSpatial;

public class village_style_2D extends DefaultStyleOGL2D {
	private static Color DARKGREEN = new Color(79, 121, 66);

	
	@Override
	public Color getBorderColor(Object agent){
		if (agent instanceof Village){
			Village village = (Village) agent;
			    if(village.total_pop() == 0){
			    	return DARKGREEN;
			    } else {
			    	if(village.is_in_zone()){
			    		return Color.RED;
			    	} else {
			    		return Color.BLACK;
			    	}
			    }
			} else {
			return null;
		}
	}

	
	@Override
	public Color getColor(Object agent){
		if (agent instanceof Village){
			Village village = (Village) agent;
			if(village.get_residence() == Residence.PATRILOCAL){
				return Color.BLUE;
			} else {
				return Color.RED;
			}
		} else {
			return null;
		}
	}
	
	
	@Override
	public int getBorderSize(Object agent){
		if(agent instanceof Village){
			return 5;
		} else {
			return 0;
		}
	}
	
	
	@Override
	public VSpatial getVSpatial(Object agent, VSpatial spatial){
		if (agent instanceof Village){
			Village village = (Village) agent;
			
			//if(community.getPop()==0){spatial=null;} else {
			spatial = shapeFactory.createCircle(setSize(village.total_pop()), 16);
			//}
		}
		return spatial;
	}
	
	
	private float setSize(double pop){
		double maxpop = Constants.carrying_capacity;
		double maxsize = 100; //export to constants ?
		double minsize = 10;
		float size;
		if(pop == 0){
			size = 0;
		} else {
			size=(float) (Math.floor((pop/maxpop)*(maxsize - minsize)) + minsize);
		}
		return size;
		
	}
}
