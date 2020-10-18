package com.github.nickid2018.smcl.parser;

public class LevelInfo {

	public int levelNow = 0;
	public int position = 0;

	public void levelUp() {
		levelNow++;
	}

	public void levelDown() {
		levelNow--;
	}
}
