package dad.project.dstruct;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import com.esotericsoftware.kryo.io.Input;

import dad.project.dstruct.exceptions.ItemException;
import dad.project.dstruct.exceptions.MemberNotPresentException;

final class ItemImpl implements Item {

	private final Structure structure;
	private final Object[] items;
	
	ItemImpl(Structure structure) {
		this.structure = Objects.requireNonNull(structure);
		this.items = new Object[structure.membersCount()];
		
		for (Member<?> member : structure.members())
			if (!member.isNullable())
				items[member.index()] = member.type().defaultValue();
	}
	
	ItemImpl(Structure structure, byte[] data) {
		this.structure = Objects.requireNonNull(structure);
		this.items = new Object[structure.membersCount()];
	
		if (data.length != structure.itemSize())
			throw new ItemException("data.length must be equals to structure length (" + structure.itemSize() + ")");
		
		try (final Input input = new Input(data)){
			input.setVariableLengthEncoding(false);

			for (Member<?> member : structure.members()) {
				input.setPosition(member.offset());
				items[member.index()] = member.read(input);
			}
		}
	}
	
	ItemImpl(Structure structure, Map<String, Object> map) {
		this.structure = Objects.requireNonNull(structure);
		this.items = new Object[structure.membersCount()];
		
		Object value;
		for (Member<?> member : structure.members()) {
			value = map.remove(member.name());
			if (value == null && !member.isNullable())
				value = member.type().defaultValue();
			set(member, value);
		}
		
		if (map.size() > 0) {
			final String[] array = new String[map.size()];
			map.keySet().toArray(array);
			throw new MemberNotPresentException(array);
		}
	}
	
	@Override
	public Structure structure() {
		return structure;
	}
	
	private void checkMemberPresent(Member<?> member) {
		if (!structure.isMemberPresent(member))
			throw new MemberNotPresentException();
	}
	
	private String msg(Member<?> member, String msg) {
		return "The member \"" + member.name() + "\" " + msg;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Member<T> member) {
		checkMemberPresent(member);
		
		return (T) items[member.index()];
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> void set(Member<T> member, Object item) {
		checkMemberPresent(member);
		
		if (item == null) {
			if (!member.isNullable())
				throw new ItemException(
						msg(member, "do not accepts null values"));
			items[member.index()] = null;
		}
		else {
			final MemberType<T> type = member.type();
			if (type.typeClass() != item.getClass())
				throw new ItemException(
						msg(member, "only accepts " + type.typeClass().getSimpleName() + " instances. Current is " + item.getClass().getSimpleName()));
			
			if (!type.isTypeFixedSize() && type.bodySize( (T) item) > type.bodySize())
				throw new ItemException(
						msg(member, "has a size limitation of " + type.bodySize() 
						+ " and given object is " + type.bodySize( (T) item) + " bytes"));
			items[member.index()] = item;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(items);
		result = prime * result + Objects.hash(structure);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemImpl other = (ItemImpl) obj;
		return Arrays.deepEquals(items, other.items) && Objects.equals(structure, other.structure);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(128);
		
		sb.append("Item [");
		
		for (Member<?> member : structure.members())
			sb	.append(member.name())
				.append('=')
				.append(get(member))
				.append(" ,");
		
		sb.delete(sb.length() - 2, sb.length());
		sb.append("]");
		return sb.toString();
	}
}