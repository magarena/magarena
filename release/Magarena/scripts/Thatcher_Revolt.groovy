[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN puts three 1/1 red Human creature " +
                "tokens with haste onto the battlefield. Sacrifice " +
                "those tokens at the beginning of the next end step."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicCardDefinition token = TokenCardDefinitions.get("1/1 red Human creature tokens with haste");
            for (int x=3;x>0;x--) {
                game.doAction(new PlayTokenAction(
                    player, 
                    token,
                    MagicPlayMod.SACRIFICE_AT_END_OF_TURN
                ));
            }
        }
    }
]
