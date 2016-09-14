[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Pump"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{1}{B}"),
                new MagicDiscardChosenEvent(source, A_CREATURE_CARD_FROM_HAND)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                payedCost.getTarget(),
                this,
                "SN gets +X/+X until end of turn, where X is the discarded card's converted mana cost."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getRefCard().getConvertedCost();
            game.logAppendValue(event.getPlayer(),amount);
            game.doAction(new ChangeTurnPTAction(event.getPermanent(), +amount, +amount));
        }
    }
]
