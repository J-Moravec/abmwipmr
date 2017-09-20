package simple_model;

import java.util.ArrayList;
import java.util.List;

public class Village_list extends ArrayList<Village> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void destroy_empty() {
		List<Village> villages_to_remove = new ArrayList<Village>();
		for (Village village : this){
			if (village.power() == 0 ){
				village.kill();
				villages_to_remove.add(village);
			}
		}
		this.removeAll(villages_to_remove);
	}
	
	
	public int num_villages_in_zone(){
	    int num_villages_in_zone = 0;
		for(Village village : this){
			if(village.is_in_zone()){
				num_villages_in_zone += 1;
			}
		}
		
		return num_villages_in_zone;
	}
	
	
	public void marry() {
		List<Village> marriage_list = new ArrayList<Village>(this);
		//first filter marriage list
		List<Village> empty_villages = new ArrayList<Village>();
		for(Village village : marriage_list){
			if(!village.has_marriable_people()){
				empty_villages.add(village);
			}
		}
		marriage_list.removeAll(empty_villages);
		
		//repeat until no village to marry
		while(marriage_list.isEmpty() == false){
			int length = marriage_list.size();
			double[] frequency_array = new double[length];
			for(int i = 0; i < length; i++){
				// probability that village will marry depends on total number of marriable 
				frequency_array[i] = marriage_list.get(i).get_marriable_people();
			}
			int index = Utils.random_roll(frequency_array);
			Village source_village = marriage_list.get(index);
			Village target_village = source_village.marry();
			/*
			System.out.println("frequency_array" + Arrays.toString(frequency_array));
			if (married_village == null){
				System.out.println(marry_village.name() + " could not find suitable partner");
			} else {
				System.out.println("Married " + marry_village.name());
				System.out.println("with " + married_village.name());
			}
			*/
			if(!source_village.has_marriable_people()){
				marriage_list.remove(source_village);
			}
			if(target_village != null && !target_village.has_marriable_people()){
				marriage_list.remove(target_village);
			}
			
			
		}
		for (Village village : this){
			village.remove_temp();
		}
	}
	
	
	public void grow() {
		for (Village village : this){
			village.grow();
		}
	}
	
	
	
	/** change
	 * 
	 *  Function describe change of residence for all villages in community.
	 *  First, change for every individual village is tested and if test is true, residences are
	 *  later changed. This is done in two cycles as change_residence_test is dependent on
	 *  neighbours.
	 */
	public void change_residence() {
		List<Village> changing_residence = new ArrayList<Village>();
		for(Village village : this){
			if(village.roll_for_residence_change()){
				changing_residence.add(village);
			}
		}
		
		for(Village village : changing_residence){
			village.change_residence();
		}
	}
	
}
