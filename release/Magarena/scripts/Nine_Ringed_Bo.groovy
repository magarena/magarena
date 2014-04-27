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
                MagicTargetChoice.Negative("target Spirit creature"),
                new MagicDamageTargetPicker(1),
                this,
                "SN deals 1 damage to target Spirit creature\$. " +
                "If that creature would die this turn, exile it instead."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent creature ->
                final MagicDamage damage = new MagicDamage(event.getSource(),creature,1);
                game.doAction(new MagicAddTurnTriggerAction((MagicPermanent)creature,MagicWhenSelfLeavesPlayTrigger.IfDieExileInstead));
                game.doAction(new MagicDealDamageAction(damage));
            });
        }
    }
]
