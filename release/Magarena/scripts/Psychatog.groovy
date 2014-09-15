def choice = new MagicTargetChoice("a card from your graveyard");

def EFFECT = MagicRuleEventAction.create("SN gets +1/+1 until end of turn.");

[
    new MagicPermanentActivation(
        [MagicConditionFactory.GraveyardAtLeast(2)],
        new MagicActivationHints(MagicTiming.Pump),
        "Pump"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
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
