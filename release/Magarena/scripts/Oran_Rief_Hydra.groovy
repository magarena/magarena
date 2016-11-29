[
    new OtherEntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            final int amount = otherPermanent.hasSubType(MagicSubType.Forest) ? 2 : 1;
            return (otherPermanent.isLand() && otherPermanent.isFriend(permanent)) ?
                new MagicEvent(
                    permanent,
                    amount,
                    this,
                    "PN puts RN +1/+1 counter(s) on SN."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeCountersAction(
                event.getPermanent(),
                MagicCounterType.PlusOne,
                event.getRefInt()
            ));
        }
    }
]
