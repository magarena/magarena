[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "destroy land"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{2}"),
                new MagicTapEvent(source),
                new MagicSacrificeEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                new MagicTargetChoice("target nonbasic land an opponent controls"),
                this,
                "Destroy target nonbasic land PN's opponent controls\$. " +
                "Each player searches his or her library for a basic land card, puts it onto the battle field, then shuffles his or her library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new DestroyAction(it));
                game.getAPNAP().each({
                    final MagicPlayer player ->
                    game.addEvent(new MagicSearchOntoBattlefieldEvent(
                        event.getSource(),
                        player,
                        new MagicFromCardFilterChoice(
                            BASIC_LAND_CARD_FROM_LIBRARY,
                            1,
                            false,
                            "a basic land card from library"
                        )
                    ));
                });
            });
        }
    }
]

