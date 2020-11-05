package com.github.nickid2018.smcl;

public class SMCLSettings {

	// Parsing settings
	public boolean globalVariablesEnabled = true;
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
