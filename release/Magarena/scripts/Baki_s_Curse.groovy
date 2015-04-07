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
            game.filterPermanents(MagicTargetFilterFactory.CREATURE) each {
                if (it.isEnchanted()) {
                    final int amount = it.getAuraPermanents().size()*2
                    game.doAction(new MagicDealDamageAction(event.getSource(), it, amount));
                }
            }
        }
    }
]
