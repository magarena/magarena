[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int amount=payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                this,
                "All creatures get +"+amount+"/-"+amount+" until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amt = event.getCardOnStack().getX();
            CREATURE.filter(event) each {
                game.doAction(new ChangeTurnPTAction(it, amt, -amt));
            }
        }
    }
]
