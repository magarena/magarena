[
    new MagicAtBeginOfCombatTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                permanent.getOpponent(),
                new MagicMayChoice("Sacrifice a creature?"),
                this,
                "PN may\$ sacrifice a creature. If you do, tap SN and put a +1/+1 counter on it."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent perm = event.getPermanent();
            if (event.getPlayer().controlsPermanent(MagicType.Creature) && event.isYes()) {
                game.addEvent(new MagicSacrificePermanentEvent(perm,event.getPlayer(),MagicTargetChoice.SACRIFICE_CREATURE));
                game.doAction(new MagicTapAction(perm, true)); //tap
                game.doAction(new MagicChangeCountersAction(perm,MagicCounterType.PlusOne,1));
            }
        }
    }
]
