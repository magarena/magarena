[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {  
            final int amount = damage.getDealtAmount();
            return (damage.getTarget() == permanent) ?
                new MagicEvent(
                    permanent,
                    MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                    new MagicDamageTargetPicker(amount),
                    amount,
                    this,
                    "SN deals " + amount + " damage to target creature or player\$."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            event.processTarget(game,choiceResults,0,new MagicTargetAction() {
                public void doAction(final MagicTarget target) {
                    final MagicDamage damage=new MagicDamage(event.getSource(),target,event.getRefInt(),false);
                    game.doAction(new MagicDealDamageAction(damage));
                }
            });
        }
    }
]
