[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN puts a 1/1 blue and black Horror creature token with flying onto the battlefield. " +
                "Cipher."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicPlayTokenAction(
                event.getPlayer(),
                TokenCardDefinitions.get("1/1 blue and black Horror creature token with flying")
            ));
            game.doAction(new MagicCipherAction(event.getCardOnStack(),event.getPlayer()));
        }
    }
]
