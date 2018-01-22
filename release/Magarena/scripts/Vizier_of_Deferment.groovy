[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(TARGET_CREATURE),
                this,
                "PN may\$ exile target creature\$ if it attacked or blocked this turn. " +
                "Return that card to the battlefield under its owner's control a the beginning of the next end step."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    game.doAction(new ExileUntilEndOfTurnAction(it));
                });
            }
        }
    }
]

