[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                TARGET_CREATURE_YOUR_OPPONENT_CONTROLS, 
                MagicExileTargetPicker.create(),
                this,
                "Exile target creature an opponent controls\$ until SN leaves the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                if (event.getPermanent().isValid()) {
                    game.doAction(new ExileLinkAction(event.getPermanent(), it));
                    game.doAction(new AddTriggerAction(event.getPermanent(), MagicWhenSelfLeavesPlayTrigger.ExileUntilLeaves));
                }
            });
        }
    }
]
