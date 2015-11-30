[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE_OR_PLAYER,
                payedCost.getTarget(),
                this,
                "SN deals damage equal to RN's power to target creature or player\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final MagicPermanent sacrificed = event.getRefPermanent();
                final int amount = sacrificed.getPower();
                game.logAppendValue(event.getPlayer(),amount);
                game.doAction(new DealDamageAction(event.getSource(),it,amount));
            });
        }
    }
]
