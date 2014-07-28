[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_OPPONENT,
                this,
                "SN deals X damage to target opponent\$, where X is the number of Mountains PN controls. SN deals half X damage, rounded up, to PN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = player.getNrOfPermanents(MagicSubType.Mountain);
            event.processTarget(game, {
                final MagicDamage damage = new MagicDamage(
                    event.getSource(),
                    it,
                    amount
                );
                final MagicDamage damage2 = new MagicDamage(
                    event.getSource(),
                    event.getPlayer(),
                    (int)Math.ceil(amount/2)
                );
                game.doAction(new MagicDealDamageAction(damage));
                game.doAction(new MagicDealDamageAction(damage2));
            });
        }
    }
]
