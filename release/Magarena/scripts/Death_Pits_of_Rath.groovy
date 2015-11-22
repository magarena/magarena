[
    new DamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicTarget target = damage.getTarget();
            return target.isCreature() ?
                new MagicEvent(
                    permanent,
                    target,
                    this,
                    "Destroy RN. It can't be regenerated."
                ): 
                MagicEvent.NONE;
        }
        @Override       
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent creature = event.getRefPermanent();
            game.doAction(ChangeStateAction.Set(creature,MagicPermanentState.CannotBeRegenerated));
            game.doAction(new DestroyAction(creature));
        }
    }
]
