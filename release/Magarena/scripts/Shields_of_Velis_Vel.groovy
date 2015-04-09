[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_PLAYER,
                this,
                "Creatures PN controls get +0/+1 and gains all creature types until the end of the turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final Collection<MagicPermanent> targets = it.filterPermanents(CREATURE_YOU_CONTROL);
                for (final MagicPermanent creature : targets) {
                    game.doAction(new MagicChangeTurnPTAction(creature,0,1));
                    game.doAction(new MagicAddStaticAction(creature,MagicStatic.AllCreatureTypesUntilEOT));
                }
            });
        }
    }
]
