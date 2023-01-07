package dad.project.dstruct;

import java.util.Arrays;
import java.util.Objects;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import dad.project.dstruct.exceptions.MemberException;

abstract non-sealed class MemberTypeSizeVariable<T> implements MemberType<T> {

	@SuppressWarnings("unchecked")
	static <E> MemberTypeSizeVariable<E> from(MemberTypes type, short size){
		return (MemberTypeSizeVariable<E>) switch (type) {
			case STRING -> new StringType(size);
			case BYTEARRAY -> new ByteArrayType(size);
			default -> throw new MemberException("Unexpected value: " + type);
		};
	}
	
	private static class StringType extends MemberTypeSizeVariable<String> {
		public StringType(short size) {
			super(MemberTypes.STRING, size);
		}

		@Override
		public String decode(byte[] arr, short size) {
			return new String(arr, 0, size);
		}

		@Override
		public short encode(Output out, String object) {
			final byte[] data = object.getBytes();
			if (object.length() >= size())
				throw new MemberException("the string is too long for this member");
			
			out.write(data);
			return (short) data.length;
		}
	}
	
	private static class ByteArrayType extends MemberTypeSizeVariable<byte[]> {
		public ByteArrayType(short size) {
			super(MemberTypes.BYTEARRAY, size);
		}

		public byte[] decode(byte[] arr, short size) {
			return Arrays.copyOf(arr, size);
		}
		
		public short encode(Output out, byte[] object) {
			if (object.length >= size())
				throw new MemberException("the array is too big for this member");
			
			out.write(object);
			return (short) object.length;
		}
	}
	
	private final MemberTypes type;
	private final short bodySize;
	
	private MemberTypeSizeVariable(MemberTypes type, short size) {
		this.type = Objects.requireNonNull(type);
		this.bodySize = size;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(bodySize, type);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MemberTypeSizeVariable other = (MemberTypeSizeVariable) obj;
		return bodySize == other.bodySize && type == other.type;
	}
	
	public MemberTypes getType() {
		return type;
	}

	@Override
	public String toString() {
		return type + "(" + bodySize + ")";
	}

	@Override
	public boolean isFixedSize() {
		return true;
	}
	
	@Override
	public boolean isTypeFixedSize() {
		return false;
	}

	@Override
	public short identifier() {
		return type.identifier();
	}

	@Override
	public int bodySize() {
		return bodySize;
	}
	
	@Override
	public int headerSize() {
		return type.headerSize();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<T> typeClass() {
		return type.typeClass();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T defaultValue() {
		return (T) type.defaultValue();
	}

	@Override
	public void write(Output out, T object) {
		final byte[] data = new byte[bodySize];
		final Output out2 = new Output(data);
		final short len = encode(out2, object);
		
		if (len > bodySize)
			throw new MemberException("The given lengtth is greater than the maximum body size");
		
		out.writeShort(len);
		out.write(data);
	}

	@Override
	public T read(Input in) {
		final short s = in.readShort();
		final byte[] data = in.readBytes(bodySize);
		
		if (s > bodySize)
			throw new MemberException("The size is greater than the maximum body size");
		
		return decode(data, s);
	}
	
	@Override
	public int bodySize(T object) {
		return type.bodySize(object);
	}
	
	public abstract T decode(byte[] arr, short size);
	public abstract short encode(Output out, T object);
}
