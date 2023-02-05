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

package io.github.nickid2018.smcl.number;

import io.github.nickid2018.smcl.util.MatrixFunctions;

public class MatrixObject extends NumberObject {

    private final NumberObject[][] matrix;

    public MatrixObject(NumberObject[][] matrix) {
        int col = matrix[0].length;
        for (NumberObject[] numberObjects : matrix)
            if (numberObjects.length != col)
                throw new IllegalArgumentException("The matrix is not a rectangle");
        this.matrix = matrix;
    }

    public static MatrixObject identity(int n, NumberProvider<?> provider) {
        NumberObject[][] matrix = new NumberObject[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                matrix[i][j] = i == j ? provider.getOne() : provider.getZero();
        return new MatrixObject(matrix);
    }

    @Override
    public MatrixObject add(NumberObject number) {
        if (number instanceof MatrixObject) {
            NumberObject[][] m = ((MatrixObject) number).matrix;
            if (m.length != matrix.length || m[0].length != matrix[0].length)
                throw new ArithmeticException("Matrix size mismatch");
            NumberObject[][] result = new NumberObject[matrix.length][matrix[0].length];
            for (int i = 0; i < matrix.length; i++)
                for (int j = 0; j < matrix[0].length; j++)
                    result[i][j] = matrix[i][j].add(m[i][j]);
            return new MatrixObject(result);
        } else {
            NumberObject[][] result = new NumberObject[matrix.length][matrix[0].length];
            for (int i = 0; i < matrix.length; i++)
                for (int j = 0; j < matrix[0].length; j++)
                    result[i][j] = matrix[i][j].add(number);
            return new MatrixObject(result);
        }
    }

    @Override
    public MatrixObject subtract(NumberObject number) {
        if (number instanceof MatrixObject) {
            NumberObject[][] m = ((MatrixObject) number).matrix;
            if (m.length != matrix.length || m[0].length != matrix[0].length)
                throw new ArithmeticException("Matrix size mismatch");
            NumberObject[][] result = new NumberObject[matrix.length][matrix[0].length];
            for (int i = 0; i < matrix.length; i++)
                for (int j = 0; j < matrix[0].length; j++)
                    result[i][j] = matrix[i][j].subtract(m[i][j]);
            return new MatrixObject(result);
        } else {
            NumberObject[][] result = new NumberObject[matrix.length][matrix[0].length];
            for (int i = 0; i < matrix.length; i++)
                for (int j = 0; j < matrix[0].length; j++)
                    result[i][j] = matrix[i][j].subtract(number);
            return new MatrixObject(result);
        }
    }

    @Override
    public MatrixObject multiply(NumberObject number) {
        if (number instanceof MatrixObject) {
            NumberObject[][] m = ((MatrixObject) number).matrix;
            if (matrix[0].length != m.length)
                throw new ArithmeticException("Matrix size mismatch");
            NumberObject[][] result = new NumberObject[matrix.length][m[0].length];
            for (int i = 0; i < matrix.length; i++)
                for (int j = 0; j < m[0].length; j++) {
                    result[i][j] = m[0][0].getProvider().fromStdNumber(0);
                    for (int k = 0; k < matrix[0].length; k++)
                        result[i][j] = result[i][j].add(matrix[i][k].multiply(m[k][j]));
                }
            return new MatrixObject(result);
        } else {
            NumberObject[][] result = new NumberObject[matrix.length][matrix[0].length];
            for (int i = 0; i < matrix.length; i++)
                for (int j = 0; j < matrix[0].length; j++)
                    result[i][j] = matrix[i][j].multiply(number);
            return new MatrixObject(result);
        }
    }

    @Override
    public NumberObject divide(NumberObject number) {
        if (number instanceof MatrixObject)
            throw new UnsupportedOperationException("Matrix division is not supported");
        NumberObject[][] result = new NumberObject[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[0].length; j++)
                result[i][j] = matrix[i][j].divide(number);
        return new MatrixObject(result);
    }

    @Override
    public MatrixObject power(NumberObject number) {
        if (!isSquare())
            throw new ArithmeticException("The matrix is not square");
        if (!(number instanceof LongConvertable) || !((LongConvertable) number).canConvertToLong())
            throw new UnsupportedOperationException("Matrix power with non-int is not supported");
        long n = ((LongConvertable) number).toLong();
        MatrixObject multiplier = this;
        if (n < 0) {
            multiplier = MatrixFunctions.invert(multiplier);
            n = -n;
        }
        MatrixObject result = identity(matrix.length, matrix[0][0].getProvider());
        while (n > 0) {
            if ((n & 1) == 1)
                result = result.multiply(multiplier);
            multiplier = multiplier.multiply(multiplier);
            n >>= 1;
        }
        return result;
    }

    @Override
    public MatrixObject negate() {
        NumberObject[][] result = new NumberObject[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[0].length; j++)
                result[i][j] = matrix[i][j].negate();
        return new MatrixObject(result);
    }

    @Override
    public NumberObject abs() {
        throw new UnsupportedOperationException("Matrix absolute value is not supported");
    }

    @Override
    public NumberObject sgn() {
        throw new UnsupportedOperationException("Matrix signum is not supported");
    }

    @Override
    public NumberObject sin() {
        throw new UnsupportedOperationException("Matrix sine is not supported");
    }

    @Override
    public NumberObject cos() {
        throw new UnsupportedOperationException("Matrix cosine is not supported");
    }

    @Override
    public NumberObject tan() {
        throw new UnsupportedOperationException("Matrix tangent is not supported");
    }

    @Override
    public NumberObject asin() {
        throw new UnsupportedOperationException("Matrix arcsine is not supported");
    }

    @Override
    public NumberObject acos() {
        throw new UnsupportedOperationException("Matrix arccosine is not supported");
    }

    @Override
    public NumberObject atan() {
        throw new UnsupportedOperationException("Matrix arctangent is not supported");
    }

    @Override
    public NumberObject sinh() {
        throw new UnsupportedOperationException("Matrix hyperbolic sine is not supported");
    }

    @Override
    public NumberObject cosh() {
        throw new UnsupportedOperationException("Matrix hyperbolic cosine is not supported");
    }

    @Override
    public NumberObject tanh() {
        throw new UnsupportedOperationException("Matrix hyperbolic tangent is not supported");
    }

    @Override
    public NumberObject log() {
        throw new UnsupportedOperationException("Matrix logarithm is not supported");
    }

    @Override
    public NumberObject log10() {
        throw new UnsupportedOperationException("Matrix logarithm base 10 is not supported");
    }

    @Override
    public NumberObject reciprocal() {
        throw new UnsupportedOperationException("Matrix reciprocal is not supported");
    }

    @Override
    public boolean isReal() {
        return false;
    }

    @Override
    public boolean isZero() {
        return false;
    }

    @Override
    public boolean isOne() {
        return false;
    }

    @Override
    public boolean isMinusOne() {
        return false;
    }

    @Override
    public double toStdNumber() {
        return 0;
    }

    @Override
    public String toPlainString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < matrix.length; i++) {
            sb.append('[');
            for (int j = 0; j < matrix[0].length; j++) {
                sb.append(matrix[i][j].toPlainString());
                if (j < matrix[0].length - 1)
                    sb.append(", ");
            }
            sb.append(']');
            if (i < matrix.length - 1)
                sb.append(", ");
        }
        sb.append(']');
        return sb.toString();
    }

    public NumberObject[][] getMatrix() {
        return matrix;
    }

    public boolean isSquare() {
        return matrix.length == matrix[0].length;
    }

    public int getRows() {
        return matrix.length;
    }

    public int getColumns() {
        return matrix[0].length;
    }

    @Override
    public NumberProvider<? extends NumberObject> getProvider() {
        return PROVIDER;
    }

    public static final NumberProvider<MatrixObject> PROVIDER = new NumberProvider<MatrixObject>() {
        @Override
        public MatrixObject fromStdNumber(double value) {
            return new MatrixObject(new NumberObject[][]{{StdNumberObject.PROVIDER.fromStdNumber(value)}});
        }

        @Override
        public MatrixObject fromStdNumberDivided(double dividend, double divisor) {
            return new MatrixObject(new NumberObject[][]{{StdNumberObject.PROVIDER.fromStdNumberDivided(dividend, divisor)}});
        }
    };
}
