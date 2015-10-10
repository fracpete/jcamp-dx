package org.jcamp.parser;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Type implements Serializable {
	public final static Type ASSIGNMENT = new Type(4, "assignment");
	public final static Type FULLSPECTRUM = new Type(2, "full spectrum");
	public final static Type LINK = new Type(0, "link");

	public final static Type PEAKTABLE = new Type(3, "peak table");

	private static final long serialVersionUID = -8081269600789693382L;

	public final static Type STRUCTURE = new Type(1, "structure");

	private final static Type[] TYPES = new Type[] { LINK, STRUCTURE,
			FULLSPECTRUM, PEAKTABLE, ASSIGNMENT };

	private final static List<Type> TYPES_LIST = Collections
			.unmodifiableList(Arrays.asList(TYPES));

	private final String key;

	private final int ordinal;

	private Type(int ordinal, String key) {
		this.ordinal = ordinal;
		this.key = key;
	}

	@Override
	public final boolean equals(Object obj) {
		if (obj instanceof Type && ((Type) obj) == this)
			return true;
		return false;
	}

	@Override
	public final int hashCode() {
		return ordinal;
	}

	private Object readResolve() throws java.io.ObjectStreamException {
		return TYPES[ordinal];
	}

	@Override
	public String toString() {
		return key;
	}

	public Collection<Type> types() {
		return TYPES_LIST;
	}
}
