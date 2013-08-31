[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Put a 1/1 green Saproling creature token onto the battlefield for each creature you control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicPlayTokensAction(
                event.getPlayer(),
                TokenCardDefinitions.get("Saproling"),
                event.getPlayer().getNrOfPermanents(MagicType.Creature)
            ));
        }
    },
    new MagicCardActivation(
        new MagicActivationHints(MagicTiming.Token,true),
        "Instant"
    ) {
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            return [
                new MagicPayManaCostEvent(source, "{5}{G}")
            ];
        }
    }
]
