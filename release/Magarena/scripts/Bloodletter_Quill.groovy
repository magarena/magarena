[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Draw),
        "Draw"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source, "{2}"),
                new MagicAddCounterEvent(source, MagicCounterType.Blood, 1)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN draws a card, then loses 1 life for each blood counter on SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount=event.getPermanent().getCounters(MagicCounterType.Blood);
            game.logAppendValue(player,amount);
            game.doAction(new DrawAction(player));
            game.doAction(new ChangeLifeAction(player, -amount));
        }
    }
]
