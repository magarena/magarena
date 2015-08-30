[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "Return each creature PN's opponents control with toughness X or less to its owner's hand, "+
                "where X is the number of Islands PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = player.getNrOfPermanents(MagicSubType.Island);
            game.logAppendX(player,amount);
            for (final MagicPermanent creature : CREATURE_YOU_CONTROL.filter(player.getOpponent())) {
                if (creature.getToughness() <= amount) {
                    game.doAction(new RemoveFromPlayAction(creature, MagicLocationType.OwnersHand));
                }
            }
        }
    }
]
