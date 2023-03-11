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
import io.github.nickid2018.smcl.parser.StatementTokenType;

import java.util.ResourceBundle;

/**
 * The exception throws when parser can't parse the string into a correct statement.
 */
public class StatementParseException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = -7821386249067385803L;
    private final SMCLContext context;
    private final StatementToken token;
    private String statement;

    /**
     * Construct the exception.
     * @param context the context of the exception
     * @param desc the description of the exception
     * @param statement the statement of the exception
     * @param token the token when the exception occurred
     */
    public StatementParseException(SMCLContext context, String desc, String statement, StatementToken token) {
        super(desc);
        this.context = context;
        this.statement = statement;
        this.token = token;
    }

    @Override
    public String getMessage() {
        ResourceBundle bundle = context.settings.resourceBundle;
        String tokenString = token != null ? tokenToString() : "[" + statement + "]";
        return String.format(bundle.getString("smcl.parse.error"), super.getMessage(), tokenString);
    }

    private String tokenToString() {
        ResourceBundle bundle = context.settings.resourceBundle;
        StringBuilder builder = new StringBuilder();
        String nameTokenType = bundle.getString("smcl.parse.token_type." + token.type.name().toLowerCase());
        String detailToken = token.detail;
        String tokenString = statement.substring(0, token.pos)
                + "[" + (token.type == StatementTokenType.UNARY_OPERATOR ? detailToken.substring(0, detailToken.length() - 1) : detailToken) + "]"
                + (token.pos + token.length > statement.length() ? "" : statement.substring(token.pos + token.length));
        builder.append(String.format(bundle.getString("smcl.parse.token"), token.pos, nameTokenType, tokenString));
        return builder.toString();
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
