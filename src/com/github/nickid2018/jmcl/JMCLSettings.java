package com.github.nickid2018.jmcl;

public class JMCLSettings {

	// Parsing settings

	/**
	 * Max stack depth for parsing
	 */
	public int maxStackSize = 100;

	/**
	 * Enable global variables
	 */
	public boolean globalVariablesEnabled = true;

	/**
	 * Enable multi-functions like log
	 */
	public boolean multiFunctionEnabled = false;
	public int minFunctionNameLength = 2;
	public int maxFunctionNameLength = 5;
	public boolean strictFunction = false;

	// Calculating settings
	public boolean degreeAngle = false;
	public boolean invalidArgumentWarn = false;
	public boolean strictVariableCheck = false;

	// Optimizing settings
	public boolean disableNumberPool = false;
	public boolean mergeNumbers = true;
	public boolean pushStackWhenParse = false;
}
