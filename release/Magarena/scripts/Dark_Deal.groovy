[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Each player discards all the cards in his or her hand, then draws that many cards minus one."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int hand1 = event.getPlayer().getHandSize() - 1;
            final int hand2 = event.getPlayer().getOpponent().getHandSize() - 1;
                game.addEvent(new MagicDiscardEvent(
                    event.getSource(),
                    event.getPlayer(),
                    event.getPlayer().getHandSize()
                ));
                game.addEvent(new MagicDiscardEvent(
                    event.getSource(),
                    event.getPlayer().getOpponent(),
                    event.getPlayer().getOpponent().getHandSize()
                ));
                game.doAction(new MagicDrawAction(event.getPlayer(),hand1));
                game.doAction(new MagicDrawAction(event.getPlayer().getOpponent(),hand2));
        }
    }
]
