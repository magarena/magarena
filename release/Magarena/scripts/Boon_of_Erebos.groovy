[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_CREATURE,
                MagicPumpTargetPicker.create(),
                this,
                "Target creature\$ gets +2/+0 until end of turn. Regenerate it. PN loses 2 life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicChangeTurnPTAction(it, 2, 0));
                game.doAction(new MagicRegenerateAction(it));
                game.doAction(new MagicChangeLifeAction(event.getPlayer(),-2));
            });
        }
    }
]
