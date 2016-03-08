[
    new AtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer player) {
            return permanent.isOpponent(player) && MagicCondition.DELIRIUM_CONDITION.accept(permanent) ?
                new MagicEvent(
                    permanent,
                    TARGET_CREATURE_YOUR_OPPONENT_CONTROLS,
                    MagicTapTargetPicker.Tap,
                    player,
                    this,
                    "Tap target creature PN controls.\$"
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new TapAction(it));
            });
        }
    }
]
