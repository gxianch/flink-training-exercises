package com.ververica.flinktraining.solutions.datastream_java.basics;

import com.ververica.flinktraining.exercises.datastream_java.datatypes.TaxiRide;
import com.ververica.flinktraining.exercises.datastream_java.sources.TaxiRideSource;
import com.ververica.flinktraining.exercises.datastream_java.utils.GeoUtils;
import com.ververica.flinktraining.solutions.datastream_java.basics.xxRideCleansingSolution.EnrichedRide;
import com.ververica.flinktraining.exercises.datastream_java.utils.ExerciseBase;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class xxRideCleansingSolution extends ExerciseBase {
	public static class EnrichedRide extends TaxiRide {
		  public int startCell;
		  public int endCell;

		  public EnrichedRide() {}

		  public EnrichedRide(TaxiRide ride) {
		    this.rideId = ride.rideId;
		    this.isStart = ride.isStart;
		    this.startCell = GeoUtils.mapToGridCell(ride.startLon, ride.startLat);
		    this.endCell = GeoUtils.mapToGridCell(ride.endLon, ride.endLat);
		  }

		  public String toString() {
		    return super.toString() + "," +
		      Integer.toString(this.startCell) + "," +
		      Integer.toString(this.endCell);
		  }
		}

	public static class NYCFilter implements FilterFunction<TaxiRide> {

		@Override
		public boolean filter(TaxiRide taxiRide) throws Exception {
			return GeoUtils.isInNYC(taxiRide.startLon, taxiRide.startLat) &&
					GeoUtils.isInNYC(taxiRide.endLon, taxiRide.endLat);
		}

	}

	public static void main(String[] args) throws Exception {
		ParameterTool params =ParameterTool.fromArgs(args);
		final String input = params.get("input",pathToRideData);
		final int maxEventDelay = 60;       // events are out of order by max 60 seconds
		final int servingSpeedFactor = 600; // events of 10 minutes are served in 1 second
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		env.setParallelism(ExerciseBase.parallelism);
		// start the data generator
		DataStream<TaxiRide> rides = env.addSource(rideSourceOrTest(new TaxiRideSource(input,maxEventDelay,servingSpeedFactor)));
		printOrTest(rides);
		rides.filter(taxiRide -> GeoUtils.isInNYC(taxiRide.startLon, taxiRide.startLat) &&
				GeoUtils.isInNYC(taxiRide.endLon, taxiRide.endLat))
		.map(m -> new EnrichedRide(m));
		env.execute("Taxi Ride Cleansing");
	}
}
