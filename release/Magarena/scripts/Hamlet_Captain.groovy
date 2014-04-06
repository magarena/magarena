def Action = {
    final MagicGame game, final MagicEvent event ->
    final Collection<MagicPermanent> targets =
        game.filterPermanents(event.getPlayer(),MagicTargetFilterFactory.TARGET_HUMAN_CREATURE_YOU_CONTROL);
    for (final MagicPermanent creature : targets) {
        if (creature != event.getPermanent()) {
            game.doAction(new MagicChangeTurnPTAction(creature,1,1));
        }
    }
}

def Event = {
    final MagicPermanent permanent ->
    return new MagicEvent(
        permanent,
        Action,
        "Other Human creatures PN controls get +1/+1 until end of turn."
    );
}

[
    new MagicWhenSelfAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return Event(permanent);
        }
    },
    new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (permanent == creature) ? Event(permanent) : MagicEvent.NONE;
        }
    }
]
