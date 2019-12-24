package com.cj.jmcl;

import java.util.*;

import com.cj.jmcl.func.Function;

public class PowerStatement extends MathStatement {
	
	private MathStatement first; 
	private Set<MathStatement> muls=new HashSet<>();
	
	@Override
	public double calc(Map<String, Double> values) {
		double ret=first.calc(values);
		for(MathStatement ms:muls) {
			double mul=ms.calc(values);
			ret=Math.pow(ret, mul);
		}
		return ret;
	}
	
	@Override
	public boolean isAllNum() {
		if(!(first instanceof Number))return false;
		for(MathStatement en:muls) {
			if(!(en instanceof Number))return false;
		}
		return true;
	}

	public static final PowerStatement format(String s) throws MathException {
		PowerStatement ms=new PowerStatement();
		boolean a=true;
		int begin=0;
		int intimes=0;
		for(int i=0;i<s.length();i++) {
			char c=s.charAt(i);
			if(c=='(')intimes++;
			else if(c==')')intimes--;
			else if(c=='^'&&intimes==0) {
				String sub=s.substring(begin,i);
				begin=i+1;
				if(i==0)continue;
				if(i!=0) {
					MathStatement tmp=JMCLRegister.getStatement(sub);
					if(a) {
						ms.first=tmp;
						a=false;
					}else {
						ms.muls.add(tmp);
					}
				}
			}
			if(i==s.length()-1) {
				if(intimes!=0) {
					throw new MathException("Parentheses are not paired",s,i);
				}
				String sub=s.substring(begin,s.length());
				MathStatement tmp=JMCLRegister.getStatement(sub);
				ms.muls.add(tmp);
			}
		}
		return ms;
	}
	
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append(first.toString());
		for(MathStatement en:muls) {
			if(!(en instanceof Number)&&!(en instanceof Variable)&&!(en instanceof Function))
				sb.append("^("+en+")");
			else sb.append("^"+en);
		}
		return sb+"";
	}
}
