def PT = new MagicStatic(MagicLayer.SetPT, MagicStatic.UntilEOT) {
    @Override
    public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
        pt.set(1,4);
    }
};

def C = new MagicStatic(MagicLayer.Color,MagicStatic.UntilEOT) {
    @Override
    public int getColorFlags(final MagicPermanent perm, final int flags) {
        return MagicColor.Blue.getMask() | MagicColor.Red.getMask();
    }
}

def ST = new MagicStatic(MagicLayer.Type, MagicStatic.UntilEOT) {
    @Override
    public void modSubTypeFlags(final MagicPermanent permanent, final Set<MagicSubType> flags) {
        flags.add(MagicSubType.Elemental);
    }
    @Override
    public int getTypeFlags(final MagicPermanent permanent,final int flags) {
        return flags | MagicType.Creature.getMask()
    }
};

def ability = MagicPermanentActivation.create(
    "{0}: Switch this creature's power and toughness until end of turn. " +
    "Activate this ability with AI no more than three times each turn."
);

def AB = new MagicStatic(MagicLayer.Ability, MagicStatic.UntilEOT) {
    @Override
    public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
        permanent.addAbility(ability);
    }
};

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Animate),
        "Becomes"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{2}{U}{R}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Until end of turn, SN becomes a 1/4 blue and red Elemental creature with " + 
                "\"{0}: Switch this creature's power and toughness until end of turn.\" It's still a land."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new BecomesCreatureAction(event.getPermanent(), PT, C, ST, AB));
        }
    }
]
