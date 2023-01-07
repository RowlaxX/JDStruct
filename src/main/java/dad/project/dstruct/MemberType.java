package dad.project.dstruct;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public sealed interface MemberType<T> permits MemberTypeSizeVariable<T>, MemberTypes  {

	public short identifier();
	public boolean isFixedSize();
	public boolean isTypeFixedSize();
	public int bodySize();
	public int bodySize(T object);
	public int headerSize();
	
	default int size() {
		return headerSize() + bodySize();
	}
	
	public Class<T> typeClass();
	public T defaultValue();
	
	public void write(Output out, T object);
	public T read(Input in);
}
