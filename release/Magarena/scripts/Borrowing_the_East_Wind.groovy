[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "SN deals X damage to each creature with horsemanship and each player. (X="+payedCost.getX()+")"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicSource source = event.getSource();
            final int amount = event.getCardOnStack().getX();
            game.filterPermanents(event.getPlayer(), CREATURE_WITH_HORSEMANSHIP) each {
                game.doAction(new MagicDealDamageAction(source, it, amount));
            }
            for (final MagicPlayer player : game.getAPNAP()) {
                game.doAction(new MagicDealDamageAction(source, player, amount));
            }
        }
    }
]
