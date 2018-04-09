def A_CREATURE_CARD_FROM_GRAVEYARD = new MagicTargetChoice("a creature card from your graveyard");
[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "+Counters"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicExileCardEvent(source, A_CREATURE_CARD_FROM_GRAVEYARD)];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                payedCost.getTarget(),
                this,
                "PN puts X +0/+1 counters on SN, where X is the exiled card's converted mana cost."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCard exiled = event.getRefCard();
            final int amount = exiled.getConvertedCost();
            game.logAppendX(event.getPlayer(), amount);
            game.doAction(new ChangeCountersAction(event.getPlayer(), event.getPermanent(), MagicCounterType.PlusZeroPlusOne, amount));
        }
    }
]
