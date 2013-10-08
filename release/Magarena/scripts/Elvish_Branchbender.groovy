def PT = new MagicStatic(MagicLayer.SetPT, MagicStatic.UntilEOT) {
    @Override
    public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
        final int amount = source.getController().getNrOfPermanents(MagicSubType.Elf);
		pt.set(amount,amount);
    }
};
def ST = new MagicStatic(MagicLayer.Type, MagicStatic.UntilEOT) {
    @Override
    public int getTypeFlags(final MagicPermanent permanent,final int flags) {
        return flags|MagicType.Creature.getMask();
    }
};
def TT = new MagicStatic( MagicLayer.Type, MagicStatic.UntilEOT) {
	@Override
	public void modSubTypeFlags(final MagicPermanent permanent, final Set<MagicSubType> flags) {
		flags.add(MagicSubType.Treefolk);
	}
};
[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Animate),
        "Animate"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                new MagicTargetChoice("target Forest"),
                this,
                "Target land\$ becomes a X/X Treefolk creature until end of turn, " +
				"where X is the number of Elf creatures you control."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent land ->
                game.doAction(new MagicBecomesCreatureAction(land,PT,ST, TT));
            } as MagicPermanentAction);
        }
    }
]
