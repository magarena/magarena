[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return (permanent.isFriend(cardOnStack) &&
                    (cardOnStack.hasColor(MagicColor.White) ||
                     cardOnStack.hasColor(MagicColor.Blue) ||
                     cardOnStack.hasColor(MagicColor.Black) ||
                     cardOnStack.hasColor(MagicColor.Red))) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Put a +1/+1 counter on SN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeCountersAction(event.getPermanent(),MagicCounterType.PlusOne,1,true));
        }
    }
]
