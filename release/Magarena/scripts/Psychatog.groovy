def choice = new MagicTargetChoice("a card from your graveyard");

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Graveyard"
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
            return new MagicEvent(
                source,
                this,
                "SN gets +1/+1 until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
           game.doAction(new MagicChangeTurnPTAction(event.getPermanent(), 1, 1));
        }
    }
]
