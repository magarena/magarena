[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_PLAYER,
                this,
                "Target player\$ draws three cards, loses 3 life, and gets three poison counters."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.doAction(new DrawAction(it, 3));
                game.doAction(new ChangeLifeAction(it, -3));
                game.doAction(new ChangeCountersAction(it, MagicCounterType.Poison, 3));
            });
        }
    }
]
