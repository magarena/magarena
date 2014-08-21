def EFFECT = MagicRuleEventAction.create("Sacrifice SN.");

[
    new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer eotPlayer) {
            return permanent.getController().controlsPermanent(MagicType.Artifact) == false ?
                EFFECT.getEvent(permanent) :
                MagicEvent.NONE;
        }
    }
]
