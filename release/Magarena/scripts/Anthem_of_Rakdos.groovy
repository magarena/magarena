[
    new MagicWhenAttacksTrigger(1) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            return attacker.isFriend(permanent) ?
                new MagicEvent(
                    permanent,
                    attacker,
                    this,
                    "RN gets +2/+0 until end of turn and SN deals 1 damage to PN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeTurnPTAction(event.getRefPermanent(),2,0));
            game.doAction(new MagicDealDamageAction(event.getSource(), event.getPlayer(), 1));
        }
    },
    
    new MagicIfDamageWouldBeDealtTrigger(MagicTrigger.INCREASE_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.getSource().isFriend(permanent) && MagicCondition.HELLBENT.accept(damage.getSource())) {
                damage.setAmount(damage.getAmount() * 2);
            }
            return MagicEvent.NONE;
        }
    }
]
