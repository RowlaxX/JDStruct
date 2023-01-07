package dad.project.dstruct;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * <p>
 * A structure define the way item is stored in the database.
 * An instance of this class is immutable
 * </p>
 * 
 * @see dad.project.dstruct.Item
 * @author Théo Linder
 */
public sealed interface Structure permits StructureImpl {

	public static Structure read(Kryo kryo, Input in) {
		try {
			return kryo.readObject(in, StructureImpl.class);	
		}catch(IllegalArgumentException e) {
			kryo.register(StructureImpl.class, new StructureSerializer());
			return kryo.readObject(in, StructureImpl.class);	
		}
	}
	
	public static void write(Kryo kryo, Output out, Structure struct) {
		try{
			kryo.writeObject(out, struct);
		}catch(IllegalArgumentException e) {
			kryo.register(StructureImpl.class, new StructureSerializer());
			kryo.writeObject(out, struct);
		}
	}
	
	static final String ID = "id";
	static final String TIMESTAMP = "timestamp";
	static final String STARTDATE = "startDate";
	static final String ENDDATE = "endDate";
	
	/**
	 * <p>
	 * Is id supported by items of this structure
	 * If id is supported, each items of this structure has a id property
	 * </p>
	 * 
	 * @return true if supported
	 */
	public boolean supportId();
	
	/**
	 * Is timestamp supported by items of this structure
	 * If timestamp is supported, each items of this structure has a timestamp property

	 * @return true if supported
	 */
	public boolean supportTimestamp();
	
	/**
	 * If timespan supported by items of this structure
	 * If timespan is supported, each items of this structure has a startDate and endDate properties
	 * 
	 * @return true if supported
	 */
	public boolean supportTimespan();
	
	/**
	 * <p>
	 * Return the members description of this structure.
	 * The members are offset sorted
	 * </p>
	 * 
	 * <p>
	 * The returned instance is immutable, even when casted, to guarantee immutability
	 * </p>
	 * @return the members
	 */
	public Iterable<Member<?>> members();
	
	/**
	 * Return the number of member present in that structure
	 * @return the count of members
	 */
	public short membersCount();
	
	/**
	 * @param member the name of the member to get
	 * @return the member with the associated name
	 */
	public <T> Member<T> member(String member);

	/**
	 * @param member the member to test
	 * @return true if the member is present in this structure
	 */
	public boolean isMemberPresent(Member<?> member);
	
	/**
	 * @param member the member to test
	 * @return true if the member is present in this structure
	 */
	public boolean isMemberPresent(String member);
	
	/**
	 * @return the id member
	 */
	public Member<Long> getId();
	/**
	 * @return the timestamp member
	 */
	public Member<Long> getTimestamp();
	
	/**
	 * @return the startDate member
	 */
	public Member<Long> getStartDate();
	
	/**
	 * @return the endDate member
	 */
	public Member<Long> getEndDate();
	
	/**
	 * @return the size of each item of this structure
	 */
	public int itemSize();
}
