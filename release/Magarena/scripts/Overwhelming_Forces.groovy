[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_OPPONENT,
                this,
                "Destroy all creatures target opponent\$ controls. PN draws a card for each creature destroyed this way."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final DestroyAction destroy = new DestroyAction(CREATURE_YOU_CONTROL.filter(it));
                game.doAction(destroy);
                game.doAction(new DrawAction(event.getPlayer(),destroy.getNumDestroyed()));
            });
        }
    }
]
