package simple_model;

public class Parameters {

	public static void init_params(repast.simphony.parameter.Parameters params){
		Constants.growth_rate = (double)params.getValue("growth_rate");
		Constants.newborn_male_bias = (int)params.getValue("newborn_male_bias");
		Constants.warfare_pressure = (int)params.getValue("warfare_pressure");
		Constants.yearly_warfare_mortality = (double)params.getValue("yearly_warfare_mortality");
		Constants.preferred_marriage_weight = (double)params.getValue("preferred_marriage_weight");
		Constants.change_residence_pause = (int)params.getValue("change_residence_pause");
		Constants.carrying_capacity = (int)params.getValue("carrying_capacity");
		Constants.X_communities = (int)params.getValue("sizeX");
		Constants.Y_communities = (int)params.getValue("sizeY");
		Constants.allow_matrilocal = (int)params.getValue("allow_matrilocal");
	}
}
