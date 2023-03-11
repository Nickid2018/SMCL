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

import io.github.nickid2018.smcl.MetaArithmeticException;
import io.github.nickid2018.smcl.util.MatrixFunctions;

public class MatrixObject extends NumberObject {

    private final NumberObject[][] matrix;

    public MatrixObject(NumberObject[][] matrix) {
        int col = matrix[0].length;
        for (NumberObject[] numberObjects : matrix)
            if (numberObjects.length != col)
                throw new MetaArithmeticException("smcl.compute.matrix.not_rectangle");
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
                throw new MetaArithmeticException("smcl.compute.matrix.size_mismatch",
                        m.length, m[0].length, matrix.length, matrix[0].length);
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
                throw new MetaArithmeticException("smcl.compute.matrix.size_mismatch",
                        m.length, m[0].length, matrix.length, matrix[0].length);
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
                throw new MetaArithmeticException("smcl.compute.matrix.invalid_multiply",
                        m.length, m[0].length, matrix.length, matrix[0].length);
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
            return multiply(MatrixFunctions.invert((MatrixObject) number));
        NumberObject[][] result = new NumberObject[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[0].length; j++)
                result[i][j] = matrix[i][j].divide(number);
        return new MatrixObject(result);
    }

    @Override
    public MatrixObject power(NumberObject number) {
        if (!isSquare())
            throw new MetaArithmeticException("smcl.compute.matrix.not_square");
        if (!(number instanceof LongConvertable) || !((LongConvertable) number).canConvertToLong())
            throw new MetaArithmeticException("smcl.compute.matrix.power_fraction");
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
        NumberObject[][] result = new NumberObject[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[0].length; j++)
                result[i][j] = matrix[i][j].abs();
        return new MatrixObject(result);
    }

    @Override
    public NumberObject sgn() {
        NumberObject[][] result = new NumberObject[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[0].length; j++)
                result[i][j] = matrix[i][j].sgn();
        return new MatrixObject(result);
    }

    @Override
    public NumberObject sin() {
        NumberObject[][] result = new NumberObject[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[0].length; j++)
                result[i][j] = matrix[i][j].sin();
        return new MatrixObject(result);
    }

    @Override
    public NumberObject cos() {
        NumberObject[][] result = new NumberObject[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[0].length; j++)
                result[i][j] = matrix[i][j].cos();
        return new MatrixObject(result);
    }

    @Override
    public NumberObject tan() {
        NumberObject[][] result = new NumberObject[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[0].length; j++)
                result[i][j] = matrix[i][j].tan();
        return new MatrixObject(result);
    }

    @Override
    public NumberObject asin() {
        NumberObject[][] result = new NumberObject[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[0].length; j++)
                result[i][j] = matrix[i][j].asin();
        return new MatrixObject(result);
    }

    @Override
    public NumberObject acos() {
        NumberObject[][] result = new NumberObject[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[0].length; j++)
                result[i][j] = matrix[i][j].acos();
        return new MatrixObject(result);
    }

    @Override
    public NumberObject atan() {
        NumberObject[][] result = new NumberObject[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[0].length; j++)
                result[i][j] = matrix[i][j].atan();
        return new MatrixObject(result);
    }

    @Override
    public NumberObject sinh() {
        NumberObject[][] result = new NumberObject[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[0].length; j++)
                result[i][j] = matrix[i][j].sinh();
        return new MatrixObject(result);
    }

    @Override
    public NumberObject cosh() {
        NumberObject[][] result = new NumberObject[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[0].length; j++)
                result[i][j] = matrix[i][j].cosh();
        return new MatrixObject(result);
    }

    @Override
    public NumberObject tanh() {
        NumberObject[][] result = new NumberObject[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[0].length; j++)
                result[i][j] = matrix[i][j].tanh();
        return new MatrixObject(result);
    }

    @Override
    public NumberObject log() {
        NumberObject[][] result = new NumberObject[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[0].length; j++)
                result[i][j] = matrix[i][j].log();
        return new MatrixObject(result);
    }

    @Override
    public NumberObject log10() {
        NumberObject[][] result = new NumberObject[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[0].length; j++)
                result[i][j] = matrix[i][j].log10();
        return new MatrixObject(result);
    }

    @Override
    public NumberObject reciprocal() {
        NumberObject[][] result = new NumberObject[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[0].length; j++)
                result[i][j] = matrix[i][j].reciprocal();
        return new MatrixObject(result);
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

        @Override
        public MatrixObject fromString(String value) {
            // Unsupported now
            return null;
        }
    };
}
