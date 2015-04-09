def ST1 = new MagicStatic(MagicLayer.Ability) {
    @Override
    public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
        permanent.loseAllAbilities();
        permanent.addAbility(MagicTapManaActivation.Green);
    }
};

def ST2 = new MagicStatic(MagicLayer.Type) {
    @Override
    public void modSubTypeFlags(final MagicPermanent permanent,final Set<MagicSubType> flags) {
        flags.clear();
        flags.add(MagicSubType.Forest);
    }
}

def SACRIFICE_GREEN_CREATURE = new MagicTargetChoice("a green creature to sacrifice");


[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Forest"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicSacrificePermanentEvent(source,SACRIFICE_GREEN_CREATURE)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_LAND,
                this,
                "Target land\$ becomes a Forest."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicAddStaticAction(it, ST1));
                game.doAction(new MagicAddStaticAction(it, ST2));
            });
        }
    }
]
