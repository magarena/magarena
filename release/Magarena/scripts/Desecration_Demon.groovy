[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                        permanent,
                        upkeepPlayer.opponent, //should be player.opponent
                        new MagicMayChoice(MagicTargetChoice.SACRIFICE_CREATURE),
                        MagicSacrificeTargetPicker.create(),
                        this,
                        "You may sacrifice a creature. If you do, tap SN and put a +1/+1 counter on it."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent perm = event.getPermanent();
            if(event.isYes()){
                game.addEvent(new MagicSacrificePermanentEvent(perm, event.getPlayer(),MagicTargetChoice.SACRIFICE_CREATURE));
                game.doAction(new MagicTapAction(perm, true)); //tap
                game.doAction(new MagicChangeCountersAction(perm, MagicCounterType.PlusOne, 1, true));
            }
        }
    }
]