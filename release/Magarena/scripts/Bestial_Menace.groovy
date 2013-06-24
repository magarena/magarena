[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Put a 1/1 green Snake creature token, " +
                    "a 2/2 green Wolf creature token and " +
                    "a 3/3 green Elephant creature token onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player=event.getPlayer();
            game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.get("Snake")));
            game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.get("Wolf")));
            game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.get("Elephant")));
        }
    }
]
