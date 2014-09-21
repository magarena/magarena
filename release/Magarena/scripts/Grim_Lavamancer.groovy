def choice = new MagicTargetChoice("a card from your graveyard");

def EFFECT = MagicRuleEventAction.create("SN deals 2 damage to target creature or player.");

[
    new MagicPermanentActivation(
        [MagicConditionFactory.GraveyardAtLeast(2)],
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{R}"),
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
