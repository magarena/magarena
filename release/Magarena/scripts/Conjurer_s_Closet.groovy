[
    new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPlayer eotPlayer) {
            return permanent.isController(eotPlayer) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        MagicTargetChoice.TARGET_CREATURE_YOU_CONTROL
                    ),
                    MagicBounceTargetPicker.create(),
                    this,
                    "You may\$ exile target creature\$ you control, then return " +
                    "that card to the battlefield under your control."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    game.doAction(new MagicRemoveFromPlayAction(
                        it,
                        MagicLocationType.Exile
                    ));
                    game.doAction(new MagicRemoveCardAction(
                        it.getCard(),
                        MagicLocationType.Exile
                    ));
                    game.doAction(new MagicPlayCardAction(
                        it.getCard(),
                        event.getPlayer()
                    ));
                });
            }
        }
    }
]
