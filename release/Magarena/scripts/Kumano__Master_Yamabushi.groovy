[
   new DamageIsDealtTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return damage.isSource(permanent) && (damage.isTargetCreature()) ?
                new MagicEvent(
                    permanent,
                    damage.getTargetPermanent(),
                    this,
                    "If RN would die this turn, exile it instead."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
               game.doAction(new AddTurnTriggerAction(event.getRefPermanent(),ThisLeavesBattlefieldTrigger.IfDieExileInstead));
        }
    }
]
