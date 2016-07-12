[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_CREATURE,
                MagicPumpTargetPicker.create(),
                this,
                "Untap target creature\$ and each other creature that shares a color with it. Those creatures get +2/+0 until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                for (final MagicPermanent creature : CREATURE.filter(event)) {
                    if (creature == it || creature.shareColor(it)) {
                        game.doAction(new UntapAction(creature));
                        game.doAction(new ChangeTurnPTAction(creature, 2, 0));
                    }
                }
            });
        }
    }
]
