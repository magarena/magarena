[
    new AtBeginOfCombatTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer attackingPlayer) {
            return permanent.getController() == attackingPlayer ?
                new MagicEvent(
                    permanent,
                    TARGET_CREATURE_YOU_CONTROL,
                    this,
                    "PN puts a +1/+1 counter on target creature he or she controls\$. "+
                    "Then if that creature has three or more +1/+1 counters on it, transform SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                 game.doAction(new ChangeCountersAction(event.getPlayer(),it,MagicCounterType.PlusOne,1));
                if (it.getCounters(MagicCounterType.PlusOne) >= 3) {
                 game.doAction(new TransformAction(event.getPermanent()));
                }
            });
        }
    }
]
