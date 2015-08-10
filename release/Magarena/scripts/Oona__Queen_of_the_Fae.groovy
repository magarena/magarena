[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "Token"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source, "{X}{U/B}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_OPPONENT,
                payedCost.getX(),
                this,
                "Choose a color. Target opponent\$ exiles the top RN cards of his or her library. " +
                "For each card of the chosen color exiled this way, put a 1/1 blue and black Faerie Rogue creature token with flying onto the battlefield."
            );
        }

        @Override
        public void executeEvent(final MagicGame outerGame, final MagicEvent outerEvent) {
            outerEvent.processTargetPlayer(outerGame, {
                outerGame.addEvent(new MagicEvent(
                    outerEvent.getSource(),
                    MagicColorChoice.ALL_INSTANCE,
                    it,
                    {
                        final MagicGame game, final MagicEvent event ->
                        final MagicColor color = event.getChosenColor();
                        final MagicPlayer player = event.getRefPlayer();
                        final int x = outerEvent.getRefInt();
                        for (final MagicCard card : player.getLibrary().getCardsFromTop(x)) {
                            game.doAction(new ShiftCardAction(card,MagicLocationType.OwnersLibrary,MagicLocationType.Exile));
                            if (card.hasColor(color)) {
                                game.doAction(new PlayTokenAction(
                                    event.getPlayer(),
                                    CardDefinitions.getToken("1/1 black Faerie Rogue creature token with flying")
                                ));
                            }
                        }
                    },
                    "Chosen color\$."
                ));
            });
        }
    }
]
