package com.github.nickid2018.jmcl;

import java.util.*;

import com.github.nickid2018.jmcl.Number;
import com.github.nickid2018.jmcl.func.*;

public class DivideStatement extends MathStatement{
	
	private MathStatement first; 
	private Set<MathStatement> divs=new HashSet<>(); 
	
	@Override
	public double calc(Map<String, Double> values) {
		double ret=first.calc(values);
		for(MathStatement ms:divs) {
			double v=ms.calc(values);
			if(v==0)throw new ArithmeticException("divide by 0");
			ret/=v;
		}
		return ret;
	}
	
	@Override
	public boolean isAllNum() {
		if(!(first instanceof Number))return false;
		for(MathStatement en:divs) {
			if(!(en instanceof Number))return false;
		}
		return true;
	}

	public static final DivideStatement format(String s) throws MathException {
		DivideStatement ms=new DivideStatement();
		boolean a=true;
		int begin=0;
		int intimes=0;
		for(int i=0;i<s.length();i++) {
			char c=s.charAt(i);
			if(c=='(')intimes++;
			else if(c==')')intimes--;
			else if(c=='/'&&intimes==0) {
				String sub=s.substring(begin,i);
				begin=i+1;
				if(i==0)continue;
				if(i!=0) {
					MathStatement tmp=JMCLRegister.getStatement(sub);
					if(a) {
						ms.first=tmp;
						a=false;
					}else {
						ms.divs.add(tmp);
					}
				}
			}
			if(i==s.length()-1) {
				if(intimes!=0) {
					throw new MathException("Parentheses are not paired",s,i);
				}
				String sub=s.substring(begin,s.length());
				MathStatement tmp=JMCLRegister.getStatement(sub);
				ms.divs.add(tmp);
			}
		}
		return ms;
	}
	
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append(first.toString());
		for(MathStatement en:divs) {
			if(!(en instanceof Number)&&!(en instanceof Variable)&&!(en instanceof Function))
				sb.append("/("+en+")");
			else sb.append("/"+en);
		}
		return sb+"";
	}
}
