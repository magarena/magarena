[
    new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer eotPlayer) {
            final Collection<MagicPermanent> targets =
                    game.filterPermanents(permanent.getController(),MagicTargetFilterFactory.CREATURE);
            return (targets.size() == 0) ?
                MagicRuleEventAction.create(permanent, "Sacrifice SN."):
                MagicEvent.NONE;
        }
    }
]
