[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Pump"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{3}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "SN gets +1/+0 until end of turn for each basic land type among lands PN controls."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent creature = event.getPermanent();
            final MagicPlayer player = event.getPlayer()
            final int amount = player.getDomain();
            game.logAppendMessage(player," ("+amount+")");
            game.doAction(new ChangeTurnPTAction(creature,amount,0));
        }
    }
]
