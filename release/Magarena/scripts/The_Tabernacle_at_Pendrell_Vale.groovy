
def TabernacleUpkeep = new AtYourUpkeepTrigger() {
    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
        return new MagicEvent(
            permanent,
            new MagicMayChoice(
                new MagicPayManaCostChoice(MagicManaCost.create("{1}"))
            ),
            this,
            "PN may\$ pay {1}. If PN doesn't, destroy SN."
        );
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        if (event.isNo()) {
            game.doAction(new DestroyAction(event.getPermanent()));
        }
    }
};

[
    new MagicStatic(MagicLayer.Ability, CREATURE) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            permanent.addAbility(TabernacleUpkeep);
        }
    }
]
