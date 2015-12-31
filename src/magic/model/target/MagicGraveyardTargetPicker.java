package magic.model.target;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPlayer;

public class MagicGraveyardTargetPicker extends MagicTargetPicker<MagicCard> {

    private final boolean free;
    private int order;

    public static MagicGraveyardTargetPicker ExileOwn = new MagicGraveyardTargetPicker(true, -1);

    public static MagicGraveyardTargetPicker ExileOpp = new MagicGraveyardTargetPicker(true, 1);

    public static MagicGraveyardTargetPicker ReturnToHand = new MagicGraveyardTargetPicker(false, 1);

    public static MagicGraveyardTargetPicker PutOntoBattlefield = new MagicGraveyardTargetPicker(true, 1);

    private MagicGraveyardTargetPicker(final boolean aFree, final int aOrder) {
        free = aFree;
        order = aOrder;
    }

    @Override
    protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicCard target) {
        return order *
            (free ? ArtificialScoringSystem.getFreeCardScore(target):
                    ArtificialScoringSystem.getCardScore(target));
    }
}
