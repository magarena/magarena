[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(
                final MagicCardOnStack cardOnStack,
                final MagicPayedCost payedCost) {
            final int x = payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                this,
                "PN puts " + x + " 4/4 white Angel " +
                "creature tokens with flying onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            int x = event.getCardOnStack().getX();
            for (;x>0;x--) {
                game.doAction(new MagicPlayTokenAction(
                    event.getPlayer(),
                    TokenCardDefinitions.get("Angel4")
                ));
            }
        }
    }
]
