package org.ic.protrade.model.prediction;

import java.util.ArrayList;
import java.util.Collections;

public class StatisticMeasures {
	
	public static double InterQuartileRange(ArrayList<Double> list){
		double[] quartiles = Quartiles(list);
		return quartiles[2] - quartiles[0];
	}
	
	public static double[] Quartiles(ArrayList<Double> values)
	{ 
		if (values.size() < 3){
			return new double[] {(1/4)*Avg(values), Avg(values), (3/4)*Avg(values)};
		}
	    double median = Median(values);
	 
	    ArrayList<Double> lowerHalf = GetValuesLessThan(values, median, true);
	    ArrayList<Double> upperHalf = GetValuesGreaterThan(values, median, true);
	 
	    return new double[] {Median(lowerHalf), median, Median(upperHalf)};
	}
	 
	public static ArrayList<Double> GetValuesGreaterThan(ArrayList<Double> values, double limit, boolean orEqualTo)
	{
	    ArrayList<Double> modValues = new ArrayList<Double>();
	 
	    for (double value : values)
	        if (value > limit || (value == limit && orEqualTo))
	            modValues.add(value);
	 
	    return modValues;
	}
	 
	public static ArrayList<Double> GetValuesLessThan(ArrayList<Double> values, double limit, boolean orEqualTo)
	{
	    ArrayList<Double> modValues = new ArrayList<Double>();
	 
	    for (double value : values)
	        if (value < limit || (value == limit && orEqualTo))
	            modValues.add(value);
	 
	    return modValues;
	}
	
	public static double Median(ArrayList<Double> values)
	{
	    Collections.sort(values);
	 
	    if (values.size() % 2 == 1)
		return values.get((values.size()+1)/2-1);
	    else
	    {
		double lower = values.get(values.size()/2-1);
		double upper = values.get(values.size()/2);
	 
		return (lower + upper) / 2.0;
	    }	
	}
	
	public static double Avg(ArrayList<Double> list){
		double res = 0;
		for (double elem:list){
			res += elem;
		}
		return res/(list.size());
	}	
}