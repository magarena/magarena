[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    MagicTargetChoice.TARGET_CREATURE_YOUR_OPPONENT_CONTROLS,
                    new MagicNoCombatTargetPicker(true,true,false),
                    this,
                    "Detain target creature\$ an opponent controls."
                ):
                MagicEvent.NONE
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicDetainAction(event.getPlayer(), it));
            });
        }
    }
]
