package dad.project.dstruct;

import java.util.Objects;

import dad.project.dstruct.exceptions.MemberException;

record MemberDescriptorImpl<T>(
		String name, 
		boolean isNullable, 
		MemberType<T> type) 
	implements MemberDescriptor<T> {
	
	MemberDescriptorImpl {
		Objects.requireNonNull(name, "name may not be null");
		Objects.requireNonNull(type, "type may not be null");
		
		if (MemberDescriptor.isSpecial(name)) {
			if (isNullable)
				throw new MemberException("the special member \"" + name + "\" can't be nullable");
			else if (type != MemberTypes.LONG)
				throw new MemberException("the special member \"" + name + "\" need to be long");
		}
	}
	
	@Override
	public String toString() {
		return "MemberDescriptor ["
				+ "name=" + name + ", "
				+ "isNullable=" + isNullable + ", "
				+ "size=" + size() + ", "
				+ "type=" + type + "]";
	}
}
