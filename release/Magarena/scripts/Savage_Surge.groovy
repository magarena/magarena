[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.POS_TARGET_CREATURE,
                MagicTapTargetPicker.Untap,
                this,
                "Target creature\$ gets +2/+2. Untap that creature."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent permanent ->
                game.doAction(new MagicChangeTurnPTAction(permanent,2,2));
                game.doAction(new MagicUntapAction(permanent));
            });
        }
    }
]
