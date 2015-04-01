[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Draw two cards, then discard two cards. Untap up to three lands."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicDrawAction(event.getPlayer(),2));
            game.addEvent(new MagicDiscardEvent(event.getSource(), event.getPlayer(), 2));
            game.addEvent(new MagicRepeatedPermanentsEvent(
                event.getSource(),
                MagicTargetChoice.TARGET_LAND,
                3,
                MagicChainEventFactory.Untap
            ));
        }
    }
]
