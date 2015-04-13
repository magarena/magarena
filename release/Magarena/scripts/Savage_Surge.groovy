[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_CREATURE,
                MagicTapTargetPicker.Untap,
                this,
                "Target creature\$ gets +2/+2. Untap that creature."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new ChangeTurnPTAction(it,2,2));
                game.doAction(new MagicUntapAction(it));
            });
        }
    }
]
