[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_CREATURE,
                MagicPumpTargetPicker.create(),
                this,
                "Target creature\$ gets +2/+2 until end of turn. " +
                "If it's paired with a creature, that creature " +
                "also gets +2/+2 until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicChangeTurnPTAction(it,2,2));
                if (it.isPaired()) {
                    game.doAction(new MagicChangeTurnPTAction(
                        it.getPairedCreature(),
                        2,
                        2
                    ));
                }
            });
        }
    }
]
