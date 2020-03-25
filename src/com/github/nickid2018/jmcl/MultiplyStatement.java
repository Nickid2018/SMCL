package com.github.nickid2018.jmcl;

import java.util.*;

import com.github.nickid2018.jmcl.func.Function;

public class MultiplyStatement extends MathStatement{
	
	private Set<MathStatement> subs=new HashSet<>(); 
	
	@Override
	public double calc(Map<String, Double> values) {
		double all=1;
		for(MathStatement ms:subs) {
			all*=ms.calc(values);
		}
		return all;
	}
	
	@Override
	public boolean isAllNum() {
		for(MathStatement en:subs) {
			if(!(en instanceof Number))return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		boolean first=true;
		for(MathStatement ms:subs) {
			if(first)first=false;
			else sb.append("*");
			if(ms.getClass().equals(Variable.class)||ms.getClass().getSuperclass().equals(Function.class)||ms instanceof Number)
				sb.append(ms);
			else sb.append("("+ms+")");
		}
		return sb.toString();
	}

	public static final MultiplyStatement format(String s) throws MathException {
		MultiplyStatement ms=new MultiplyStatement();
		int begin=0;
		int intimes=0;
		for(int i=0;i<s.length();i++) {
			char c=s.charAt(i);
			if(c=='(')intimes++;
			else if(c==')')intimes--;
			else if(c=='*'&&intimes==0) {
				String sub=s.substring(begin,i);
				begin=i+1;
				if(i==0)continue;
				if(i!=0) {
					MathStatement tmp=JMCLRegister.getStatement(sub);
					ms.subs.add(tmp);
				}
			}
			if(i==s.length()-1) {
				if(intimes!=0) {
					throw new MathException("Parentheses are not paired",s,i);
				}
				String sub=s.substring(begin,s.length());
				MathStatement tmp=JMCLRegister.getStatement(sub);
				ms.subs.add(tmp);
			}
		}
		Set<MultiplyStatement> mss=new HashSet<>();
		for(MathStatement en:ms.subs) {
			if(en.getClass().equals(MultiplyStatement.class)) {
				mss.add((MultiplyStatement) en);
			}
		}
		if(mss.size()>0) {
			for(MultiplyStatement n:mss) {
				ms.subs.remove(n);
				ms.subs.addAll(n.subs);
			}
		}
		return ms;
	}
}
