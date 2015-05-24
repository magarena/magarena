[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_PLAYER,
                this,
                "Target player\$ gains 4 life, then 4 life for,"+
                "each card named Life Burst in all graveyards."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = cardName("Life Burst")
                .from(MagicTargetType.Graveyard)
                .from(MagicTargetType.OpponentsGraveyard)
                .filter(event)
                .size()*4;
            event.processTargetPlayer(game, {
                game.doAction(new ChangeLifeAction(it,4));
                game.logAppendValue(event.getPlayer(),amount);
                game.doAction(new ChangeLifeAction(it,4*amount));
            });
        }
    }
]
