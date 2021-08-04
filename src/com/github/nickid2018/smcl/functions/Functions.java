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
package com.github.nickid2018.smcl.functions;

import java.util.function.*;

import com.github.nickid2018.smcl.set.*;

import static com.github.nickid2018.smcl.functions.FunctionBuilder.*;
import static com.github.nickid2018.smcl.functions.FunctionDerivatives.*;

public class Functions {

    // Domain of the logarithm
    public static final DoubleConsumer DOMAIN_LOG = checkDomainExclude(Interval.lessThanInclude(0),
            arg -> "invalid argument for logarithm functions: " + arg + " <=0");
    public static final DoubleConsumer DOMAIN_SQRT = checkDomainExclude(Interval.lessThanExclude(0),
            arg -> "invalid argument for square root: " + arg + " <0");
    // Domain of the tangent/cotangent
    public static final DoubleConsumer DOMAIN_TAN = checkDomainExclude(arg -> {
        double deg = arg * 2 / Math.PI;
        int multi = (int) deg;
        if (Math.abs(deg - multi) > 1E-5)
            return false;
        return multi % 2 == 1;
    }, arg -> "invalid argument for tan/cot: " + arg);
    // Domain of the arcsin and arccos
    public static final DoubleConsumer DOMAIN_ARC = checkDomainInclude(Interval.fromNonInfString("[-1,1]"),
            arg -> "invalid argument for asin/acos: " + arg + " doesn't belong [-1,1]");

    // Basic trigonometric functions
    // sine (sin)
    public static final UnaryFunctionBuilder SIN = UnaryFunctionBuilder.createBuilder("sin")
            .withResolve(RESOLVE_RADIANS).withFunction(Math::sin).withDerivativeResolver(DERIVATIVE_SIN);
    // cosine (cos)
    public static final UnaryFunctionBuilder COS = UnaryFunctionBuilder.createBuilder("cos")
            .withResolve(RESOLVE_RADIANS).withFunction(Math::cos).withDerivativeResolver(DERIVATIVE_COS);
    // tangent (tan)
    public static final UnaryFunctionBuilder TAN = UnaryFunctionBuilder.createBuilder("tan").withDomain(DOMAIN_TAN)
            .withResolve(RESOLVE_RADIANS).withFunction(Math::tan).withDerivativeResolver(DERIVATIVE_TAN);

    // Advanced trigonometric functions
    // cosecant (csc)
    public static final UnaryFunctionBuilder CSC = SIN.copyWithoutFunction("csc").withFunction(arg -> 1 / Math.sin(arg))
            .withDerivativeResolver(DERIVATIVE_CSC);
    // secant (sec)
    public static final UnaryFunctionBuilder SEC = COS.copyWithoutFunction("sec").withFunction(arg -> 1 / Math.cos(arg))
            .withDerivativeResolver(DERIVATIVE_SEC);
    // cotangent (cot)
    public static final UnaryFunctionBuilder COT = TAN.copyWithoutFunction("cot").withFunction(arg -> 1 / Math.tan(arg))
            .withDerivativeResolver(DERIVATIVE_COT);

    // Basic inverse trigonometric functions
    // arc-sine (asin, arcsin)
    public static final UnaryFunctionBuilder ASIN = UnaryFunctionBuilder.createBuilder("asin").withDomain(DOMAIN_ARC)
            .withFunction(Math::asin).withResolveEnd(RESOLVE_DEGREES).withDerivativeResolver(DERIVATIVE_ASIN);
    // arc-cosine (acos, arccos)
    public static final UnaryFunctionBuilder ACOS = UnaryFunctionBuilder.createBuilder("acos").withDomain(DOMAIN_ARC)
            .withFunction(Math::acos).withResolveEnd(RESOLVE_DEGREES).withDerivativeResolver(DERIVATIVE_ACOS);
    // arc-tangent (atan, arctan)
    public static final UnaryFunctionBuilder ATAN = UnaryFunctionBuilder.createBuilder("atan").withFunction(Math::atan)
            .withResolveEnd(RESOLVE_DEGREES).withDerivativeResolver(DERIVATIVE_ATAN);

    // Basic hyperbolic functions
    // hyperbolic sine (sinh, sh)
    public static final UnaryFunctionBuilder SINH = UnaryFunctionBuilder.createBuilder("sinh").withFunction(Math::sinh)
            .withDerivativeResolver(DERIVATIVE_SINH);
    // hyperbolic cosine (cosh, ch)
    public static final UnaryFunctionBuilder COSH = UnaryFunctionBuilder.createBuilder("cosh").withFunction(Math::cosh)
            .withDerivativeResolver(DERIVATIVE_COSH);
    // hyperbolic tangent (tanh, th)
    public static final UnaryFunctionBuilder TANH = UnaryFunctionBuilder.createBuilder("tanh").withFunction(Math::tanh)
            .withDerivativeResolver(DERIVATIVE_TANH);

    // Basic logarithm functions
    // natural logarithm function (ln)
    public static final UnaryFunctionBuilder LN = UnaryFunctionBuilder.createBuilder("ln").withDomain(DOMAIN_LOG)
            .withFunction(Math::log).withDerivativeResolver(DERIVATIVE_LN);
    // common logarithm function (lg)
    public static final UnaryFunctionBuilder LG = UnaryFunctionBuilder.createBuilder("lg").withDomain(DOMAIN_LOG)
            .withFunction(Math::log10).withDerivativeResolver(DERIVATIVE_LG);

    // Basic power & root functions
    // square root (sqrt)
    public static final UnaryFunctionBuilder SQRT = UnaryFunctionBuilder.createBuilder("sqrt").withDomain(DOMAIN_SQRT)
            .withFunction(Math::sqrt).withDerivativeResolver(DERIVATIVE_SQRT);
    // cubic root (cbrt)
    public static final UnaryFunctionBuilder CBRT = UnaryFunctionBuilder.createBuilder("cbrt").withFunction(Math::cbrt)
            .withDerivativeResolver(DERIVATIVE_CBRT);
    // e's power (exp)
    public static final UnaryFunctionBuilder EXP = UnaryFunctionBuilder.createBuilder("exp").withFunction(Math::exp)
            .withDerivativeResolver(DERIVATIVE_EXP);

    // Common functions
    // absolute value (abs or |x|)
    public static final UnaryFunctionBuilder ABS = UnaryFunctionBuilder.createBuilder("abs").withFunction(Math::abs)
            .withDerivativeResolver(DERIVATIVE_ABS);
    // signum function (sgn)
    public static final UnaryFunctionBuilder SGN = UnaryFunctionBuilder.createBuilder("sgn").withFunction(Math::signum);
    // round-up function or "ceil" function (ceil)
    public static final UnaryFunctionBuilder CEIL = UnaryFunctionBuilder.createBuilder("ceil").withFunction(Math::ceil);
    // round-down function or "floor" function (floor)
    public static final UnaryFunctionBuilder FLOOR = UnaryFunctionBuilder.createBuilder("floor")
            .withFunction(Math::floor);
    // rounding function (round)
    public static final UnaryFunctionBuilder ROUND = UnaryFunctionBuilder.createBuilder("round")
            .withFunction(Math::round);
}
