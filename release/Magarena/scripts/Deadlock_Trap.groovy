[
    new MagicPermanentActivation(
    new MagicActivationHints(MagicTiming.Tapping),
    "Tap"
    ) {
        @Override
        public abstract Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayEnergyEvent(source, 1)
            ];
        }
        @Override
        public MagicEvent getEvent(final MagicSource source) {
            return new MagicEvent(
                source,
                MagicTargetChoice.Negative("target creature or planeswalker"),
                MagicTapTargetPicker.Tap,
                this,
                "Tap target creature or planeswalker.\$ " +
                "Its activated abilities can't be activated this turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new TapAction(it));
                game.doAction(new GainAbilityAction(it, MagicAbility.CantActivateAbilities, MagicStatic.UntilEOT));
            });
        }
    }
]

