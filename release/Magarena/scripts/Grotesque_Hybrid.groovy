[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.isSource(permanent) && damage.isTargetCreature() && damage.isCombat()) ?
                new MagicEvent(
                    permanent,
                    damage.getTarget(),
                    this,
                    "Destroy RN. It can't be regenerated."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processRefPermanent(game, {
                game.doAction(ChangeStateAction.Set(
                    it,
                    MagicPermanentState.CannotBeRegenerated
                ));
                game.doAction(new MagicDestroyAction(it));
            });
        }
    }
]
