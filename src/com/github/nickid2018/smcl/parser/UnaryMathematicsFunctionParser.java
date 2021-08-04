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

import java.util.function.*;

import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.functions.*;

public class UnaryMathematicsFunctionParser<T extends UnaryFunctionStatement> extends FunctionParser<T> {

    private final BiFunction<SMCL, Statement[], ? extends UnaryFunctionStatement> map;

    public UnaryMathematicsFunctionParser(UnaryFunctionBuilder unary) {
        map = unary::create;
    }

    public UnaryMathematicsFunctionParser(BiFunction<SMCL, Statement[], ? extends UnaryFunctionStatement> map) {
        this.map = map;
    }

    @Override
    public boolean numParamsVaries() {
        return false;
    }

    @Override
    public int getNumParams() {
        return 1;
    }

    @Override
    public UnaryFunctionStatement parseStatement(SMCL smcl, Statement... statements) {
        return map.apply(smcl, statements);
    }

}
