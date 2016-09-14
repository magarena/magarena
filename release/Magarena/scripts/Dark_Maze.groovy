def COULD_ATTACK = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            return permanent.isTapped() == false &&
                   ((permanent.hasState(MagicPermanentState.Summoned) == false) ||
                   (permanent.hasState(MagicPermanentState.Summoned) && permanent.hasAbility(MagicAbility.Haste)));
        }
    };

[
    new MagicPermanentActivation(
        [new MagicArtificialCondition(COULD_ATTACK)],
        new MagicActivationHints(MagicTiming.FirstMain),
        "Attack"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{0}"),
                new MagicPlayAbilityEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "SN can attack this turn as though it didn't have defender. "+
                "Exile it at the beginning of the next end step."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            game.doAction(new GainAbilityAction(permanent, MagicAbility.CanAttackWithDefender));
            game.doAction(new AddTurnTriggerAction(
                permanent,
                AtEndOfTurnTrigger.ExileAtEnd
            ));
        }
    }
]
