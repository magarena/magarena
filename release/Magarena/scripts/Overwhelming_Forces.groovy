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
                final Collection<MagicPermanent> targets=
                    game.filterPermanents(it,CREATURE_YOU_CONTROL);
                final MagicDestroyAction destroy = new MagicDestroyAction(targets);
                game.doAction(destroy);
                game.doAction(new MagicDrawAction(event.getPlayer(),destroy.getNumDestroyed()));
            });
        }
    }
]
