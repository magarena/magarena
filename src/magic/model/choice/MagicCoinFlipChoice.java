package magic.model.choice;

public class MagicCoinFlipChoice extends MagicOrChoice {

    // translatable UI text (prefix with _S).
    private static final String _S1 = "Choose Heads or Tails";

    private static final MagicCoinFlipChoice INSTANCE = new MagicCoinFlipChoice();

    private MagicCoinFlipChoice() {
        super(_S1, MagicChoice.NONE, MagicChoice.NONE);
    }

    public static final MagicCoinFlipChoice create() {
        return INSTANCE;
    }
}
