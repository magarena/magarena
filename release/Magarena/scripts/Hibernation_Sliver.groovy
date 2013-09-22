
def HibernationBounce = new MagicPermanentActivation(
    new MagicActivationHints(MagicTiming.Removal),
    "Bounce"
) {

    @Override
    public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
        return [new MagicPayLifeEvent(source,2)];
    }

    @Override
    public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
        return new MagicEvent(
            source,
            this,
            "Return SN to its owner's hand."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {         
        game.doAction(new MagicRemoveFromPlayAction(
            event.getPermanent(),
            MagicLocationType.OwnersHand
        ));
    }
};

[
    new MagicStatic(
        MagicLayer.Ability,
        MagicTargetFilter.TARGET_SLIVER
    ) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {    
            permanent.addAbility(HibernationBounce);
        }
    }
]
