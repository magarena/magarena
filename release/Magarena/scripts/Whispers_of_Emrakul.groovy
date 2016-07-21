[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_OPPONENT,
                this,
                "Target opponent\$ discards a card at random. "+
                "If there are four or more card types among cards in PN's graveyard, that player discards two cards at random instead."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final int amount = MagicCondition.DELIRIUM_CONDITION.accept(event.getSource()) ? 2 : 1;
                game.addEvent(MagicDiscardEvent.Random(event.getSource(), it, amount));
            });
        }
    }
]
