[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                new MagicWeakenTargetPicker(3,3),
                this,
                "Target creature\$ and all other creatures with the same " +
                "name as that creature get -3/-3 until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicTargetFilter<MagicPermanent> targetFilter = new MagicNameTargetFilter(CREATURE, it.getName());
                targetFilter.filter(game) each {
                    final MagicPermanent creature ->
                    game.doAction(new MagicChangeTurnPTAction(creature,-3,-3));
                }
            });
        }
    }
]
