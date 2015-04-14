[
    new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            return new MagicEvent(
                permanent,
                this,
                "PN puts a 4/4 red Bird creature token with flying onto the battlefield at the beginning of the next end step."
            );
        }

        @Override
        public void executeEvent(final MagicGame outerGame, final MagicEvent event) {
            //insert trigger to act at the beginning of the next end step
            outerGame.doAction(new AddTriggerAction(new MagicAtEndOfTurnTrigger() {
                @Override
                public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer eotPlayer) {
                    game.doAction(new PlayTokenAction(
                        game.getPlayer(event.getPlayer().getIndex()),
                        TokenCardDefinitions.get("4/4 red Bird creature token with flying")
                    ));
                    game.addDelayedAction(new RemoveTriggerAction(this));
                    return MagicEvent.NONE;
                }
            }));
        }
    }
]
