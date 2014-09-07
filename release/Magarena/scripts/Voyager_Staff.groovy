[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal, true),
        "Exile"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source,"{2}"),
		    new MagicSacrificeEvent(source)
		   ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.TARGET_CREATURE,
                MagicExileTargetPicker.create(),
                this,
                "Exile target creature\$. Return that card to the battlefield under its owner's control at the beginning of the next end step."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicExileUntilEndOfTurnAction(it));
            });
        }
    }
]
