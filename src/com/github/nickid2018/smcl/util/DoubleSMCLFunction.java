package com.github.nickid2018.smcl.util;

import java.util.*;
import com.github.nickid2018.smcl.*;

@FunctionalInterface
public interface DoubleSMCLFunction {

	public double accept(double arg, SMCL smcl);

	public default DoubleSMCLFunction addThen(DoubleSMCLFunction after) {
		Objects.requireNonNull(after);
		return (arg, smcl) -> after.accept(accept(arg, smcl), smcl);
	}
}
