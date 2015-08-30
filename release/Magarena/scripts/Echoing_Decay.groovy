[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                new MagicWeakenTargetPicker(2,2),
                this,
                "Target creature\$ and all other creatures with the same " +
                "name as that creature get -2/-2 until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicTargetFilter<MagicPermanent> targetFilter = new MagicNameTargetFilter(CREATURE, it.getName());
                targetFilter.filter(event) each {
                    game.doAction(new ChangeTurnPTAction(it,-2,-2));
                }
            });
        }
    }
]
