[
    new MagicCardAbilityActivation(
        new MagicActivationHints(MagicTiming.Pump,true),
        "Protection"
    ) {
        @Override
        public void change(final MagicCardDefinition cdef) {
            cdef.addGraveyardAct(this);
        }

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
            return [
                new MagicPayManaCostEvent(source, "{2}{W}"),
            ];
        }

        @Override
        public MagicEvent getCardEvent(final MagicCard source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicColorChoice.ALL_INSTANCE,
                this,
                "Choose a color\$. " +
                "Creatures PN controls gain protection from the chosen color until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            final MagicAbility protection = event.getChosenColor().getProtectionAbility();
            CREATURE_YOU_CONTROL.filter(event) each {
                game.doAction(new GainAbilityAction(it, protection));
            }
        }
    }
]
