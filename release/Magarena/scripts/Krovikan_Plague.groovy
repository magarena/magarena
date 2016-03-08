[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEnchantedEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(1),
                source.getEnchantedPermanent(),
                this,
                "SN deals 1 damage to target creature or player.\$ Put a -0/-1 counter on RN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                game.doAction(new DealDamageAction(event.getPermanent(), it, 1));
                game.doAction(new ChangeCountersAction(event.getRefPermanent(), MagicCounterType.MinusZeroMinusOne, 1));
            });
        }
    }
]
