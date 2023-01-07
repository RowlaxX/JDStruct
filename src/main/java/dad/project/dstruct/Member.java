package dad.project.dstruct;

/**
 * A member represent a field of a structure
 * @param <T>
 */
public sealed interface Member<T> extends MemberDescriptor<T> permits MemberImpl<T> {

	/**
	 * @return the offset of this member in each item
	 */
	public int offset();
	
	/**
	 * @return the index of this member in the structure
	 */
	public int index();
	
	/**
	 * @return the corresponding structure
	 */
	public Structure structure();
}
