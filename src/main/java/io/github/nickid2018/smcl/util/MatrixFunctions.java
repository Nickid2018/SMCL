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

package io.github.nickid2018.smcl.util;

import io.github.nickid2018.smcl.number.MatrixObject;
import io.github.nickid2018.smcl.number.NumberObject;
import io.github.nickid2018.smcl.number.NumberProvider;

public class MatrixFunctions {

    public static MatrixObject transpose(MatrixObject matrix) {
        int len1 = matrix.getRows();
        int len2 = matrix.getColumns();
        NumberObject[][] result = new NumberObject[len2][len1];
        for (int i = 0; i < len1; i++)
            for (int j = 0; j < len2; j++)
                result[j][i] = matrix.getMatrix()[i][j];
        return new MatrixObject(result);
    }

    private static NumberObject determinant(NumberObject[][] matrix) {
        if (matrix.length == 1)
            return matrix[0][0];
        if (matrix.length == 2)
            return matrix[0][0].multiply(matrix[1][1]).subtract(matrix[0][1].multiply(matrix[1][0]));
        NumberObject result = matrix[0][0].getProvider().getZero();
        for (int i = 0; i < matrix.length; i++) {
            NumberObject[][] m = new NumberObject[matrix.length - 1][matrix.length - 1];
            for (int j = 1; j < matrix.length; j++)
                for (int k = 0; k < matrix.length; k++)
                    if (k < i)
                        m[j - 1][k] = matrix[j][k];
                    else if (k > i)
                        m[j - 1][k - 1] = matrix[j][k];
            result = result.add(matrix[0][i].multiply(determinant(m)));
            if (i % 2 == 1)
                result = result.negate();
        }
        return result;
    }

    public static NumberObject determinant(MatrixObject matrix) {
        if (!matrix.isSquare())
            throw new ArithmeticException("The matrix is not a square");
        return determinant(matrix.getMatrix());
    }

    public static MatrixObject getMinor(MatrixObject matrix, int x, int y) {
        if (!matrix.isSquare())
            throw new ArithmeticException("The matrix is not a square");
        NumberObject[][] result = new NumberObject[matrix.getRows() - 1][matrix.getColumns() - 1];
        for (int i = 0; i < matrix.getRows(); i++)
            for (int j = 0; j < matrix.getColumns(); j++)
                if (i < x && j < y)
                    result[i][j] = matrix.getMatrix()[i][j];
                else if (i > x && j < y)
                    result[i - 1][j] = matrix.getMatrix()[i][j];
                else if (i < x && j > y)
                    result[i][j - 1] = matrix.getMatrix()[i][j];
                else if (i > x && j > y)
                    result[i - 1][j - 1] = matrix.getMatrix()[i][j];
        return new MatrixObject(result);
    }

    public static MatrixObject invert(MatrixObject matrix) {
        if (!matrix.isSquare())
            throw new ArithmeticException("The matrix is not a square");
        NumberObject det = determinant(matrix);
        if (det.isZero())
            throw new ArithmeticException("The matrix is not invertible");
        NumberObject[][] result = new NumberObject[matrix.getRows()][matrix.getColumns()];
        for (int i = 0; i < matrix.getRows(); i++)
            for (int j = 0; j < matrix.getColumns(); j++) {
                result[i][j] = determinant(getMinor(matrix, i, j));
                if ((i + j) % 2 == 1)
                    result[i][j] = result[i][j].negate();
            }
        return transpose(new MatrixObject(result)).multiply(det.reciprocal());
    }
}
