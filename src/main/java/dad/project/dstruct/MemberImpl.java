package dad.project.dstruct;

import java.util.Objects;

final record MemberImpl<T>(
		Structure structure,
		MemberDescriptor<T> descriptor,
		int index,
		int offset)
	implements Member<T> {
	
	MemberImpl {
		Objects.requireNonNull(descriptor);
		Objects.requireNonNull(structure);
	}

	@Override
	public int hashCode() {
		return Objects.hash(descriptor, index, offset);
	}

	@Override
	public String toString() {
		return "Member ["
				+ "name=" + descriptor.name() + ", "
				+ "isNullable=" + descriptor.isNullable() + ", "
				+ "type=" + descriptor.type() + ", "
				+ "index=" + index + ", "
				+ "size=" + size() + ", "
				+ "offset=" + offset + "]";
	}

	@Override
	public String name() {
		return descriptor.name();
	}

	@Override
	public boolean isNullable() {
		return descriptor.isNullable();
	}

	@Override
	public MemberType<T> type() {
		return descriptor.type();
	}
}
