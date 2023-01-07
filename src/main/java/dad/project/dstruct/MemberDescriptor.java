package dad.project.dstruct;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * A member descriptor is a sharable instance of member.
 * It contains methods and data unrelated to the structure
 * @param <T> The type of the member
 */
sealed interface MemberDescriptor<T> permits MemberDescriptorImpl<T>, Member<T> {
	
	/**
	 * @param name the name to check
	 * @return true if this member correspond to a special member (id, timestamp, start or end date)
	 */
	public static boolean isSpecial(String name) {
		return 	Structure.ID.equals(name) ||
				Structure.TIMESTAMP.equals(name) ||
				Structure.STARTDATE.equals(name) || 
				Structure.ENDDATE.equals(name);
	}
	
	/**
	 * @return the name of this member
	 */
	public String name();
	
	default T read(Input in) {
		if (isNullable() && !in.readBoolean()) {
			in.skip(type().size());
			return null;
		}
		
		return type().read(in);
	}
	
	default void write(Output out, T item) {
		if (isNullable()) {
			out.writeBoolean(item != null);
			if (item == null) {
				out.setPosition(out.position() + type().size());
				return;
			}
		}
		
		type().write(out, item);
	}
	
	/**
	 * @return true if this member is nullable
	 */
	public boolean isNullable();
	
	/**
	 * @return the type of the member
	 */
	public MemberType<T> type();
	
	/**
	 * Get the number of bytes needed to store each item of this member
	 * @return the size of this member (in bytes)
	 */
	default int size() {
		if (isNullable())
			return type().size() + 1;
		return type().size();
	}
	
	/**
	 * @return true if this member correspond to the id member
	 */
	default boolean isId() {
		return Structure.ID.equals(name());
	}
	
	/**
	 * @return true if this member correspond to the timestamp member
	 */
	default boolean isTimestamp() {
		return Structure.TIMESTAMP.equals(name());
	}
	
	/**
	 * @return true if this member correspond to the startDate member
	 */
	default boolean isStartDate() {
		return Structure.STARTDATE.equals(name());
	}
	
	/**
	 * @return true if this member correspond to the endDate member
	 */
	default boolean isEndDate() {
		return Structure.ENDDATE.equals(name());
	}
	
	/**
	 * @return true if this member correspond to a special member (id, timestamp, start or end date)
	 */
	default boolean isSpecial() {
		return isSpecial(name());
	}
}
