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
            final int amount = cardName("Feast of Flesh")
                .from(MagicTargetType.Graveyard)
                .from(MagicTargetType.OpponentsGraveyard)
                .filter(event)
                .size()+1;
            event.processTargetPermanent(game, {
                game.logAppendX(event.getPlayer(),amount);
                game.doAction(new DealDamageAction(event.getSource(),it,amount));
                game.doAction(new ChangeLifeAction(event.getPlayer(),amount));
            });
        }
    }
]
