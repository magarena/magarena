[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Scry 4, then " +
                "PN draws two cards."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicScryXEvent(event.getSource(),event.getPlayer(),4));
            game.doAction(new MagicDrawAction(event.getPlayer(),2));
        }
    }
]
