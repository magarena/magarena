[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_NONATTACKING_CREATURE,
                MagicTapTargetPicker.Untap,
                this,
                "Untap target nonattacking creature\$. It gets +1/+3 until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicUntapAction(it));
                game.doAction(new MagicChangeTurnPTAction(it,1,3));
            });
        }
    }
]
