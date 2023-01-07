package dad.project.dstruct;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import dad.project.dstruct.exceptions.MemberException;

@SuppressWarnings("rawtypes")
public enum MemberTypes implements MemberType {
	
	BYTE(Byte.class, (byte)0, 1,
			Output::writeByte,
			Input::readByte),
	BOOLEAN(Boolean.class, false, 1,
			Output::writeBoolean,
			Input::readBoolean),
	SHORT(Short.class, (short)0, 2,
			(out, obj) -> out.writeShort(obj),
			Input::readShort),
	CHAR(Character.class, ' ', 2,
			Output::writeChar,
			Input::readChar),
	INT(Integer.class, 0, 4,
			Output::writeInt,
			Input::readInt),
	FLOAT(Float.class, 0f, 4,
			Output::writeFloat,
			Input::readFloat),
	LONG(Long.class, 0l, 8,
			Output::writeLong,
			Input::readLong),
	DOUBLE(Double.class, 0d, 8,
			Output::writeDouble,
			Input::readDouble),
	STRING(String.class, "",
			String::length),
	BYTEARRAY(byte[].class, new byte[0],
			b -> b.length);
	
	private static final MemberTypes[] VALUES = values();
	public static MemberTypes from(short identifier) {
		return VALUES[identifier];
	}
	
	private final Class<?> clazz;
	private final Object defaultValue;
	private final int size;
	
	private final BiConsumer<Output, Object> writer;
	private final Function<Input, Object> reader;
	private final Function<Object, Integer> mesurer;
	
	@SuppressWarnings("unchecked")
	private <T> MemberTypes(Class<T> clazz, T defaultValue, int size, 
			BiConsumer<Output, T> writer,
			Function<Input, T> reader) {
		this.clazz = Objects.requireNonNull(clazz);
		this.defaultValue = Objects.requireNonNull(defaultValue);
		this.size = size;
		this.writer = (BiConsumer<Output, Object>) writer;
		this.reader = (Function<Input, Object>) reader;
		this.mesurer = null;
	}
	
	@SuppressWarnings("unchecked")
	private <T> MemberTypes(Class<T> clazz, T defaultValue, Function<T, Integer> mesurer) {
		this.clazz = Objects.requireNonNull(clazz);
		this.defaultValue = Objects.requireNonNull(defaultValue);
		this.size = -1;
		this.writer = null;
		this.reader = null;
		this.mesurer = (Function<Object, Integer>) mesurer;
	}

	@Override
	public int bodySize() {
		if (!isFixedSize())
			throw new MemberException("This type is not size fixed");
		return size;
	}
	
	@Override
	public int headerSize() {
		return isFixedSize() ? 0 : 2;
	}
	
	public <T> MemberType<T> withSize(short size) {
		if (isFixedSize())
			throw new MemberException("This type is size fixed");
		return MemberTypeSizeVariable.from(this, size);
	}
	
	@Override
	public int bodySize(Object object) {
		if (isFixedSize())
			return size;
		return mesurer.apply(object);
	}

	@Override
	public Class typeClass() {
		return clazz;
	}

	@Override
	public Object defaultValue() {
		return defaultValue;
	}

	@Override
	public void write(Output out, Object object) {
		writer.accept(out, object);
	}

	@Override
	public Object read(Input in) {
		return reader.apply(in);
	}
	
	@Override
	public boolean isFixedSize() {
		return size >= 0;
	}
	
	@Override
	public boolean isTypeFixedSize() {
		return size >= 0;
	}
	
	@Override
	public short identifier() {
		return (short) ordinal();
	}
}
