def trigger = new AtEndOfTurnTrigger() {
    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer eotPlayer) {
        return new MagicEvent(
            permanent,
            this,
            "Exile SN unless PN has the city's blessing."
        );
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        if (!event.getPlayer().hasState(MagicPlayerState.CitysBlessing)) {
            game.doAction(new RemoveFromPlayAction(event.getPermanent(), MagicLocationType.Exile));
        }
        game.doAction(new RemoveTriggerAction(event.getPermanent(), this));
    }
}

def playMod = {
    final MagicGame game, final MagicPermanent perm ->
    game.doAction(new AddTriggerAction(perm, trigger));
}

[
    new ThisAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent attacker) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(new MagicPayManaCostChoice(MagicManaCost.create("{X}{R}"))),
                this,
                "PN may\$ pay {X}{R}\$. " +
                "If PN does, create X 1/1 red Elemental creature tokens that are tapped and attacking."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.getPaidMana().getX().times {
                    game.doAction(new PlayTokenAction(
                        event.getPlayer(),
                        CardDefinitions.getToken("1/1 red Elemental creature token"),
                        MagicPlayMod.TAPPED_AND_ATTACKING,
                        playMod
                    ));
                }
            }
        }
    }
]

