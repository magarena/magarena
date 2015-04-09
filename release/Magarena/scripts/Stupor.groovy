[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_OPPONENT,
                this,
                "Target opponent\$ discards a card at random, then discards a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.addEvent(MagicDiscardEvent.Random(event.getSource(),it));
                game.addEvent(new MagicDiscardEvent(event.getSource(),it));
            });
        }
    }
]
