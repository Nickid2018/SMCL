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
package io.github.nickid2018.smcl.functions;

import io.github.nickid2018.smcl.number.NumberObject;
import io.github.nickid2018.smcl.number.StdNumberObject;
import io.github.nickid2018.smcl.set.Interval;

import java.util.function.Consumer;

import static io.github.nickid2018.smcl.functions.FunctionBuilder.*;
import static io.github.nickid2018.smcl.functions.FunctionDerivatives.*;

public class Functions {

    /**
     * Domain of the logarithm
     */
    public static final Consumer<NumberObject> DOMAIN_LOG = checkDomainExclude(Interval.lessThanInclude(0),
            arg -> "invalid argument for logarithm functions: " + arg + " <=0");
    /**
     * Domain of the square root
     */
    public static final Consumer<NumberObject> DOMAIN_SQRT = checkDomainExclude(Interval.lessThanExclude(0),
            arg -> "invalid argument for square root: " + arg + " <0");
    /**
     * Domain of the tangent/cotangent
     */
    public static final Consumer<NumberObject> DOMAIN_TAN = checkDomainExclude(arg -> {
        if (!arg.isReal())
            return false;
        double deg = arg.toStdNumber() * 2 / Math.PI;
        int multi = (int) deg;
        if (Math.abs(deg - multi) > 1E-5)
            return false;
        return multi % 2 == 1;
    }, arg -> "invalid argument for tan/cot: " + arg);
    /**
     * Domain of the arcsin and arccos
     */
    public static final Consumer<NumberObject> DOMAIN_ARC = checkDomainInclude(Interval.fromNonInfString("[-1,1]"),
            arg -> "invalid argument for asin/acos: " + arg + " doesn't belong [-1,1]");
    /**
     * Domain of N
     */
    public static final Consumer<NumberObject> DOMAIN_NATURAL = checkDomainInclude(
            arg -> arg.isReal() && arg.toStdNumber() >= 0 && Math.abs(Math.round(arg.toStdNumber()) - arg.toStdNumber()) < 1E-5,
            arg -> "invalid argument - The number isn't a natural number");

    // Basic trigonometric functions
    /** sine (sin) */
    public static final UnaryFunctionBuilder SIN = UnaryFunctionBuilder.createBuilder("sin")
            .withResolve(RESOLVE_RADIANS).withFunction(NumberObject::sin).withDerivativeResolver(DERIVATIVE_SIN);
    /** cosine (cos) */
    public static final UnaryFunctionBuilder COS = UnaryFunctionBuilder.createBuilder("cos")
            .withResolve(RESOLVE_RADIANS).withFunction(NumberObject::cos).withDerivativeResolver(DERIVATIVE_COS);
    /** tangent (tan) */
    public static final UnaryFunctionBuilder TAN = UnaryFunctionBuilder.createBuilder("tan").withDomain(DOMAIN_TAN)
            .withResolve(RESOLVE_RADIANS).withFunction(NumberObject::tan).withDerivativeResolver(DERIVATIVE_TAN);
    /** cotangent (cot) */
    public static final UnaryFunctionBuilder COT = TAN.copyWithoutFunction("cot").withFunction(arg -> arg.tan().reciprocal())
            .withDerivativeResolver(DERIVATIVE_COT);
    // Advanced trigonometric functions
    /** co-secant (csc) */
    public static final UnaryFunctionBuilder CSC = SIN.copyWithoutFunction("csc").withFunction(arg -> arg.sin().reciprocal())
            .withDerivativeResolver(DERIVATIVE_CSC);
    /** secant (sec) */
    public static final UnaryFunctionBuilder SEC = COS.copyWithoutFunction("sec").withFunction(arg -> arg.cos().reciprocal())
            .withDerivativeResolver(DERIVATIVE_SEC);
    // Basic inverse trigonometric functions
    /** arc-sine (asin, arcsin) */
    public static final UnaryFunctionBuilder ASIN = UnaryFunctionBuilder.createBuilder("asin").withDomain(DOMAIN_ARC)
            .withFunction(NumberObject::asin).withResolveEnd(RESOLVE_DEGREES).withDerivativeResolver(DERIVATIVE_ASIN);
    /** arc-cosine (acos, arccos) */
    public static final UnaryFunctionBuilder ACOS = UnaryFunctionBuilder.createBuilder("acos").withDomain(DOMAIN_ARC)
            .withFunction(NumberObject::acos).withResolveEnd(RESOLVE_DEGREES).withDerivativeResolver(DERIVATIVE_ACOS);
    /** arc-tangent (atan, arctan) */
    public static final UnaryFunctionBuilder ATAN = UnaryFunctionBuilder.createBuilder("atan").withFunction(NumberObject::atan)
            .withResolveEnd(RESOLVE_DEGREES).withDerivativeResolver(DERIVATIVE_ATAN);

    // Basic hyperbolic functions
    /** hyperbolic sine (sinh, sh) */
    public static final UnaryFunctionBuilder SINH = UnaryFunctionBuilder.createBuilder("sinh").withFunction(NumberObject::sinh)
            .withDerivativeResolver(DERIVATIVE_SINH);
    /** hyperbolic cosine (cosh, ch) */
    public static final UnaryFunctionBuilder COSH = UnaryFunctionBuilder.createBuilder("cosh").withFunction(NumberObject::cosh)
            .withDerivativeResolver(DERIVATIVE_COSH);
    /** hyperbolic tangent (tanh, th) */
    public static final UnaryFunctionBuilder TANH = UnaryFunctionBuilder.createBuilder("tanh").withFunction(NumberObject::tanh)
            .withDerivativeResolver(DERIVATIVE_TANH);

    // Basic logarithm functions
    /** natural logarithm function (ln) */
    public static final UnaryFunctionBuilder LN = UnaryFunctionBuilder.createBuilder("ln").withDomain(DOMAIN_LOG)
            .withFunction(NumberObject::log).withDerivativeResolver(DERIVATIVE_LN);
    /** common logarithm function (lg) */
    public static final UnaryFunctionBuilder LG = UnaryFunctionBuilder.createBuilder("lg").withDomain(DOMAIN_LOG)
            .withFunction(NumberObject::log10).withDerivativeResolver(DERIVATIVE_LG);

    // Basic power & root functions
    /** square root (sqrt) */
    public static final UnaryFunctionBuilder SQRT = UnaryFunctionBuilder.createBuilder("sqrt").withDomain(DOMAIN_SQRT)
            .withFunction(arg -> arg.power(StdNumberObject.PROVIDER.fromStdNumber(0.5))).withDerivativeResolver(DERIVATIVE_SQRT);
    /** cubic root (cbrt) */
    public static final UnaryFunctionBuilder CBRT = UnaryFunctionBuilder.createBuilder("cbrt")
            .withFunction(arg -> arg.power(StdNumberObject.PROVIDER.fromStdNumber(1 / 3.0))).withDerivativeResolver(DERIVATIVE_CBRT);
    /** e's power (exp) */
    public static final UnaryFunctionBuilder EXP = UnaryFunctionBuilder.createBuilder("exp")
            .withFunction(arg -> StdNumberObject.PROVIDER.fromStdNumber(Math.E).power(arg)).withDerivativeResolver(DERIVATIVE_EXP);

    // Common functions
    /** absolute value (abs or |x|) */
    public static final UnaryFunctionBuilder ABS = UnaryFunctionBuilder.createBuilder("abs").withFunction(NumberObject::abs)
            .withDerivativeResolver(DERIVATIVE_ABS);
    /** sign function (sgn) */
    public static final UnaryFunctionBuilder SGN = UnaryFunctionBuilder.createBuilder("sgn").withFunction(NumberObject::sgn);
    /** round-up function or "ceil" function (ceil) */
    public static final UnaryFunctionBuilder CEIL = UnaryFunctionBuilder.createBuilder("ceil")
            .withFunction(arg -> arg.isReal() ? StdNumberObject.PROVIDER.fromStdNumber(Math.ceil(arg.toStdNumber())) : arg);
    /** round-down function or "floor" function (floor) */
    public static final UnaryFunctionBuilder FLOOR = UnaryFunctionBuilder.createBuilder("floor")
            .withFunction(arg -> arg.isReal() ? StdNumberObject.PROVIDER.fromStdNumber(Math.floor(arg.toStdNumber())) : arg);
    /** rounding function (round) */
    public static final UnaryFunctionBuilder ROUND = UnaryFunctionBuilder.createBuilder("round")
            .withFunction(arg -> arg.isReal() ? StdNumberObject.PROVIDER.fromStdNumber(Math.round(arg.toStdNumber())) : arg);

    public static final UnaryFunctionBuilder FACTORIAL = UnaryFunctionBuilder.createBuilder("fact").withDomain(DOMAIN_NATURAL)
            .withFunction(arg -> StdNumberObject.PROVIDER.fromStdNumber(BaseFunctions.factorial(arg.toStdNumber())));

    // Binary functions
    /** mod function (mod) */
    public static final BinaryFunctionBuilder MOD = BinaryFunctionBuilder.createBuilder("mod")
            .withCalcFunction(((arg1, arg2) -> StdNumberObject.PROVIDER.fromStdNumber(arg1.toStdNumber() % arg2.toStdNumber())));

    public static final BinaryFunctionBuilder LOG = BinaryFunctionBuilder.createBuilder("log")
            .withCalcFunction(((arg1, arg2) -> arg1.log().divide(arg2.log())))
            .withDomainCheck1(DOMAIN_LOG).withDomainCheck2(DOMAIN_LOG).withDerivativeResolver(DERIVATIVE_LOG);
}
