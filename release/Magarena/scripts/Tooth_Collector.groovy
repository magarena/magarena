[
    new AtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer player) {
            return permanent.isOpponent(player) && MagicCondition.DELIRIUM_CONDITION.accept(permanent) ?
                new MagicEvent(
                    permanent,
                    TARGET_CREATURE_YOUR_OPPONENT_CONTROLS,
                    new MagicWeakenTargetPicker(1,1),
                    player,
                    this,
                    "Target creature RN controls\$ gets -1/-1 until end of turn."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new ChangeTurnPTAction(it, -1, -1));
            });
        }
    }
]
