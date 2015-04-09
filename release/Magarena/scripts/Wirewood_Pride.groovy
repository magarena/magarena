[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_CREATURE,
                MagicPumpTargetPicker.create(),
                this,
                "Target creature\$ gets +X/+X until end of turn, where X is the number of Elves on the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final int X = game.getNrOfPermanents(MagicSubType.Elf);
                game.doAction(new MagicChangeTurnPTAction(it, +X, +X));
            });
        }
    }
]
