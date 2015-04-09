[
   new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                this,
                "SN deals X damage to target creature\$ and you gain X life, "+
                "where X is 1 plus the number of cards named Feast of Flesh in all graveyards."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = game.filterCards(
                cardName("Feast of Flesh")
                .from(MagicTargetType.Graveyard)
                .from(MagicTargetType.OpponentsGraveyard)
            ).size()+1;
            event.processTargetPermanent(game, {
                game.logAppendMessage(event.getPlayer()," (X="+amount+")");
                game.doAction(new MagicDealDamageAction(event.getSource(),it,amount));
                game.doAction(new MagicChangeLifeAction(event.getPlayer(),amount));
            });
        }
    }
]
