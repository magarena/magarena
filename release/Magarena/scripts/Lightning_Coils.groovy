def trigger = new AtEndOfTurnTrigger() {
    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer eotPlayer) {
        return new MagicEvent(
            permanent,
            this,
            "Exile SN."
        );
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new RemoveFromPlayAction(event.getPermanent(), MagicLocationType.Exile));
    }
}

def playMod = {
    final MagicPermanent perm ->
    final MagicGame game = perm.getGame();
    game.doAction(new AddTurnTriggerAction(perm, trigger));
}

[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            final int counters = permanent.getCounters(MagicCounterType.Charge);
            return permanent.getCounters(MagicCounterType.Charge) >= 5 ?
                new MagicEvent(
                    permanent,
                    counters,
                    this,
                    "PN remove all charge counters from SN and creates RN 3/1 red Elemental creature tokens with haste. "+
                    "Exile them at the beginning of the next end step."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new ChangeCountersAction(event.getPlayer(),event.getPermanent(),MagicCounterType.Charge,-event.getRefInt()));
            event.getRefInt().times {
                game.doAction(new PlayTokenAction(
                    event.getPlayer(),
                    CardDefinitions.getToken("3/1 red Elemental creature token with haste"),
                    playMod
                ));
            }
        }
    }
]
