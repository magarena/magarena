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
            event.processTarget(game, {
                final int amount = player.getNrOfPermanents(MagicSubType.Mountain);
                game.doAction(new DealDamageAction(event.getSource(),it,amount));
                
                def halfAmount = (amount + 1).intdiv(2);
                game.doAction(new DealDamageAction(event.getSource(),event.getPlayer(),halfAmount));
            });
        }
    }
]
