[
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            if (otherPermanent.isEnemy(permanent) &&
                (otherPermanent.isArtifact() || otherPermanent.isCreature() || otherPermanent.isLand())) {
                game.doAction(TapAction.Enters(otherPermanent));
            }
            return MagicEvent.NONE;
        }
    }
]
