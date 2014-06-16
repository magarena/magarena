[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                MagicTargetChoice.TARGET_NONLAND_PERMANENT_YOUR_OPPONENT_CONTROLS, 
                MagicExileTargetPicker.create(),
                this,
                "Exile target nonland permanent an opponent controls\$ until SN leaves the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent creature ->
                if (event.getPermanent().isValid()) {
                    game.doAction(new MagicExileLinkAction(event.getPermanent(), creature));
                    game.doAction(new MagicAddTriggerAction(event.getPermanent(), MagicWhenSelfLeavesPlayTrigger.ExileUntilLeaves));
                }
            });
        }
    }
]
