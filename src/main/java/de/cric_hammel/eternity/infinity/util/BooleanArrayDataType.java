package de.cric_hammel.eternity.infinity.util;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

public class BooleanArrayDataType implements PersistentDataType<byte[], boolean[]> {

	@Override
	public Class<byte[]> getPrimitiveType() {
		return byte[].class;
	}

	@Override
	public Class<boolean[]> getComplexType() {
		return boolean[].class;
	}

	@Override
	public byte[] toPrimitive(boolean[] complex, PersistentDataAdapterContext context) {
		byte[] primitive = new byte[complex.length];
        for (int i = 0; i < complex.length; i++) {
            primitive[i] = (byte) (complex[i] ? 1 : 0);
        }
        return primitive;
	}

	@Override
	public boolean[] fromPrimitive(byte[] primitive, PersistentDataAdapterContext context) {
		boolean[] complex = new boolean[primitive.length];
        for (int i = 0; i < primitive.length; i++) {
            complex[i] = primitive[i] != 0;
        }
        return complex;
	}

}
