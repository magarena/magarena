[
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            if  (otherPermanent.isArtifact() || otherPermanent.isLand()) {
                game.doAction(MagicTapAction.Enters(otherPermanent));
            }
            return MagicEvent.NONE;
        }
    }
]
