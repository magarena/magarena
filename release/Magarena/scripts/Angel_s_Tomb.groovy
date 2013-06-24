def PT = new MagicStatic(
        MagicLayer.SetPT,
        MagicStatic.UntilEOT) {

    @Override
    public void modPowerToughness(
            final MagicPermanent source,
            final MagicPermanent permanent,
            final MagicPowerToughness pt) {
        pt.set(3, 3);
    }
};
def AB = new MagicStatic(
        MagicLayer.Ability,
        MagicStatic.UntilEOT) {

    @Override
    public void modAbilityFlags(
            final MagicPermanent source,
            final MagicPermanent permanent,
            final Set<MagicAbility> flags) {
        flags.add(MagicAbility.Flying);
    }
};
def ST = new MagicStatic(
        MagicLayer.Type,
        MagicStatic.UntilEOT) {

    @Override
    public void modSubTypeFlags(
            final MagicPermanent permanent,
            final Set<MagicSubType> flags) {
        flags.add(MagicSubType.Angel);
    }

    @Override
    public int getTypeFlags(final MagicPermanent permanent, final int flags) {
        return flags | MagicType.Creature.getMask();
    }
};
def C = new MagicStatic(
        MagicLayer.Color,
        MagicStatic.UntilEOT) {

    @Override
    public int getColorFlags(final MagicPermanent permanent, final int flags) {
        return MagicColor.White.getMask();
    }
};
[
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPermanent otherPermanent) {
            return (otherPermanent.isFriend(permanent) &&
                    otherPermanent.isCreature()) ?
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(
                        MagicSimpleMayChoice.BECOME_CREATURE,
                        0,
                        MagicSimpleMayChoice.DEFAULT_YES
                    ),
                    this,
                    "PN may\$ have SN become a 3/3 white " +
                    "Angel artifact creature with flying until end of turn."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicBecomesCreatureAction(
                    event.getPermanent(),
                    PT,
                    AB,
                    ST,
                    C
                ));
            }
        }
    }
]
