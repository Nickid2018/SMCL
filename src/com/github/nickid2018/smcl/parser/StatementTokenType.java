/*
 * Copyright 2021 Nickid2018
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.nickid2018.smcl.parser;

public enum StatementTokenType {

    /**
     * For variables defined in SMCL
     */
    VARIABLE,
    /**
     * For functions like sin or atan
     */
    FUNCTION,
    /**
     * Plain numbers
     */
    NUMBER,
    /**
     * Operators like '+' or '-'
     */
    OPERATOR,
    /**
     * Unary operator
     */
    UNARY_OPERATOR,
    /**
     * Left blanket (
     */
    OPEN_PAREN,
    /**
     * Comma ,
     */
    COMMA,
    /**
     * Right blanket )
     */
    CLOSE_PAREN,
    /**
     * Hex number
     */
    HEX_NUMBER
}
