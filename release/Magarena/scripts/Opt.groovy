[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Look at the top card of your library. You may put that card on the bottom of your library." +
                "Draw a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicScryEvent(event,false));
            game.addEvent(new MagicDrawEvent(event.getSource(),event.getPlayer(),1));
        }
    }
]
