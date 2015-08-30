[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "SN deals 2 damage to each creature for each Aura attached to that creature."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            CREATURE.filter(event) each {
                if (it.isEnchanted()) {
                    game.doAction(new DealDamageAction(
                        event.getSource(), 
                        it, 
                        it.getAuraPermanents().size()*2
                    ));
                }
            }
        }
    }
]
