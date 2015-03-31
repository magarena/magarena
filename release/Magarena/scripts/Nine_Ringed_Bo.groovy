def choice = MagicTargetChoice.Negative("target Spirit creature");

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                choice,
                new MagicDamageTargetPicker(1),
                this,
                "SN deals 1 damage to target Spirit creature\$. " +
                "If that creature would die this turn, exile it instead."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicDealDamageAction(event.getSource(),it,1));
                game.doAction(new MagicAddTurnTriggerAction(it,MagicWhenSelfLeavesPlayTrigger.IfDieExileInstead));
            });
        }
    }
]
