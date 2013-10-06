package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicSoulbondAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicOtherPermanentTargetFilter;
import magic.model.target.MagicTargetHint;

public class MagicSoulbondEvent extends MagicEvent {

    public MagicSoulbondEvent(final MagicPermanent permanent,final boolean hasSoulbond) {
        super(
            permanent,
            new MagicMayChoice(
                hasSoulbond ?
                    new MagicTargetChoice(
                        new MagicOtherPermanentTargetFilter(
                            MagicTargetFilter.TARGET_UNPAIRED_CREATURE_YOU_CONTROL,
                            permanent
                        ),
                        MagicTargetHint.None,
                        "a creature other than " + permanent
                    ):
                    MagicTargetChoice.TARGET_UNPAIRED_SOULBOND_CREATURE
            ),
            EVENT_ACTION,
            hasSoulbond ?
                "PN may$ pair SN with an unpaired creature$ you control." :
                "PN may$ pair SN with an unpaired creature$ with soulbond you control."
        );
    }

    private static final MagicEventAction EVENT_ACTION = new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent creature) {
                        game.doAction(new MagicSoulbondAction(
                            event.getPermanent(),
                            creature,
                            true
                        ));
                    }
                });
            }
        }
    };
}
