def choice = new MagicTargetChoice("a card from your graveyard");

def EFFECT = MagicRuleEventAction.create("Return target creature card from your graveyard to your hand.");

[
    new MagicPermanentActivation(
        [MagicConditionFactory.GraveyardAtLeast(2)],
        new MagicActivationHints(MagicTiming.Token),
        "Token"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{2}{B}{B}"),
                new MagicTapEvent(source),
                new MagicExileCardEvent(source, choice),
                new MagicExileCardEvent(source, choice)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return EFFECT.getEvent(source);
        }
    }
]
