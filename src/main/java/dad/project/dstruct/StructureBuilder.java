package dad.project.dstruct;

import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public final class StructureBuilder {
	
	public static StructureBuilder withId() {
		return new StructureBuilder()
				.enableId();
	}
	
	public static StructureBuilder withIdAndTimestamp() {
		return new StructureBuilder()
				.enableId()
				.enableTimestamp();
	}
	
	public static StructureBuilder withIdAndTimespan() {
		return new StructureBuilder()
				.enableId()
				.enableTimespan();
	}
	
	public static StructureBuilder withTimestamp() {
		return new StructureBuilder()
				.enableTimestamp();
	}
	
	public static StructureBuilder withTimespan() {
		return new StructureBuilder()
				.enableTimespan();
	}
	
	private final Map<String, MemberDescriptor> map = new LinkedHashMap<>();
	
	public Structure build() {
		return new StructureImpl(map.values());
	}
	
	private StructureBuilder put(MemberDescriptor member) {
		map.put(member.name(), member);
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public StructureBuilder put(String name, MemberType type, boolean nullable) {
		return put(new MemberDescriptorImpl(name, nullable, type));
	}
	
	@SuppressWarnings("unchecked")
	public StructureBuilder put(String name, MemberType type) {
		return put(new MemberDescriptorImpl(name, false, type));
	}
	
	public StructureBuilder enableId() {
		return put(Structure.ID, MemberTypes.LONG);
	}
	
	public StructureBuilder enableTimestamp() {
		return put(Structure.TIMESTAMP, MemberTypes.LONG);
	}
	
	public StructureBuilder enableTimespan() {
		put(Structure.STARTDATE, MemberTypes.LONG);
		put(Structure.ENDDATE, MemberTypes.LONG);
		return this;
	}
	
	public StructureBuilder putInt(String name, boolean nullable) {
		return put(name, MemberTypes.INT, nullable);
	}
	
	public StructureBuilder putInt(String name) {
		return put(name, MemberTypes.INT);
	}
	
	public StructureBuilder putByte(String name, boolean nullable) {
		return put(name, MemberTypes.BYTE, nullable);
	}
	
	public StructureBuilder putByte(String name) {
		return put(name, MemberTypes.BYTE);
	}
	
	public StructureBuilder putBoolean(String name, boolean nullable) {
		return put(name, MemberTypes.BOOLEAN, nullable);
	}
	
	public StructureBuilder putBoolean(String name) {
		return put(name, MemberTypes.BOOLEAN);
	}
	
	public StructureBuilder putShort(String name, boolean nullable) {
		return put(name, MemberTypes.SHORT, nullable);
	}
	
	public StructureBuilder putShort(String name) {
		return put(name, MemberTypes.SHORT);
	}
	
	public StructureBuilder putFloat(String name, boolean nullable) {
		return put(name, MemberTypes.FLOAT, nullable);
	}
	
	public StructureBuilder putFloat(String name) {
		return put(name, MemberTypes.FLOAT);
	}
	
	public StructureBuilder putLong(String name, boolean nullable) {
		return put(name, MemberTypes.LONG, nullable);
	}
	
	public StructureBuilder putLong(String name) {
		return put(name, MemberTypes.LONG);
	}
	
	public StructureBuilder putDouble(String name, boolean nullable) {
		return put(name, MemberTypes.DOUBLE, nullable);
	}
	
	public StructureBuilder putDouble(String name) {
		return put(name, MemberTypes.DOUBLE);
	}
	
	public StructureBuilder putArray(String name, int size, boolean nullable) {
		return put(name, MemberTypes.BYTEARRAY.withSize((short)size), nullable);
	}
	
	public StructureBuilder putArray(String name, int size) {
		return put(name, MemberTypes.BYTEARRAY.withSize((short)size));
	}
	
	public StructureBuilder putString(String name, int size, boolean nullable) {
		return put(name, MemberTypes.STRING.withSize((short)size), nullable);
	}
	
	public StructureBuilder putString(String name, int size) {
		return put(name, MemberTypes.STRING.withSize((short)size));
	}
}