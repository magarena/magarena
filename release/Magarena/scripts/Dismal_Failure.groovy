[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_SPELL,
                this,
                "Counter target spell\$. Its controller discards a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game,new MagicCardOnStackAction() {
                public void doAction(final MagicCardOnStack counteredCard) {
                    game.doAction(new MagicCounterItemOnStackAction(counteredCard));
                    game.addEvent(new MagicDiscardEvent(event.getSource(),counteredCard.getController()));
                }
            });
        }
    }
]
