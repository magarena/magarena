[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE_OR_PLAYER,
                this,
                "SN deals X damage to target creature or player\$,"+
                "where X is 2 plus the number of cards named Flame Burst in all graveyards."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int flame = cardName("Flame Burst")
                .from(MagicTargetType.Graveyard)
                .from(MagicTargetType.OpponentsGraveyard)
                .filter(event)
                .size();
            final int pardic = cardName("Pardic Firecat")
                .from(MagicTargetType.Graveyard)
                .from(MagicTargetType.OpponentsGraveyard)
                .filter(event)
                .size();
            final int amount = flame + pardic + 2;
            event.processTarget(game, {
                game.logAppendX(event.getPlayer(),amount);
                game.doAction(new DealDamageAction(event.getSource(),it,amount));
            });
        }
    }
]
