def choice = new MagicTargetChoice("a card from your graveyard");

def EFFECT = MagicRuleEventAction.create("Target player loses 3 life.");

[
    new MagicPermanentActivation(
        [MagicConditionFactory.GraveyardAtLeast(3)],
        new MagicActivationHints(MagicTiming.Removal),
        "-Life"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicExileCardEvent(source, choice),
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
