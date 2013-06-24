[
    new MagicWhenTargetedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicItemOnStack target) {
            final MagicPermanent equippedCreature = permanent.getEquippedCreature();
            return (target.containsInChoiceResults(equippedCreature) &&
                    target.isSpell()) ?
                new MagicEvent(
                    equippedCreature,
                    MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                    new MagicDamageTargetPicker(2),
                    this,
                    "SN deals 2 damage to target creature or player\$."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game,new MagicTargetAction() {
                public void doAction(final MagicTarget target) {
                    final MagicDamage damage=new MagicDamage(event.getSource(),target,2);
                    game.doAction(new MagicDealDamageAction(damage));
                }
            });
        }
    }
]
