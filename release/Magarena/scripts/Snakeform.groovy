def PT = new MagicStatic(MagicLayer.SetPT, MagicStatic.UntilEOT) {
    @Override
    public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
        pt.set(1,1);
    }
};
def AB = new MagicStatic(MagicLayer.Ability, MagicStatic.UntilEOT) {
    @Override
    public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
        permanent.loseAllAbilities();
    }
};
def ST = new MagicStatic(MagicLayer.Type, MagicStatic.UntilEOT) {
    @Override
    public void modSubTypeFlags(final MagicPermanent permanent,final Set<MagicSubType> flags) {
        flags.removeAll(MagicSubType.ALL_CREATURES);
        flags.add(MagicSubType.Snake);
    }
};
def C = new MagicStatic(MagicLayer.Color, MagicStatic.UntilEOT) {
    @Override
    public int getColorFlags(final MagicPermanent permanent,final int flags) {
        return MagicColor.Green.getMask();
    }
};
[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE,
                new MagicBecomeTargetPicker(1,1,false),
                this,
                "Target creature\$ loses all abilities " +
                "and becomes a 1/1 green Snake until end of turn. PN draws a card"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                //Does not lose static
                game.doAction(new MagicBecomesCreatureAction(it,PT,AB,ST,C));
                game.doAction(new MagicDrawAction(event.getPlayer()));
            });
        }
    }
]
