def action = {
    final MagicGame game, final MagicEvent event ->
    game.doAction(new MagicPlayTokensAction(
        event.getPlayer(),
        TokenCardDefinitions.get("Bat"),
        2
    ));
} as MagicEventAction

def getEvent = {
    final MagicPermanent permanent ->
    return new MagicEvent(
        permanent,
        action,
        "PN puts two 1/1 black Bat creature tokens with flying onto the battlefield."
    );
}

[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer player) {
            return getEvent(permanent);
        }
    },
    new MagicPermanentActivation(
        [
            MagicConditionFactory.ManaCost("{3}{B}{B}"),
            MagicCondition.CONTROL_BAT_CONDITION
        ],
        new MagicActivationHints(MagicTiming.Token,true),
        "Token"
    ) {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [ 
                new MagicPayManaCostEvent(source,"{3}{B}{B}"),
                new MagicSacrificePermanentEvent(source,MagicTargetChoice.SACRIFICE_BAT)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return getEvent(source);
        }
    },
    new MagicPermanentActivation(
        [
            MagicCondition.CAN_REGENERATE_CONDITION,
            MagicCondition.CONTROL_BAT_CONDITION,
            new MagicSingleActivationCondition()
        ],
        new MagicActivationHints(MagicTiming.Pump),
        "Regen"
    ) {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                new MagicSacrificePermanentEvent(
                    source,
                    MagicTargetChoice.SACRIFICE_BAT
                )
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Regenerate SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicRegenerateAction(event.getPermanent()));
        }
    }
]
