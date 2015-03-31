[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_OPPONENT,
                this,
                "SN deals X damage to target opponent\$, where X is twice the number of white creatures that player controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final int amount = it.getNrOfPermanents(MagicTargetFilterFactory.WHITE_CREATURE) * 2;
                game.logAppendMessage(event.getPlayer()," (X="+amount+")");
                if (amount>0) {
                    game.doAction(new MagicDealDamageAction(event.getSource(),it,amount));
                }
            });
        }
    }
]
