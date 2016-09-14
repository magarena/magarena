def PT = new MagicStatic(MagicLayer.SetPT, MagicStatic.UntilEOT) {
    @Override
    public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
        final int cmc = permanent.getConvertedCost();
        pt.set(cmc,cmc);
    }
};
def ST = new MagicStatic(MagicLayer.Type, MagicStatic.UntilEOT) {
    @Override
    public int getTypeFlags(final MagicPermanent permanent,final int flags) {
        return flags|MagicType.Artifact.getMask()|MagicType.Creature.getMask();
    }
};
def TP = new MagicTargetPicker<MagicPermanent>() {
    @Override
    protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
        final MagicPowerToughness pt=permanent.getPowerToughness();
        final int power = permanent.getConvertedCost();
        final int toughness = permanent.getConvertedCost();
        final int score = (pt.power()-power)*2+(pt.toughness()-toughness);
        return permanent.getController() == player ? -score:score;
    }
};

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Animate),
        "Becomes"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source,"{U}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                POS_TARGET_NONCREATURE_ARTIFACT,
                TP,
                this,
                "Target noncreature artifact\$ becomes an artifact creature with " +
                "power and toughness each equal to its converted mana cost until end of turn"
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new BecomesCreatureAction(it,PT,ST));
            });
        }
    }
]
