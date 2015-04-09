[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN draws two cards, then sacrifices a permanent."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicDrawEvent(event.getSource(),event.getPlayer(),2));
            game.addEvent(new MagicSacrificePermanentEvent(event.getSource(),SACRIFICE_PERMANENT));
        }
    }
]
