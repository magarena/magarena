[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_CREATURE,
                this,
                "Target creature\$ gets +X/+X until end of turn, "+
                "where X is 3 plus the number of cards named Muscle Burst in all graveyards."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = cardName("Muscle Burst")
                .from(MagicTargetType.Graveyard)
                .from(MagicTargetType.OpponentsGraveyard)
                .filter(event)
                .size()+3;
            event.processTargetPermanent(game, {
                game.logAppendX(event.getPlayer(),amount);
                game.doAction(new ChangeTurnPTAction(it,amount,amount));
            });
        }
    }
]
