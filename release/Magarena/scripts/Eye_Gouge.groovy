[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                new MagicWeakenTargetPicker(1,1),
                this,
                "Target creature\$ gets -1/-1 until end of turn. If it's a Cyclops, destroy it."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new ChangeTurnPTAction(it,-1,-1));
                if (it.hasSubType(MagicSubType.Cyclops)) {
                    game.doAction(new MagicDestroyAction(it));
                }
            });
        }
    }
]
