package magic.model.action;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPlayer;

import java.util.List;

public class MagicManifestAction extends MagicAction {

    private final MagicPlayer player;
    private final int amount;
    private final MagicCardList milledCards = new MagicCardList();

    public MagicManifestAction(final MagicPlayer aPlayer,final int aAmount) {
        player = aPlayer;
        amount = aAmount;
    }

    @Override
    public void doAction(final MagicGame game) {
    }

    @Override
    public void undoAction(final MagicGame game) {
    
    }
}
