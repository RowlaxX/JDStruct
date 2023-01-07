package dad.project.dstruct;

import java.util.Map;

import org.json.JSONObject;

public sealed interface ItemFactory permits ItemFactoryImpl {

	public static ItemFactory from(Structure structure) {
		return new ItemFactoryImpl(structure);
	}
	
	public Structure structure();
	public Item newItem();
	public Item parse(Map<String, Object> map);
	public Item parse(JSONObject json);
	public Item parse(byte[] data);
	
}
