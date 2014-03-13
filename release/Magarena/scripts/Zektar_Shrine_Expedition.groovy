    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "Token"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicRemoveCounterEvent(source,MagicCounterType.Quest,3),
                new MagicSacrificeEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN puts a 7/1 red Elemental creature token with trample and haste onto the battlefield. "+
                "Exile it at the beginning of the next end step."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCard card = MagicCard.createTokenCard(TokenCardDefinitions.get("7/1 red Elemental creature token with trample and haste"),event.getPlayer());
            game.doAction(new MagicCardAction(
                card,
                event.getPlayer(),
                [MagicPlayMod.EXILE_AT_END_OF_TURN]
            )));
        }
    }
]
