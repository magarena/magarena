[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_PLAYER,
                this,
                "Target player\$ discards X cards, where X is one plus "+
                "the number of cards named Mind Burst in all graveyards."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = cardName("Mind Burst")
                .from(MagicTargetType.Graveyard)
                .from(MagicTargetType.OpponentsGraveyard)
                .filter(event)
                .size()+1;
            event.processTargetPlayer(game, {
                game.logAppendMessage(event.getPlayer()," (X="+amount+")");
                game.addEvent(new MagicDiscardEvent(event.getSource(),it,amount));
            });
        }
    }
]
