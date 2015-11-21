package magic.model.trigger;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.action.MagicCardAction;
import magic.model.action.ShiftCardAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicCMCCardFilter;
import magic.model.target.MagicGraveyardTargetPicker;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetFilterFactory;
import magic.model.target.MagicTargetHint;
import magic.model.target.Operator;

public class SoulshiftTrigger extends WhenSelfDiesTrigger {

    private final int cmc;

    public SoulshiftTrigger(final int cmc) {
        this.cmc = cmc;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent died) {
        final MagicTargetFilter<MagicCard> targetFilter =
            new MagicCMCCardFilter(
                MagicTargetFilterFactory.SPIRIT_CARD_FROM_GRAVEYARD,
                Operator.LESS_THAN_OR_EQUAL,
                cmc
            );
        final MagicTargetChoice targetChoice =
            new MagicTargetChoice(
                targetFilter,
                MagicTargetHint.None,
                "target Spirit card from your graveyard"
            );
        return new MagicEvent(
            permanent,
            new MagicMayChoice(targetChoice),
            MagicGraveyardTargetPicker.ReturnToHand,
            this,
            "PN may$ return target Spirit card$ with " +
            "converted mana cost " + cmc + " or less " +
            "from his or her graveyard to his or her hand."
        );
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        if (event.isYes()) {
            event.processTargetCard(game,new MagicCardAction() {
                public void doAction(final MagicCard card) {
                    game.doAction(new ShiftCardAction(
                        card,
                        MagicLocationType.Graveyard,
                        MagicLocationType.OwnersHand
                    ));
                }
            });
        }
    }
}
