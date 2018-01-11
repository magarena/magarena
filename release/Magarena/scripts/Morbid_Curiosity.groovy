[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                payedCost.getTarget(),
                this,
                "PN draws RN cards."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DrawAction(event.getPlayer(), event.getRefCard().getConvertedCost()));
        }
    }
]

