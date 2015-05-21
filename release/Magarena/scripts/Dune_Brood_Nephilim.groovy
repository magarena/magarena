[
    new MagicWhenSelfCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return new MagicEvent(
                permanent,
                this,
                "PN puts a 1/1 colorless Sand creature token onto the battlefield for each land PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = event.getPlayer().getNrOfPermanents(MagicType.Land);
            game.logAppendMessage(player, "("+amount+")");
            game.doAction(new PlayTokensAction(
                player,
                CardDefinitions.getToken("1/1 colorless Sand creature token"),
                amount
            ));
        }
    }
]
