package dad.project.dstruct;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.esotericsoftware.kryo.DefaultSerializer;

import dad.project.dstruct.exceptions.MemberNotPresentException;
import dad.project.dstruct.exceptions.StructureException;

@DefaultSerializer(value = StructureSerializer.class)
final class StructureImpl implements Structure {
	
	private final List<Member<?>> members;
	private final Member<Long> mId;
	private final Member<Long> mTimestamp;
	private final Member<Long> mStartdate;
	private final Member<Long> mEnddate;
	private final int itemSize;
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	StructureImpl(Collection<MemberDescriptor> memberDescriptors) {
		final Map<String, MemberDescriptor> map = new LinkedHashMap<>(memberDescriptors.size());
		
		for (MemberDescriptor member : memberDescriptors) {
			if (map.containsKey(member.name()))
				throw new StructureException("duplicated member entry : " + member.name());
			map.put(member.name(), member);
		}
		
		final MemberDescriptor<Long> mdId = map.remove(ID);
		final MemberDescriptor<Long> mdTimestamp = map.remove(TIMESTAMP);
		final MemberDescriptor<Long> mdStartdate = map.remove(STARTDATE);
		final MemberDescriptor<Long> mdEnddate = map.remove(ENDDATE);
		
		if ( (mdStartdate == null) != (mdEnddate == null) )
			throw new StructureException("can't support partial timespan");
		if (mdId == null && mdTimestamp == null && mdStartdate == null)
			throw new StructureException("No special member is supported");
		if (mdTimestamp != null && mdStartdate != null)
			throw new StructureException("Can't support both timespan & timestamp");
		
		final List<Member<?>> mems = new ArrayList<>(map.size());
		final int[] indexAndOffset = new int[] {0, 0};

		this.mId = put(mems, mdId, indexAndOffset);
		this.mTimestamp = put(mems, mdTimestamp, indexAndOffset);
		this.mStartdate = put(mems, mdStartdate, indexAndOffset);
		this.mEnddate = put(mems, mdEnddate, indexAndOffset);
		
		for (MemberDescriptor remain : map.values())
			put(mems, remain, indexAndOffset);
		
		this.members = Collections.unmodifiableList(mems);
		this.itemSize = indexAndOffset[1];
	}
	
	private <T> Member<T> put(Collection<Member<?>> mems, MemberDescriptor<T> descriptor, int[] indexAndOffset) {
		if (descriptor == null)
			return null;
		
		final Member<T> member = new MemberImpl<>(this, descriptor, indexAndOffset[0], indexAndOffset[1]);
		mems.add(member);
		indexAndOffset[0]++;
		indexAndOffset[1] += member.size();
		return member;
	}
	
	@Override
	public boolean supportId() {
		return mId != null;
	}

	@Override
	public boolean supportTimestamp() {
		return mTimestamp != null;
	}

	@Override
	public boolean supportTimespan() {
		return mStartdate != null;
	}

	@Override
	public Iterable<Member<?>> members() {
		return members;
	}
	
	@Override
	public short membersCount() {
		return (short) members.size();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Member<T> member(String member) {
		for (Member<?> m : members)
			if (Objects.equals(m.name(), member))
				return (Member<T>) m;
		throw new MemberNotPresentException(member);
	}

	@Override
	public int itemSize() {
		return itemSize;
	}
	
	@Override
	public Member<Long> getEndDate() {
		if (mEnddate == null)
			throw new MemberNotPresentException(ENDDATE);
		return mEnddate;
	}
	
	@Override
	public Member<Long> getId() {
		if (mId == null)
			throw new MemberNotPresentException(ID);
		return mId;
	}
	
	@Override
	public Member<Long> getStartDate() {
		if (mStartdate == null)
			throw new MemberNotPresentException(STARTDATE);
		return mStartdate;
	}
	
	@Override
	public Member<Long> getTimestamp() {
		if (mTimestamp == null)
			throw new MemberNotPresentException(TIMESTAMP);
		return mTimestamp;
	}

	@Override
	public int hashCode() {
		return Objects.hash(itemSize, members);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StructureImpl other = (StructureImpl) obj;
		return itemSize == other.itemSize && Objects.equals(members, other.members);
	}
	
	@Override
	public boolean isMemberPresent(Member<?> member) {
		return equals(member.structure());
	}
	
	@Override
	public boolean isMemberPresent(String member) {
		for (Member<?> m : members)
			if (member.equals(m.name()))
				return true;
		return false;
	}

	@Override
	public String toString() {
		return "StructureImpl ["
				+ "members=" + members + ", "
				+ "itemSize=" + itemSize + ", "
				+ "supportId=" + supportId() + ", "
				+ "supportTimestamp=" + supportTimestamp() + ", "
				+ "supportTimespan=" + supportTimespan() + ", "
				+ "membersCount=" + membersCount() + "]";
	}
}