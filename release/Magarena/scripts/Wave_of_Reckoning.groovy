[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Each creature deals damage to itself equal to its power."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            CREATURE.filter(event) each {
                final int amount = it.getPower();
                if (amount > 0) {
                    game.doAction(new DealDamageAction(it, it, amount));
                }
            }
        }
    }
]
