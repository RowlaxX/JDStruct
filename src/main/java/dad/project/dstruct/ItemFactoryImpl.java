package dad.project.dstruct;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.json.JSONObject;

import dad.project.dstruct.exceptions.ItemException;

final class ItemFactoryImpl implements ItemFactory {

	private final Set<String> mandatory;
	private final Structure structure;
	private final Map<String, String> transformer;

	ItemFactoryImpl(Structure structure) {
		this.structure = Objects.requireNonNull(structure);
		this.mandatory = Collections.emptySet();
		this.transformer = Collections.emptyMap();
	}
	
	ItemFactoryImpl(Structure structure, Set<String> mandatory, Map<String, String> transformer) {
		this.structure = Objects.requireNonNull(structure);
		this.mandatory = Objects.requireNonNull(mandatory);
		this.transformer = Objects.requireNonNull(transformer);
	}
	
	@Override
	public Structure structure() {
		return structure;
	}

	@Override
	public Item newItem() {
		return new ItemImpl(structure);
	}

	@Override
	public Item parse(Map<String, Object> map) {
		final Map<String, Object> processed = new HashMap<>(map.size());
		
		map.forEach( (key, value) -> {
			if (!transformer.containsKey(key)) {
				processed.put(key, value);
				return;
			}
			
			final String transformTo = transformer.get(key);
			if (transformTo == null)
				return;
			
			if (processed.put(transformTo, value) != null)
				throw new ItemException("Duplicated entry : " + transformTo);
		});
		
		for (String val : mandatory)
			if (!processed.containsKey(val))
				throw new ItemException("The member " + val + " is mandatory and was not found");
				
		return new ItemImpl(structure, processed);
	}

	@Override
	public Item parse(JSONObject json) {
		return parse(json.toMap());
	}

	@Override
	public Item parse(byte[] data) {
		return new ItemImpl(structure, data);
	}
	
}
