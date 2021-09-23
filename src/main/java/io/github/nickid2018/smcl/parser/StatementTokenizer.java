/*
 * Copyright 2021 Nickid2018
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.nickid2018.smcl.parser;

import io.github.nickid2018.smcl.MathParseException;
import io.github.nickid2018.smcl.SMCLContext;

public class StatementTokenizer {

    public static final char decimalSeparator = '.';
    public static final char unaryOperatorSuffix = 'u';

    private final SMCLContext smcl;
    private final String input;
    private int pos = 0;
    private StatementToken previousToken;

    public StatementTokenizer(SMCLContext smcl, String input) {
        this.smcl = smcl;
        this.input = input;
    }

    private char peekNextChar() {
        return this.pos < this.input.length() - 1 ? this.input.charAt(this.pos + 1) : '\u0000';
    }

    private boolean isHexDigit(char ch) {
        return ch == 'x' || ch == 'X' || ch >= '0' && ch <= '9' || ch >= 'a' && ch <= 'f' || ch >= 'A' && ch <= 'F';
    }

    public boolean hasNextToken() {
        return this.pos < this.input.length();
    }

    public StatementToken nextToken() throws MathParseException {
        StatementToken token = new StatementToken();
        // The string has run out, stop!
        if (pos >= input.length())
            return previousToken = null;
        char ch = input.charAt(pos);
        // Skip whitespaces
        while (Character.isWhitespace(ch) && pos < input.length())
            ch = input.charAt(++pos);
        // Position set!
        token.pos = pos;
        // For numbers formatted in hex
        boolean isHexString;
        if (Character.isDigit(ch)) {
            // Get the number information: it is a hex number?
            isHexString = ch == '0' && (peekNextChar() == 'x' || peekNextChar() == 'X');
            // Get the whole number (we need to read decimal separator and exponent sign
            while (isHexString && isHexDigit(ch) || (Character.isDigit(ch) || ch == decimalSeparator || ch == 'e'
                    || ch == 'E'
                    || ch == '-' && token.length() > 0
                    && ('e' == token.charAt(token.length() - 1) || 'E' == token.charAt(token.length() - 1))
                    || ch == '+' && token.length() > 0
                    && ('e' == token.charAt(token.length() - 1) || 'E' == token.charAt(token.length() - 1)))
                    && pos < input.length()) {
                token.append(input.charAt(pos++));
                ch = pos == input.length() ? '\u0000' : input.charAt(pos);
            }
            token.type = isHexString ? StatementTokenType.HEX_NUMBER : StatementTokenType.NUMBER;
        } else {
            if (!Character.isLetter(ch)) {
                if (ch != '(' && ch != ')' && ch != ',') {
                    // If the letter is not blanket or comma
                    // Try to match next blanket, comma or any operator, try to read all sentences
                    String greedyMatch = "";
                    int initialPos = pos;
                    ch = input.charAt(pos);

                    int validOperatorSeenUntil = -1;
                    for (; (!Character.isLetter(ch) && !Character.isDigit(ch) && !Character.isWhitespace(ch))
                            && (ch != '(' && ch != ')' && ch != ',')// Ensure the character is not these
                            && pos < input.length(); ch = pos == input.length() ? 0 : input.charAt(pos)) {
                        greedyMatch = greedyMatch + ch;
                        ++pos;
                        // Check whether it is an operator
                        if (smcl.register.containsOperator(greedyMatch)) {
                            validOperatorSeenUntil = pos;
                        }
                    }

                    // Generate string
                    if (validOperatorSeenUntil != -1) {
                        // Found
                        token.append(input.substring(initialPos, validOperatorSeenUntil));
                        pos = validOperatorSeenUntil;
                    } else {
                        // No find
                        token.append(greedyMatch);
                    }

                    // Sign the token
                    if (previousToken != null && previousToken.type != StatementTokenType.OPERATOR
                            && previousToken.type != StatementTokenType.OPEN_PAREN
                            && previousToken.type != StatementTokenType.COMMA) {
                        token.type = StatementTokenType.OPERATOR;
                    } else {
                        token.detail = token.detail + unaryOperatorSuffix;
                        token.type = StatementTokenType.UNARY_OPERATOR;
                    }
                } else {
                    if (ch == '(') {
                        token.type = StatementTokenType.OPEN_PAREN;
                    } else if (ch == ')') {
                        token.type = StatementTokenType.CLOSE_PAREN;
                    } else {
                        token.type = StatementTokenType.COMMA;
                    }
                    token.append(ch);
                    ++pos;
                }
            } else {
                // If we can't parse this as number or any structure statement, we have to read
                // until a character that may be parsed
                while ((Character.isLetter(ch) || Character.isDigit(ch) || token.length() == 0)
                        && pos < input.length()) {
                    token.append(input.charAt(pos++));
                    ch = pos == input.length() ? 0 : input.charAt(pos);
                }

                // Skip whitespaces
                if (Character.isWhitespace(ch)) {
                    while (Character.isWhitespace(ch) && pos < input.length())
                        ch = input.charAt(pos++);
                    --pos;
                }

                // Sign the statement is a function or a variable
                // P.S. The 'VARIABLE' does not really mean a variable, it may be a function or
                // number statement, like these:
                // sinx ln2 sqrt444
                // The first statement should be parsed as 'FUNCTION' while the algorithm
                // returns 'VARIABLE' (Exactly speaking, this is a complex-statement) The second
                // and third statement should be parsed as 'NUMBER'
                // To deal with this statement, SMCLSettings has a field called
                // 'strictFunction' can allow/refuse this kind of statement and the setting will
                // come into existence when the RPN algorithm working
                token.type = ch == '(' ? StatementTokenType.FUNCTION : StatementTokenType.VARIABLE;
            }
        }

        // Finally, we should verify the token
        token.length = previousToken == null ? pos : pos - previousToken.pos - previousToken.length;
        if (previousToken == null
                || token.type != StatementTokenType.NUMBER && token.type != StatementTokenType.HEX_NUMBER
                && token.type != StatementTokenType.VARIABLE && token.type != StatementTokenType.FUNCTION
                || previousToken.type != StatementTokenType.VARIABLE
                && previousToken.type != StatementTokenType.FUNCTION
                && previousToken.type != StatementTokenType.NUMBER
                && previousToken.type != StatementTokenType.CLOSE_PAREN
                && previousToken.type != StatementTokenType.HEX_NUMBER) {
            return previousToken = token;
        } else {
            throw new MathParseException(
                    "The token \"" + token.detail + "\" can't be after the token \"" + previousToken.detail + "\"",
                    input, token);
        }
    }
}
