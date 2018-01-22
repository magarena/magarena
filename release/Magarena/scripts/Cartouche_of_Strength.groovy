[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(TARGET_CREATURE_YOUR_OPPONENT_CONTROLS),
                permanent.getEnchantedPermanent(),
                this,
                "PN may\$ have ${permanent.getEnchantedPermanent()} fight target creature an opponent controls\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final int enchantedPower = event.getRefPermanent().getPower();
                final int targetPower = it.getPower();
                game.doAction(new DealDamageAction(enchanted, it, enchantedPower));
                game.doAction(new DealDamageAction(it, enchanted, targetPower));
            });
        }
    }
]

