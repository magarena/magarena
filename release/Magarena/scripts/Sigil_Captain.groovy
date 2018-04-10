[
    new OtherEntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isCreature() &&
                    otherPermanent.isFriend(permanent) &&
                    otherPermanent.getPower() == 1 &&
                    otherPermanent.getToughness() == 1) ?
                new MagicEvent(
                    permanent,
                    otherPermanent,
                    this,
                    "If RN is 1/1, put two +1/+1 counters on it."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent creature = event.getRefPermanent();
            if (creature.getPower() == 1 && creature.getToughness() == 1) {
                game.doAction(new ChangeCountersAction(event.getPlayer(),creature,MagicCounterType.PlusOne,2));
            }
        }
    }
]
