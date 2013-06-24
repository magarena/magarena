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
                    "SN deals RN damage to each creature defending player controls."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> creatures=
                    game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
            for (final MagicPermanent creature : creatures) {
                final MagicDamage damage = new MagicDamage(
                    event.getSource(),
                    creature,
                    event.getRefInt()
                );
                game.doAction(new MagicDealDamageAction(damage));
            }
        }
    }
]
