package magic.model.choice;

public class MagicCoinFlipChoice extends MagicOrChoice {

    private static final MagicCoinFlipChoice INSTANCE = new MagicCoinFlipChoice();

    private MagicCoinFlipChoice() {
        super("Choose Heads or Tails",MagicChoice.NONE, MagicChoice.NONE);
    }

    public static final MagicCoinFlipChoice create() {
        return INSTANCE;
    }
}
