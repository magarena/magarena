[
    new OtherEntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            game.doAction(TapAction.Enters(otherPermanent));
            return MagicEvent.NONE;
        }
    }
]
