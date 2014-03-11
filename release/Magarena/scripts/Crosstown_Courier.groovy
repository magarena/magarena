[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final int amount = damage.getDealtAmount();
            return (amount > 0 &&
                    damage.getSource() == permanent &&
                    damage.getTarget().isPlayer() &&
                    damage.isCombat()) ?
                new MagicEvent(
                    permanent,
                    damage.getTargetPlayer(),
                    amount,
                    this,
                    "PN puts the top RN cards into PN's graveyard."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicMillLibraryAction(event.getPlayer(),event.getRefInt()));
        }
    }
]
