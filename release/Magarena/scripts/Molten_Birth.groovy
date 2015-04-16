[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicCoinFlipChoice(),
                this,
                "PN puts two 1/1 red Elemental creature tokens onto the battlefield. "+
                "Then PN flips a coin.\$ If PN wins the flip, return SN to its owner's hand."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokensAction(
                event.getPlayer(), 
                TokenCardDefinitions.get("1/1 red Elemental creature token"),
                2
            ));
            final Boolean heads = event.isMode(1) 
            game.addEvent(new MagicCoinFlipEvent(
                event.getSource(),
                heads,
                event.getPlayer(),
                new ChangeCardDestinationAction(event.getCardOnStack(), MagicLocationType.OwnersHand),
                null
            ));
        }
    }
]
