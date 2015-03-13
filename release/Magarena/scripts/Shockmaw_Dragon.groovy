[
    new MagicWhenSelfCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return new MagicEvent(
                permanent,
                damage.getTargetPlayer(),
                this,
                "SN deals 1 damage to each creature RN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> creatures =
            game.filterPermanents(event.getRefPlayer(),MagicTargetFilterFactory.CREATURE_YOU_CONTROL);
            for (final MagicPermanent creature : creatures) {
                final MagicDamage damage = new MagicDamage(event.getSource(),creature,1);
                game.doAction(new MagicDealDamageAction(damage));
            }
        }
    }
]
