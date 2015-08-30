[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN discards all the cards in his or her hand, then draws that many cards."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer()
            final int amount = event.getPlayer().getHandSize();
            game.logAppendValue(player, amount);
            game.addEvent(new MagicDiscardHandEvent(event.getSource()));
            game.addEvent(new MagicDrawEvent(event.getSource(), player, amount));
        }
    }
]
