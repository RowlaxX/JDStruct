package dad.project.dstruct;

import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.kryo.io.Output;

import org.json.JSONObject;

public sealed interface Item permits ItemImpl {

	public Structure structure();
	public <T> T get(Member<T> member);
	public <T> void set(Member<T> member, Object item);
		
	default byte[] toBytes() {
		final byte[] data = new byte[structure().itemSize()];
		
		try (final Output out = new Output(data)){
			out.setVariableLengthEncoding(false);
			for (Member<?> member : structure().members())
				put(out, member, this);
		}
		
		return data;
	}
	
	private static <T> void put(Output out, Member<T> member, Item item) {
		out.setPosition(member.offset());
		member.write(out, item.get(member));
	}
	
	default Map<String, Object> toMap() {
		final Map<String, Object> map = new HashMap<>(structure().membersCount());
		
		for (Member<?> member : structure().members())
			map.put(member.name(), get(member));
		
		return map;
	}
	
	default JSONObject toJson() {
		return new JSONObject(toMap());
	}
	
	default <T> void set(String member, T item) {
		set(structure().member(member), item);
	}
	
	default <T> T get(String member) {
		return get(structure().member(member));
	}
	
	default long getId() {
		return get(structure().getId());
	}
	
	default long getTimestamp() {
		return get(structure().getTimestamp());
	}
	
	default long getStartDate() {
		return get(structure().getStartDate());
	}
	
	default long getEndDate() {
		return get(structure().getEndDate());
	}
}
