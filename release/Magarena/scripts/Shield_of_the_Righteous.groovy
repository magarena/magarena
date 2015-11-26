[
    new BlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            final MagicPermanent equippedCreature = permanent.getEquippedCreature();
            final MagicPermanent blocked = equippedCreature.getBlockedCreature();
            return (equippedCreature == blocked && blocked.isValid()) ?
                new MagicEvent(
                    permanent,
                    blocked,
                    this,
                    "RN doesn't untap during its controller's next untap step."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(ChangeStateAction.Set(
                event.getRefPermanent(),
                MagicPermanentState.DoesNotUntapDuringNext
            ));
        }
    }
]
