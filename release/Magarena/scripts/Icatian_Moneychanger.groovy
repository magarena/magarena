[
    new MagicPermanentActivation(
        [MagicCondition.YOUR_UPKEEP_CONDITION],
        new MagicActivationHints(MagicTiming.Pump),
        "Gain life"
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
                "PN gains 1 life for each credit counter on SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPermanent().getCounters(MagicCounterType.Credit);
            final MagicPlayer player = event.getPlayer();
            game.doAction(new ChangeLifeAction(player,amount));
            game.logAppendMessage(player," ("+amount+")");
        }
    }
]
