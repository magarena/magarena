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
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            return getEvent(permanent);
        }
    },
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token,true),
        "Token"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
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
        new MagicActivationHints(MagicTiming.Pump),
        "Regen"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicSacrificePermanentEvent(
                    source,
                    MagicTargetChoice.SACRIFICE_BAT
                ),
                new MagicRegenerationConditionsEvent(source, this)
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
