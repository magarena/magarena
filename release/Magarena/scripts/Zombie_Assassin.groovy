def choice = new MagicTargetChoice("a card from your graveyard");

def EFFECT = MagicRuleEventAction.create("Destroy target nonblack creature. It can't be regenerated.");

[
    new MagicPermanentActivation(
        [
            MagicConditionFactory.GraveyardAtLeast(2)
        ],
        new MagicActivationHints(MagicTiming.Removal),
        "Destroy"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                   new MagicTapEvent(source),
                   new MagicExileCardEvent(source, choice),
                   new MagicExileCardEvent(source, choice),
                   new MagicExileEvent(source)   
                   ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return EFFECT.getEvent(source);
        }
    }
]
