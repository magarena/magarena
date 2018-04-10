[
    new TurnedFaceUpTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (permanent != otherPermanent && otherPermanent.isFriend(permanent) && otherPermanent.hasType(MagicType.Creature)) ?
                new MagicEvent(
                    permanent,
                    otherPermanent,
                    this,
                    "PN puts two +1/+1 counters on RN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new ChangeCountersAction(event.getPlayer(),event.getRefPermanent(),MagicCounterType.PlusOne,2));
        }
    }
]
