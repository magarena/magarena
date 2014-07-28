[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.POS_TARGET_BLOCKING_CREATURE,
                MagicPumpTargetPicker.create(),
                this,
                "Target blocking creature\$ gets +4/+4 until end of turn." +
                "PN draws a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicChangeTurnPTAction(it,+4,+4));
                game.doAction(new MagicDrawAction(event.getPlayer()));
            });
        }
    }
]
