[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_OPPONENT,
                this,
                "SN deals X damage to target opponent\$, where X is the number of Mountains PN controls. SN deals half X damage, rounded up, to PN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = player.getNrOfPermanents(MagicSubType.Mountain);
            final int halfAmount = (int)Math.ceil(amount/2);
            event.processTarget(game, {
                game.doAction(new MagicDealDamageAction(event.getSource(),it,amount));
                game.doAction(new MagicDealDamageAction(event.getSource(),event.getPlayer(),halfAmount));
            });
        }
    }
]
