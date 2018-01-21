[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(TARGET_CREATURE_YOUR_OPPONENT_CONTROLS),
                this,
                "PN may\$ have ${permanent.getEnchantedPermanent()} fight target creature an opponent controls.\$"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent enchanted = event.getSource().getEnchantedPermanent();
                game.doAction(new DealDamageAction(enchanted, it, enchanted.getPower()));
                game.doAction(new DealDamageAction(it, enchanted, it.getPower()));
            });
        }
    }
]
