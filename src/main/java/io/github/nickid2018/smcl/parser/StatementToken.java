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

public class StatementToken {

    public String detail = "";
    public StatementTokenType type;
    public int pos;
    public int length;

    public void append(char c) {
        detail += c;
    }

    public void append(String s) {
        detail += s;
    }

    public char charAt(int pos) {
        return detail.charAt(pos);
    }

    public int length() {
        return detail.length();
    }

    public String toString() {
        return detail;
    }
}
