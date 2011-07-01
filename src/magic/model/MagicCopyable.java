package magic.model;

public interface MagicCopyable {
	public MagicCopyable create();
	public void copy(final MagicCopyMap copyMap,final MagicCopyable source);
}
