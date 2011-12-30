package org.ic.tennistrader.domain;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.ic.tennistrader.domain.ChartData;
import org.ic.tennistrader.domain.markets.MOddsMarketData;
import org.junit.Before;
import org.junit.Test;

import org.ic.tennistrader.utils.Pair;
import static org.ic.tennistrader.utils.Pair.pair;

public class ChartDataTest {

	MOddsMarketData data;
	ChartData chartData = new ChartData(null);

	@Before
	public void setUp(){
		data = new MOddsMarketData();
		data.setPlayer1("Nadal");
		data.setPlayer2("Federer");
		ArrayList<Pair<Double,Double>> pl1Back = new ArrayList<Pair<Double,Double>>();
		ArrayList<Pair<Double,Double>> pl1Lay = new ArrayList<Pair<Double,Double>>();
		ArrayList<Pair<Double,Double>> pl2Back = new ArrayList<Pair<Double,Double>>();
		ArrayList<Pair<Double,Double>> pl2Lay = new ArrayList<Pair<Double,Double>>();
		data.setPl1Back(pl1Back);
		data.setPl1Lay(pl1Lay);
		data.setPl2Back(pl2Back);
		data.setPl2Lay(pl2Lay);
	}
	
	@Test
	public void testAddValue(){
		chartData.updateData(data);
		assertEquals(1, chartData.getMaPl1().size());
		assertEquals(1, chartData.getMaPl2().size());
		assertEquals(1, chartData.getPl1Lay().size());
		assertEquals(1, chartData.getPl1YSeries().size());
		assertEquals(1, chartData.getPl2Lay().size());
		assertEquals(1, chartData.getPl2YSeries().size());
		data.getPl1Back().add(pair(1.5,100.0));
		chartData.updateData(data);
		assertEquals(2, chartData.getDataSize());
		
		
	}
	
	@Test
	public void testMA(){
		
		data.getPl1Back().add( pair(1.5,100.0));
		data.setPl1LastMatchedPrice(1.5);
		chartData.updateData(data);
		data.getPl1Back().add(0, pair(2.0,100.0));
		data.setPl1LastMatchedPrice(2.0);
		chartData.updateData(data);
		data.getPl1Back().add(0, pair(2.5,100.0));
		data.setPl1LastMatchedPrice(2.5);
		chartData.updateData(data);
		ArrayList<Double> ma = chartData.getMaPl1();
		double x = ma.get(2);
		assertTrue(x==2);
	}

	@Test
	public void testAddLay(){
		data.getPl1Back().add(pair(1.5,100.0));
		data.getPl1Lay().add(pair(2.1,100.0));
		chartData.updateData(data);
		ArrayList<Pair<Double, Double>> res = new ArrayList<Pair<Double,Double>>();
		res.add(pair(2.1,1.5));
		System.out.println(chartData.getPl1Lay().get(0).first());
		System.out.println(res.get(0).first());
		System.out.println(chartData.getPl1Lay().get(0).second());
		System.out.println(res.get(0).second());
		assertEquals(res.size(),chartData.getPl1Lay().size());
//		Pair p1 = res.get(0);
//		Pair p2 = chartData.getPl1Lay().get(0);
		//assertEquals(p1,p2);
		//assertEquals(res.get(0), chartData.getPl1Lay().get(0));
		
	}
	
	
	@Test
	public void testDataSize() {
		chartData.updateData(data);
		assertEquals(1,chartData.getPl1YSeries().size());
	}
	


}
