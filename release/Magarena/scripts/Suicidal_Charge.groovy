[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.MustAttack),
        "Weaken"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicSacrificeEvent(source)];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Creatures PN's opponents control get -1/-1 until end of turn. Those creatures attack this turn if able."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            CREATURE_YOUR_OPPONENT_CONTROLS.filter(event) each {
                game.doAction(new ChangeTurnPTAction(it,-1,-1));
                game.doAction(new GainAbilityAction(it,MagicAbility.AttacksEachTurnIfAble));
            }
        }
    }
]
