[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN puts a 3/3 blue Weird creature " +
                "token with defender and flying onto the battlefield. "+
                "Exile it at the beginning of the next end step."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCard card = MagicCard.createTokenCard(TokenCardDefinitions.get("3/3 blue Weird creature token with defender and flying"),event.getPlayer());
            game.doAction(new MagicCardAction(
                card,
                event.getPlayer(),
                [MagicPlayMod.EXILE_AT_END_OF_TURN]
            )));
        }
    }
]
