package com.github.nickid2018.smcl.example;

import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.parser.*;

public class MathStatementDemo {

	public static void main(String[] args) throws Exception {
        long t = System.currentTimeMillis();
        System.out.println(RPNParser.calcFourOpreationResult(RPNParser.makeOnlyFourOperationRPN("4*1+(2.9/0.1)*4/2")));
        System.out.println(System.currentTimeMillis() - t);
    }
}
