[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN draws three cards, then put two cards from PN's hand on top of PN's library in any order."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DrawAction(event.getPlayer(),3));
            game.addEvent(new MagicReturnCardEvent(event.getSource(), event.getPlayer()));
            game.addEvent(new MagicReturnCardEvent(event.getSource(), event.getPlayer()));
        }
    }
]
