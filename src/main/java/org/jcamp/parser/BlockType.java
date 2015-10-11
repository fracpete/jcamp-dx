package org.jcamp.parser;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BlockType implements Serializable {
	public final static BlockType ASSIGNMENT = new BlockType(4, "assignment");
	public final static BlockType FULLSPECTRUM = new BlockType(2, "full spectrum");
	public final static BlockType LINK = new BlockType(0, "link");

	public final static BlockType PEAKTABLE = new BlockType(3, "peak table");

	private static final long serialVersionUID = -8081269600789693382L;

	public final static BlockType STRUCTURE = new BlockType(1, "structure");

	private final static BlockType[] TYPES = new BlockType[] { LINK, STRUCTURE,
			FULLSPECTRUM, PEAKTABLE, ASSIGNMENT };

	private final static List<BlockType> TYPES_LIST = Collections
			.unmodifiableList(Arrays.asList(TYPES));

	private final String key;

	private final int ordinal;

	private BlockType(int ordinal, String key) {
		this.ordinal = ordinal;
		this.key = key;
	}

	@Override
	public final boolean equals(Object obj) {
		if (obj instanceof BlockType && ((BlockType) obj) == this)
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

	public Collection<BlockType> types() {
		return TYPES_LIST;
	}
}
