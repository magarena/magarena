def choice = new MagicTargetChoice("a card from your graveyard");

def EFFECT = MagicRuleEventAction.create("Target player discards a card.");

[
    new MagicPermanentActivation(
        [
            MagicCondition.THRESHOLD_CONDITION,
            MagicConditionFactory.GraveyardAtLeast(2),
            MagicCondition.SORCERY_CONDITION
        ],
        new MagicActivationHints(MagicTiming.Draw),
        "Discard"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source,"{1}{B}"),
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
