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
        public void executeEvent(final MagicGame outerGame, final MagicEvent event) {
            //insert trigger to act at the beginning of the next end step
            outerGame.doAction(new MagicAddTriggerAction(new MagicAtEndOfTurnTrigger() {
                public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final eotPlayer) {
                    game.doAction(new MagicPlayTokenAction(
                        game.getPlayer(event.getPlayer().getIndex()),
                        TokenCardDefinitions.get("Bird4")
                    ));
                    game.addDelayedAction(new MagicRemoveTriggerAction(this));
                    return MagicEvent.NONE;
                }
            }));
        }
    }
]
