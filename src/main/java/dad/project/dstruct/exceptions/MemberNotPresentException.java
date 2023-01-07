package dad.project.dstruct.exceptions;

public class MemberNotPresentException extends MemberException {
	private static final long serialVersionUID = 7752994019479638463L;
	
	public MemberNotPresentException() {
		super("Member not present in the structure");
	}
	
	public MemberNotPresentException(String[] members) {
		super(msg(members));
	}
	
	public MemberNotPresentException(String member) {
		super(msg(member));
	}
	
	private static String msg(String... members) {
		final StringBuilder sb = new StringBuilder(members.length > 1 ? "Members" : "Member");
		
		sb.append(' ');
		if (members.length > 1) {
			sb.append(" [");
			for (String s : members)
				sb.append(s).append(", ");
			
			sb.delete(sb.length() - 2, sb.length());
			sb.append("] are");
		}
		else
			sb.append(members[0]).append(" is");
		
		sb.append(" not present in the structure");
		
		return sb.toString();
	}
	
}
