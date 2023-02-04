/*
 * Copyright 2021-2023 Nickid2018
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
package io.github.nickid2018.smcl;

import io.github.nickid2018.smcl.parser.StatementToken;

/**
 * The exception throws when parser can't parse the string into a correct statement.
 */
public class StatementParseException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = -7821386249067385803L;
    private final StatementToken token;
    private String statement;

    /**
     * Construct the exception.
     * @param desc the description of the exception
     * @param statement the statement of the exception
     * @param token the token when the exception occurred
     */
    public StatementParseException(String desc, String statement, StatementToken token) {
        super(desc);
        this.statement = statement;
        this.token = token;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + (token != null
                ? " (Position: " + token.pos + ", type: " + token.type + "): " + statement.substring(0, token.pos) + "["
                + token + "]" + (token.pos + token.length > statement.length() ? "" : statement.substring(token.pos + token.length))
                : ": [" + statement + "]");
    }

    /**
     * Get the statement for the exception.
     * @return an exception
     */
    public String getStatement() {
        return statement;
    }

    /**
     * Set the statement of the exception.
     * @param statement a statement
     */
    public void setStatement(String statement) {
        this.statement = statement;
    }
}
