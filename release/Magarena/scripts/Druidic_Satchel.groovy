//Land Creatures should trigger two effects

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Draw),
        "Reveal"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{2}"),
                new MagicTapEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Reveal the top card of PN's library. If it's a creature card, create a 1/1 green Saproling creature token. " +
                "If it's a land card, put that card onto the battlefield under PN's control. " +
                "If it's a noncreature, nonland card, PN gains 2 life."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicCard card : event.getPlayer().getLibrary().getCardsFromTop(1)) {
                game.doAction(new RevealAction(card));
                if (card.hasType(MagicType.Creature)) {
                    game.doAction(new PlayTokensAction(event.getPlayer(), CardDefinitions.getToken("1/1 green Saproling creature token"), 1));
                }
                if (card.hasType(MagicType.Land)) {
                    game.doAction(new PutOntoBattlefieldAction(MagicLocationType.OwnersLibrary, card, event.getPlayer()));
                }
                if (!card.hasType(MagicType.Creature) && !card.hasType(MagicType.Land)) {
                    game.doAction(new ChangeLifeAction(event.getPlayer(), 2));
                }
            }
        }
    }
]

