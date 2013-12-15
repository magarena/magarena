[
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            
            return (otherPermanent != permanent &&
                    otherPermanent.isCreature() &&
                    otherPermanent.isFriend(permanent) &&
                    otherPermanent.hasColor(MagicColor.Green)) ?
                new MagicEvent(
                    permanent,
                    MagicTargetChoice.Positive("target creature"),
                    this,
                    "Put a +1/+1 counter on target creature\$."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent target) {
                    game.doAction(new MagicChangeCountersAction(
                        target,
                        MagicCounterType.PlusOne,
                        1,
                        true
                    ));
                }
            });
        }
    }
]
