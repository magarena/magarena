[
    new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer eotPlayer) {
            return game.getCreatureDiedThisTurn() ?
                new MagicEvent(
                    permanent,
                    MagicTargetChoice.TARGET_NON_DEMON,
                    new MagicDestroyTargetPicker(false),
                    this,
                    "Destroy target non-Demon creature\$."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicDestroyAction(creature));
                }
            });
        }
    }
]
