[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source,"{3}"),
                new MagicSacrificeEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(source.getCounters(MagicCounterType.Charge)),
                this,
                "SN deals damage equal to the number of charge counters on it to target creature or player\$. "+ "("+source.getCounters(MagicCounterType.Charge)+")"
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final MagicPermanent source=event.getPermanent();
                final int amount=source.getCounters(MagicCounterType.Charge);
                game.doAction(new DealDamageAction(source,it,amount));
            });
        }
    }
]
