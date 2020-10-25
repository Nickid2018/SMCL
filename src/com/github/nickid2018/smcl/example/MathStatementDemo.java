package com.github.nickid2018.smcl.example;

import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.parser.*;

public class MathStatementDemo {

	public static void main(String[] args) throws Exception {
        SMCL smcl = SMCL.getInstance();
        smcl.init();
        long t = System.currentTimeMillis();
        Statement statement = BaseParser.parseStatement("2", smcl);
        System.out.println(System.currentTimeMillis() - t);
        t = System.currentTimeMillis();
        System.out.println(statement.calculate(new VariableList()));
        System.out.println(System.currentTimeMillis() - t);
        t = System.currentTimeMillis();
        System.out.println(System.currentTimeMillis() - t);
    }
}
