def T = new MagicStatic(MagicLayer.Type, NONCREATURE_ARTIFACT) {
    @Override
    public int getTypeFlags(final MagicPermanent permanent, final int flags) {
        return flags|MagicType.Artifact.getMask()|MagicType.Creature.getMask();
    }
}

def PT = new MagicStatic(MagicLayer.SetPT, ANY) {
    @Override
    public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
        return target.applied(source, T);
    }
    @Override
    public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
        final int cmc = permanent.getConvertedCost();
        pt.set(cmc,cmc);
    }
}

[T, PT]
