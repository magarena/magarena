[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.Negative("target green creature"),
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target green creature\$. It can't be regenerated. PN draws a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(MagicChangeStateAction.Set(it,MagicPermanentState.CannotBeRegenerated));
                game.doAction(new MagicDestroyAction(it));
                game.doAction(new MagicDrawAction(event.getPlayer()));
            });
        }
    }
]
