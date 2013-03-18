[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer player) {
            return new MagicEvent(
                    permanent,
                    this,
                    "PN puts two 1/1 red Goblin creature tokens onto the battlefield.");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            game.doAction(new MagicPlayTokensAction(
                event.getPlayer(), 
                TokenCardDefinitions.get("Goblin1"),
                2
            ));
        }
    }
]
