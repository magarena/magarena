package magic.model.choice;

/**
 * Choice for "scry 1" action.
 */
public class MagicScryChoice extends MagicTopCardChoice {
    // translatable UI text (prefix with _S).
    private static final String _S1 = "Move this card from the top of the library to the bottom?";

    public MagicScryChoice() {
        super(_S1);
    }
}
