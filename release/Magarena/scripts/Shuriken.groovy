def public MagicPermanentActivation ThrowIt(final MagicPermanent source) {
    return new MagicPermanentActivation(new MagicActivationHints(MagicTiming.Removal),"Damage"){
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent permanent) {
            return [new MagicTapEvent(permanent)];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent permanent,final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicTargetChoice(CREATURE,"target creature."),
                source,
                this,
                "Unattach RN from SN, RN deals 2 damage to target creature\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event){
            game.doAction(new AttachAction(source, MagicPermanent.NONE));
            event.processTarget(game,{
                game.doAction(new MagicDealDamageAction(source,it,2));
                if (!event.getPermanent().hasSubType(MagicSubType.Ninja)) {
                    game.doAction(new MagicGainControlAction(it.getController(),source,MagicStatic.Forever));
                }
            });
        }   
    }
};
[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            permanent.addAbility(ThrowIt(source));
        }
        @Override
        public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) { 
            return MagicStatic.acceptLinked(game,source,target);
        }
    }
]
