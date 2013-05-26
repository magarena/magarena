[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.POS_TARGET_CREATURE,
                MagicPumpTargetPicker.create(),
                this,
                "Target creature\$ gets +2/+2 until end of turn. " +
                "If it's paired with a creature, that creature " +
                "also gets +2/+2 until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicChangeTurnPTAction(creature,2,2));
                    if (creature.isPaired()) {
                        game.doAction(new MagicChangeTurnPTAction(
                            creature.getPairedCreature(),
                            2,
                            2
                        ));
                    }
                }
            });
        }
    }
]
