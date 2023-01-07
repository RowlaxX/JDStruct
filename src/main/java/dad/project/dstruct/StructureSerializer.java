package dad.project.dstruct;

import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

final class StructureSerializer extends Serializer<StructureImpl> {
	StructureSerializer() {}
	
	private static final int IDENTIFIER = 0x636DA2CE;
	private static final short VERSION = 0;
	
	@Override
	public void write(Kryo kryo, Output output, StructureImpl object) {
		output.writeInt(IDENTIFIER);
		output.writeShort(VERSION);
		output.writeInt(object.itemSize());
		output.writeBoolean(object.supportId());
		output.writeBoolean(object.supportTimestamp());
		output.writeBoolean(object.supportTimespan());
		output.writeShort(object.membersCount());
		for (Member<?> m : object.members())
			writeMemberDescriptor(output, m);
	}
	
	private static void writeMemberDescriptor(Output output, MemberDescriptor<?> mdi) {
		writeMemberType(output, mdi.type());
		output.writeBoolean(mdi.isNullable());
		output.writeString(mdi.name());
	}
	
	private static void writeMemberType(Output output, MemberType<?> mt) {
		if (mt instanceof MemberTypes i)
			output.writeShort(i.identifier());
		else if (mt instanceof MemberTypeSizeVariable<?> i) {
			output.writeShort(i.getType().identifier());
			output.writeShort(i.bodySize());
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	

	@SuppressWarnings("rawtypes")
	@Override
	public StructureImpl read(Kryo kryo, Input input, Class<? extends StructureImpl> type) {
		if (input.readInt() != IDENTIFIER)
			throw new KryoException("Bad identifier");
		if (input.readShort() != VERSION)
			throw new KryoException("Bad version");
		
		final int itemSize = input.readInt();
		final boolean idSupported = input.readBoolean();
		final boolean timestampSupported = input.readBoolean();
		final boolean timespanSupported = input.readBoolean();
		final int membersCount = input.readShort();
		
		final List<MemberDescriptor> members = new ArrayList<>(membersCount);
		
		for (int i = 0 ; i < membersCount ; i++)
			members.add(readMemberDescriptor(input));

		final StructureImpl structure = new StructureImpl(members);	

		if (	structure.itemSize() != itemSize ||
				structure.membersCount() != membersCount ||
				structure.supportId() != idSupported ||
				structure.supportTimespan() != timespanSupported ||
				structure.supportTimestamp() != timestampSupported )
			throw new IllegalStateException("Corrupted structure");
		
		return structure;
	}
	
	private static MemberDescriptor<?> readMemberDescriptor(Input input) {
		final MemberType<?> type = readMemberType(input);
		final boolean nullable = input.readBoolean();
		final String name = input.readString();
		return new MemberDescriptorImpl<>(name, nullable, type);
	}
	
	private static MemberType<?> readMemberType(Input input) {
		final MemberTypes mt = MemberTypes.from(input.readShort());
		if (mt.isFixedSize())
			return mt;
		final short bodySize = input.readShort();
		return MemberTypeSizeVariable.from(mt, bodySize);
	}
}
