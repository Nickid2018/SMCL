package com.github.nickid2018.smcl.example;

import java.util.*;
import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.regression.*;

public class RegressionDemo {

	public static final void main(String[] args) {
		SMCL smcl = SMCL.getInstance();
		smcl.globalvars.registerVariable("x");
		Map<Double,Double> map = new HashMap<>();
		map.put(1.0, 64.0);
		map.put(2.0, 8.0);
		map.put(3.0, 4.0);
		//map.put(4.0, 243.0);
		//map.put(5.0, 729.0);
		map.put(6.0, 2.0);
		Statement statement = Regression.doRegression(smcl, map, InvertedIndexModel.MODEL);
		System.out.println("Regression Result: " + statement);
        System.out.println("R2=" + Regression.getCoefficient(map, InvertedIndexModel.MODEL));
	}
}
