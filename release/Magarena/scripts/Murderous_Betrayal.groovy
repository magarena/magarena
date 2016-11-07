[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Destroy"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{B}{B}"),
                new MagicPayLifeEvent(source, source.getController().getHalfLifeRoundUp())
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_NONBLACK_CREATURE,
                MagicDestroyTargetPicker.DestroyNoRegen,
                this,
                "Destroy target nonblack creature.\$ It can't be regenerated."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(ChangeStateAction.Set(it, MagicPermanentState.CannotBeRegenerated));
                game.doAction(new DestroyAction(it));
            });
        }
    }
]
