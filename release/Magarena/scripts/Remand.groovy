[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_SPELL,
                this,
                "Counter target spell\$. If that spell is countered this way, "+
                "put it into its owner's hand instead of into that player's graveyard. "+
                "PN draws a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game, {
                game.doAction(new CounterItemOnStackAction(it,MagicLocationType.OwnersHand));
                game.doAction(new DrawAction(event.getPlayer()));
            });
        }
    }
]
