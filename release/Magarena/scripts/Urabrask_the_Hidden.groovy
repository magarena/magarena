[
    new OtherEntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            if (otherPermanent.isCreature() &&
                otherPermanent.getController()!=permanent.getController()) {
                game.addEvent(new MagicTapEvent(otherPermanent));
            }
            return MagicEvent.NONE;
        }
    }
]
