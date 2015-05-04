[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Draw),
        "Discard"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source,"{4}"),
                new MagicSacrificeEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_PLAYER,
                this,
                "Target player\$ discards a card for each charge counter on SN. ("+source.getCounters(MagicCounterType.Charge)+")"
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicPermanent source=event.getPermanent();
                final int amount=source.getCounters(MagicCounterType.Charge);
                game.addEvent(new MagicDiscardEvent(source,it,amount));
            });
        }
    }
]
