def EFFECT = MagicRuleEventAction.create("Sacrifice SN.");

[
    new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer eotPlayer) {
            final int amt = game.filterPermanents(permanent.getController(),MagicTargetFilterFactory.CREATURE).size();
            return amt == 0 ?
                EFFECT.getEvent(permanent) :
                MagicEvent.NONE;
        }
    }
]
