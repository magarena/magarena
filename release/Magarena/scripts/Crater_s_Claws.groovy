[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE_OR_PLAYER,
                this,
                "SN deals X damage to target creature or player. " +
                "SN deals X plus 2 damage to that creature or player instead if you control a creature with power 4 or greater."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getCardOnStack().getX();
            event.processTarget(game, {
                if (event.getPlayer().controlsPermanent(CREATURE_POWER_4_OR_MORE)) {

                    game.doAction(new DealDamageAction(event.getSource(),it,amount + 2));
                } else {
                    game.doAction(new DealDamageAction(event.getSource(),it,amount));
                }
            });
        }
    }
]
