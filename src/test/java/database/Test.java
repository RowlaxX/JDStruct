package database;

import org.json.JSONObject;

import dad.project.dstruct.Item;
import dad.project.dstruct.ItemFactory;
import dad.project.dstruct.ItemFactoryBuilder;
import dad.project.dstruct.Structure;
import dad.project.dstruct.StructureBuilder;

public class Test {

	public static void main(String[] args) {
				
		final Structure struct = StructureBuilder.withIdAndTimestamp()
				.putDouble("quantity")
				.putBoolean("isBuyerMaker")
				.putString("msg", 10, true)
				.putLong("nu")
				.build();

		final JSONObject json = new JSONObject();
		json.put("message", "coucou");
		json.put("id", 0l);
		json.put("timestamp", 0l);
		json.put("heyy", "error");
		json.put("salam", false);
		
		final ItemFactory factory = ItemFactoryBuilder.with(struct)
				.skip("heyy", "salam")
				.transform("message", "messages").to("msg")
				.mandatory("id", "timestamp", "msg")
				.build();
		
		final Item item = factory.parse(json);
		
		System.out.println(item);

	}

}
