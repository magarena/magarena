[
    new TurnedFaceUpTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isFriend(permanent) && otherPermanent.hasType(MagicType.Creature)) ?
                new MagicEvent(
                    permanent,
                    otherPermanent,
                    this,
                    "RN gets +2/+2 until end of turn."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new ChangeTurnPTAction(event.getRefPermanent(),2,2));
        }
    }
]
