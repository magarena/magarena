
def TabernacleUpkeep = new MagicAtYourUpkeepTrigger() {
    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
        return new MagicEvent(
            permanent,
            new MagicMayChoice(
                new MagicPayManaCostChoice(MagicManaCost.create("{1}"))
            ),
            this,
            "You may\$ pay {1}. If you don't, destroy SN."
        );
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        if (event.isNo()) {
            game.doAction(new MagicDestroyAction(event.getPermanent()));
        }
    }
};

[
    new MagicStatic(
        MagicLayer.Ability,
        CREATURE
    ) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            permanent.addAbility(TabernacleUpkeep);
        }
    }
]
