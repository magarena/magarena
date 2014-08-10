[
    new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer eotPlayer) {
            return permanent.getController().controlsPermanent(MagicType.Artifact) == false ?
                MagicRuleEventAction.create(permanent, "Sacrifice SN.") :
                MagicEvent.NONE;
        }
    }
]
