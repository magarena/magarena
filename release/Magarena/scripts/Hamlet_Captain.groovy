def Action = {
    final MagicGame game, final MagicEvent event ->
    final MagicTargetFilter<MagicPermanent> filter = HUMAN_CREATURE_YOU_CONTROL.except(event.getPermanent());
    final Collection<MagicPermanent> targets = game.filterPermanents(event.getPlayer(),filter);
    for (final MagicPermanent creature : targets) {
        game.doAction(new ChangeTurnPTAction(creature,1,1));
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
    new MagicWhenSelfBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return Event(permanent);
        }
    }
]
