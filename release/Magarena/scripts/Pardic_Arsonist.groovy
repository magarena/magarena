[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            return (MagicCondition.THRESHOLD_CONDITION.accept(permanent)) ?
                new MagicEvent(
                    permanent,
                    MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                    this,
                    "SN deals 3 damage to target creature or player."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final MagicTarget target ->
                final MagicDamage damage = new MagicDamage(event.getSource(),target,6);
                game.doAction(new MagicDealDamageAction(damage));
            });
        }
    }
]
