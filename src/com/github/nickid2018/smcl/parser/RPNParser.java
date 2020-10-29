package com.github.nickid2018.smcl.parser;

import java.util.*;
import com.github.nickid2018.smcl.*;

public class RPNParser {

    private static String normalizeString(String str) throws MathException {
        if (str == null) {
            throw new MathException("The expression is empty", str, 0);
        }
        str = str.replace(" ", "");
        str = str.replace("（", "(");
        str = str.replace("）", ")");
        return str;
    }

    private static int getFourOperatorPriority(String operator, String expr, int now) throws MathException {
        switch (operator) {
            case "+":
            case "-":
                return 0;
            case "*":
            case "/":
                return 1;
            case "(":
            case ")":
                return -1;
            default:
                throw new MathException("Uncognized tag " + operator, expr, now);
        }
    }

    public static List<String> makeOnlyFourOperationRPN(String str) throws MathException {
        str = normalizeString(str);
        Stack<String> output = new Stack<>();
        Stack<String> operand = new Stack<>();
        char[] charArray = str.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            char now = charArray[i];
            if (now == '(') {
                // if the character is left blanket, save the character into operand stack
                operand.push(now + "");
            } else if (now == ')') {
                // if the character is right blanket, pop the operand stack to the last left blanket
                while (!operand.isEmpty()) {
                    String pop = operand.pop();
                    if (pop.equals("("))
                        break;
                    output.push(pop);
                }
            } else if ((now >= '0' && now <= '9') || now == '.') {
                // if the character is a number, push to output stack
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(now);
                for (int j = i + 1; j < charArray.length; j++) {
                    char numnow = charArray[j];
                    if ((numnow < '0' || numnow > '9') && numnow != '.') {
                        i = j - 1;
                        break;
                    } else {
                        stringBuilder.append(numnow);
                        i = j;
                    }
                }
                output.push(stringBuilder.toString());
            } else {
                // if the character is an operator and the priorty of the stack top is higher, pop operand and push output
                while (!operand.empty() && getFourOperatorPriority(operand.peek(), str, i) >= getFourOperatorPriority(now + "", str, i))
                    output.push(operand.pop());
                operand.push(now + "");
            }
        }
        while (!operand.isEmpty())
            output.push(operand.pop());
        List<String> outputList = new ArrayList<>();
        while (!output.isEmpty())
            outputList.add(0, output.pop());
        return outputList;
    }

    public static double calcFourOpreationResult(List<String> rpn) {
        Stack<Double> stack = new Stack<>();
        for (String s : rpn) {
            if ("*".equals(s)) {
                stack.push(stack.pop() * stack.pop());
            } else if ("-".equals(s)) {
                stack.push(-stack.pop() + stack.pop());
            } else if ("+".equals(s)) {
                stack.push(stack.pop() + stack.pop());
            } else if ("/".equals(s)) {
                double one = stack.pop();
                double two = stack.pop();
                stack.push(two / one);
            } else {
                stack.push(Double.parseDouble(s));
            }
        }
        if (stack.size() == 1) {
            return stack.pop();
        }
        return 0;
    }


}
