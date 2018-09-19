package simple_model;

import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.SimpleCartesianAdder;
import repast.simphony.space.continuous.StrictBorders;
import simple_model.Step;

public class simple_model_builder implements ContextBuilder<Object> {

	@Override
	public Context<Object> build(Context<Object> context) {
		context.setId("simple_model");


		repast.simphony.parameter.Parameters params = RunEnvironment.getInstance().getParameters();
		Parameters.init_params(params);
		Constants.init_derived_parameter();		


		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.
				createContinuousSpaceFactory(null);
		ContinuousSpace<Object> space = spaceFactory.createContinuousSpace(
				"space",
				context,
				new SimpleCartesianAdder<Object>(),
				new StrictBorders(),
				new double[] {Constants.space_size_X, Constants.space_size_Y},
				new double[] {0, 0}
				);


		// Init grid and villages
		Village[][] helper_grid = new Village[Constants.X_communities][Constants.Y_communities];
		Village_list village_list = new Village_list();
		for(int x = 0; x < Constants.X_communities; x++){
			for(int y = 0; y < Constants.Y_communities; y++){
					helper_grid[x][y] = new Village(x, y);
					village_list.add(helper_grid[x][y]);
					context.add(helper_grid[x][y]);
					space.moveTo(
							helper_grid[x][y],
							Constants.calculate_point(x),
							Constants.calculate_point(y)
							);
				}
			}


		context.add(new Stats());
		Step.context = context;		
		Initialize.init(village_list, helper_grid);
		RunEnvironment.getInstance().endAt(Constants.number_of_generations);
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		ScheduleParameters schedule_params = ScheduleParameters.createRepeating(
				1,1
				);
		schedule.schedule(schedule_params, this, "step", village_list);


		return context;
	}
	public void step(Village_list village_list){
		Step.step(village_list);
	}
}

