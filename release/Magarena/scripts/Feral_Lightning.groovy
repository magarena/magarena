[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Put three 3/1 red Elemental creature tokens with haste onto the battlefield. " + 
                "Exile them at the beginning of the next end step."
            );
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (int i = 0; i < 3; i++) {
                game.doAction(new PlayTokenAction(
                    event.getPlayer(), 
                    TokenCardDefinitions.get("3/1 red Elemental creature token with haste"),
                    MagicPlayMod.EXILE_AT_END_OF_TURN
                ));
            } 
        }
    }
]
