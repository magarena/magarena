[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source, "{R}"),
                new MagicDiscardChosenEvent(source, A_CARD_FROM_HAND)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                TARGET_CREATURE,
                payedCost.getTarget(),
                this,
                "SN deals damage to target creature\$ equal to the discarded card's converted mana cost."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final int amount = event.getRefCard().getConvertedCost();
                game.logAppendValue(event.getPlayer(),amount);
                game.doAction(new DealDamageAction(event.getPermanent(), it, amount));
            });
        }
    }
]
