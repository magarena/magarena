[
    new MagicAtBeginOfCombatTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            final MagicPlayer opponent = permanent.getOpponent();
            return opponent.controlsPermanent(MagicType.Creature) ? 
                new MagicEvent(
                permanent,
                opponent,
                new MagicMayChoice("Sacrifice a creature?"),
                this,
                "PN may\$ sacrifice a creature. If PN does, tap SN and put a +1/+1 counter on it."
                ):
            MagicEvent.NONE
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent creature = event.getPermanent();
            if (event.isYes()) {
                game.addEvent(new MagicSacrificePermanentEvent(creature,event.getPlayer(),MagicTargetChoice.SACRIFICE_CREATURE));
                game.doAction(new MagicTapAction(creature));
                game.doAction(new MagicChangeCountersAction(creature,MagicCounterType.PlusOne,1));
            }
        }
    }
]
