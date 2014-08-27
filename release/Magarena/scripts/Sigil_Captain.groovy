[
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isCreature() &&
                    otherPermanent.isFriend(permanent)) ?
                new MagicEvent(
                    permanent,
                    otherPermanent,
                    this,
                    "if RN is 1/1. Put 2 +1/+1 counters on RN."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
			final 
			if (event.getRefPermanent.getPower()=1 && event.getRefPermanent.getToughness()=1){
				game.doAction(new MagicChangeCountersAction(event.getRefPermanent(),MagicCounterType.PlusOne,2));
			}
        }
    }
]