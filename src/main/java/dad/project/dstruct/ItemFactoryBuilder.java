package dad.project.dstruct;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import dad.project.dstruct.exceptions.MemberNotPresentException;

public final class ItemFactoryBuilder {

	public static ItemFactoryBuilder with(Structure structure) {
		return new ItemFactoryBuilder(structure);
	}
	
	public class Transform {
		private String[] keys;
		
		private Transform(String[] keys) {
			this.keys = Objects.requireNonNull(keys);
		}
		
		public ItemFactoryBuilder to(String member) {
			if (member != null)
				checkMember(member);
			return apply(member);
		}
		
		public ItemFactoryBuilder to(Member<?> member) {
			if (member != null) {
				checkMember(member);
				return apply(member.name());
			}
			return apply(null);
		}
		
		private ItemFactoryBuilder apply(String member) {
			for (String key : keys)
				transformer.put(key, member);
			keys = null;
			return ItemFactoryBuilder.this;
		}
	}
	
	private final Structure structure;
	private Set<String> mandatory = new HashSet<>();
	private Map<String, String> transformer = new HashMap<>();
	
	private ItemFactoryBuilder(Structure structure) {
		this.structure = Objects.requireNonNull(structure);
	}
	
	private void checkMember(String member) {
		if (!structure.isMemberPresent(member))
			throw new MemberNotPresentException(member);
	}
	
	private void checkMember(Member<?> member) {
		if (!structure.isMemberPresent(member))
			throw new MemberNotPresentException();
	}
	
	public ItemFactoryBuilder mandatory(String... members) {
		for (String member : members) {
			checkMember(member);
			mandatory.add(member);
		}
		return this;
	}
	
	public ItemFactoryBuilder mandatory(Member<?>... members) {
		for (Member<?> member : members) {
			checkMember(member);
			mandatory.add(member.name());
		}
		return this;
	}
	
	public Transform transform(String... keys) {
		return new Transform(keys);
	}
	
	public ItemFactoryBuilder skip(String... keys) {
		return transform(keys).to( (String) null);
	}
	
	public ItemFactory build() {
		mandatory = Collections.unmodifiableSet(mandatory);
		transformer = Collections.unmodifiableMap(transformer);
		return new ItemFactoryImpl(structure, mandatory, transformer);
	}
}
