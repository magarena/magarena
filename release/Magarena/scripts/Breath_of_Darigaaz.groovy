[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "SN deals 1 damage to each creature without flying and each player. " +
                "If SN was kicked, it deals 4 damage to each creature without flying and each player instead."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.isKicked() ? 4 : 1;
            final MagicSource source = event.getSource();
            game.filterPermanents(event.getPlayer(), CREATURE_WITHOUT_FLYING) each {
                game.doAction(new MagicDealDamageAction(source, it, amount));
            }
            for (final MagicPlayer player : game.getAPNAP()) {
                game.doAction(new MagicDealDamageAction(source, player, amount));
            }
        }
    }
]
