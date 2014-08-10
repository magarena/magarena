[
    new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPlayer eotPlayer) {
            return (permanent.isController(eotPlayer) &&
                    eotPlayer.controlsPermanent(MagicSubType.Demon) == false) ?
                MagicRuleEventAction.create(permanent, "Sacrifice SN.") :
                MagicEvent.NONE;
        }
    }
]
