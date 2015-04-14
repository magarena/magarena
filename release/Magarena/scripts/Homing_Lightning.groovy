[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                new MagicDamageTargetPicker(4),
                this,
                "SN deals 4 damage to target creature\$ and all other creatures " +
                "with the same name as that creature."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicTargetFilter<MagicPermanent> targetFilter = new MagicNameTargetFilter(
                    CREATURE,
                    it.getName()
                );
                targetFilter.filter(game) each {
                    final MagicPermanent creature ->
                    game.doAction(new DealDamageAction(event.getSource(),creature,4));
                }
            });
        }
    }
]
