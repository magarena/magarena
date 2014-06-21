[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Green creatures PN controls get +1/+1 until end of turn. "+
                "Nongreen creatures PN controls get +1/+0 until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets = game.filterPermanents(event.getPlayer(),MagicTargetFilterFactory.CREATURE_YOU_CONTROL);
            for (final MagicPermanent creature : targets) {
                if (creature.hasColor(MagicColor.Green)) {
                    game.doAction(new MagicChangeTurnPTAction(creature,1,1));
                } else {
                    game.doAction(new MagicChangeTurnPTAction(creature,1,0));
                }
            }
        }
    }
]
