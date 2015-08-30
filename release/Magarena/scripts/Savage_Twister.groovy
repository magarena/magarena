[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int amount=payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                this,
                "SN deals "+amount+" damage to each creature."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount=event.getCardOnStack().getX();
            final MagicSource source=event.getSource();
            CREATURE.filter(event) each {
                game.doAction(new DealDamageAction(source,it,amount));
            }
        }
    }
]
