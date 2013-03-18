[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    this,
                    "Put two 1/1 red Goblin creature tokens onto the battlefield.");
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
