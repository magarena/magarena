[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            final int amount = source.getCounters(MagicCounterType.Pin);
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source,"{"+amount+"}{"+amount+"}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            final int amount = source.getCounters(MagicCounterType.Pin);
            return new MagicEvent(
                source,
                NEG_TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(amount),
                this,
                "SN deals damage equal to the number of pin counters on it to target creature or player.\$"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final MagicPermanent permanent = event.getPermanent();
                final int amount = permanent.getCounters(MagicCounterType.Pin);
                game.logAppendX(event.getPlayer(), amount);
                game.doAction(new DealDamageAction(permanent, it, amount));
            });
        }
    }
]
