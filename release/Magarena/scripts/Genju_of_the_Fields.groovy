def PT = new MagicStatic(MagicLayer.SetPT, MagicStatic.UntilEOT) {
    @Override
    public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
        pt.set(2,5);
    }
};
def LC = new MagicStatic(MagicLayer.Color, MagicStatic.UntilEOT) {
    @Override
    public int getColorFlags(final MagicPermanent permanent,final int flags) {
        return MagicColor.White.getMask();
    }
};
def ST = new MagicStatic(MagicLayer.Type, MagicStatic.UntilEOT) {
    @Override
    public void modSubTypeFlags(final MagicPermanent permanent, final Set<MagicSubType> flags) {
        flags.add(MagicSubType.Spirit);
    }
    @Override
    public int getTypeFlags(final MagicPermanent permanent,final int flags) {
        return flags | MagicType.Creature.getMask();
    }
};
def LifeGainAbility = new DamageIsDealtTrigger() {
    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
        return (damage.getSource() == permanent) ?
            new MagicEvent(
                permanent,
                damage.getDealtAmount(),
                this,
                "PN gains RN life."
            ) :
            MagicEvent.NONE;
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new ChangeLifeAction(event.getPlayer(), event.getRefInt()));
    }
}
def AB = new MagicStatic(MagicLayer.Ability, MagicStatic.UntilEOT) {
    @Override
    public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
        permanent.addAbility(LifeGainAbility);
    }
};
[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Animate),
        "Becomes"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source,"{2}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                source.getEnchantedPermanent(),
                this,
                "Until end of turn, RN becomes a 2/5 white Spirit creature with 'Whenever this creature deals damage, its controller gains that much life.'" +
                "It's still a land."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new BecomesCreatureAction(event.getRefPermanent(),PT,AB,ST,LC));
        }
    },
    new OtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            return (permanent.getEnchantedPermanent() == died) ?
                new MagicEvent(
                    permanent,
                    permanent.getCard(),
                    this,
                    "Return RN to its owner's hand."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ShiftCardAction(
                event.getRefCard(),
                MagicLocationType.Graveyard,
                MagicLocationType.OwnersHand
            ));
        }
    }
]
