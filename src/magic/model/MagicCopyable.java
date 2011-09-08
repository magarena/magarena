package magic.model;

public interface MagicCopyable {
	MagicCopyable create();
	void copy(final MagicCopyMap copyMap,final MagicCopyable source);
}
