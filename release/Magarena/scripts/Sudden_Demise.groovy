[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int amount = payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                MagicColorChoice.ALL_INSTANCE,
                amount,
                this,
                "SN deals X damage to each creature of the chosen color.\$ (X=${amount})"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            CREATURE.filter(event) each {
                if (it.hasColor(event.getChosenColor())) {
                    game.doAction(new DealDamageAction(event.getSource(), it, event.getRefInt()));
                }
            }
        }
    }
]
