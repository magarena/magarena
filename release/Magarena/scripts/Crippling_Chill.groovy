[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_CREATURE,
                MagicTapTargetPicker.Tap,
                this,
                "Tap target creature\$. " +
                "It doesn't untap during its controller's next untap step. " +
                "Draw a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicTapAction(it));
                game.doAction(MagicChangeStateAction.Set(
                    it,
                    MagicPermanentState.DoesNotUntapDuringNext
                ));
                game.doAction(new MagicDrawAction(event.getPlayer()));
            });
        }
    }
]
