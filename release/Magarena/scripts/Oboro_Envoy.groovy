[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Weaken"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{2}"),
                new MagicBounceChosenPermanentEvent(source, A_LAND_YOU_CONTROL)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_CREATURE,
                new MagicWeakenTargetPicker(0,0),
                this,
                "Target creature\$ gets -X/-0 until end of turn, where X is the number of cards in PN's hand."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = event.getPlayer().getHandSize();
            event.processTargetPermanent(game, {
                game.logAppendValue(player, amount);
                game.doAction(new ChangeTurnPTAction(it, -amount, 0));
            });
        }
    }
]
