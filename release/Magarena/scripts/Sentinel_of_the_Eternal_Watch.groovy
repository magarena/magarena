[
    new AtBeginOfCombatTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer attackingPlayer) {
            return permanent.getOpponent() == attackingPlayer ?
                new MagicEvent(
                    permanent,
                    TARGET_CREATURE_YOUR_OPPONENT_CONTROLS,
                    permanent.getOpponent(),
                    this,
                    "Tap target creature\$ that RN controls."
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
