def MUTANT = new MagicStatic(MagicLayer.Type) {
    @Override
    public void modSubTypeFlags(final MagicPermanent permanent,final Set<MagicSubType> flags) {
        flags.add(MagicSubType.Mutant);
    }
};
[
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent otherPermanent) {
            return(otherPermanent != permanent &&
                otherPermanent.isCreature() &&
                otherPermanent.isFriend(permanent))?
                new MagicEvent(
                    permanent,
                    otherPermanent,
                    this,
                    "RN enters the battelfied with a number of +1/+1 counters equal to the power of PN," + 
                    "and a Mutant in adddition to its other types."
                ) : 
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeCountersAction(event.getRefPermanent(),MagicCounterType.PlusOne,event.getPermanent().getPower()));
            game.doAction(new MagicAddStaticAction(event.getRefPermanent(),MUTANT));
        }
    }    
]
