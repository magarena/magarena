[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Counter),
        "Counter"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{2}{U}"),
                new MagicDiscardChosenEvent(source, A_CARD_FROM_HAND)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                NEG_TARGET_SPELL,
                payedCost.getTarget(),
                this,
                "Counter target spell\$ if it has the same converted mana cost as the discarded card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game, {
                final int amount = event.getRefCard().getConvertedCost();
                game.logAppendValue(event.getPlayer(),amount);
                if (it.getConvertedCost() == amount) {
                    game.doAction(new CounterItemOnStackAction(it));
                    game.logAppendMessage(event.getPlayer(), "\n ("+it.getName()+") is countered.");
                }
            });
        }
    }
]
