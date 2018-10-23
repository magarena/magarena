package magic.model.choice;

/**
 * Choice for "surveil 1" action.
 */
public class MagicSurveilChoice extends MagicTopCardChoice {
    // translatable UI text (prefix with _S).
    private static final String _S1 = "Move this card from the top of the library to your graveyard?";

    public MagicSurveilChoice() {
        super(_S1);
    }
}
