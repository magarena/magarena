[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN looks at the top card of his or her library. PN may put that card on the bottom of his or her library. " +
                "Draw a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(MagicScryEvent.Pseudo(event));
            game.addEvent(new MagicDrawEvent(event.getSource(),event.getPlayer(),1));
        }
    }
]
