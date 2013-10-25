[
    new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicMoveCardAction data) {
            return new MagicEvent(
                permanent,
                this,
                "PN puts two 2/2 green Ooze creature tokens onto the battlefield. " +
                "They have \"When this creature dies, put two 1/1 green Ooze creature tokens onto the battlefield.\""
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicPlayTokensAction(event.getPlayer(), TokenCardDefinitions.get("2/2 green Ooze creature token"), 2));
        }
    }
]
