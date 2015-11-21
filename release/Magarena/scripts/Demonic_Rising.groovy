def effect = MagicRuleEventAction.create("Put a 5/5 black Demon creature token with flying onto the battlefield.");

[
    new AtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer eotPlayer) {
            return (permanent.isController(eotPlayer) && eotPlayer.getNrOfPermanents(MagicType.Creature) == 1) ?
                effect.getEvent(permanent):
                MagicEvent.NONE;
        }
    }
]
