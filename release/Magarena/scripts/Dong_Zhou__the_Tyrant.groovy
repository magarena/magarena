[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                MagicTargetChoice.TARGET_CREATURE_YOUR_OPPONENT_CONTROLS,
                MagicPowerTargetPicker.create(),
                this,
                "Target creature an opponent controls\$ deals damage equal to its power to that player."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent creature ->
                final MagicDamage damage = new MagicDamage(
                    creature,
                    creature.getController(),
                    creature.getPower()
                );
                game.doAction(new MagicDealDamageAction(damage));
            });
        }
    }
]
