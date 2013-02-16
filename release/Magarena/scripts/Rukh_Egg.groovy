[
    new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent getEvent(final MagicPermanent permanent) {
            return new MagicEvent(
                permanent,
                this,
                "PN puts a 4/4 red Bird creature token with flying onto the battlefield at the beginning of the next end step."
            );
        }

        @Override
        public void executeEvent(
                final MagicGame outerGame,
                final MagicEvent event,
                final Object[] choiceResults) {
            //insert trigger to act at the beginning of the next end step
            outerGame.doAction(new MagicAddTurnTriggerAction(MagicPermanent.NONE, new MagicAtEndOfTurnTrigger() {
                public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final eotPlayer) {
                    game.doAction(new MagicPlayTokenAction(event.getPlayer(), TokenCardDefinitions.get("Bird4")));
                    return MagicEvent.NONE;
                }
            }));
        }
    }
]
