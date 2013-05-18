[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
            return new MagicEvent(
                permanent,
                player,
                this,
                "SN deals 4 damage to each other creature."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicSource permanent = event.getPermanent();
            final Collection<MagicPermanent> creatures =
                game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_CREATURE);
            for (final MagicPermanent creature : creatures) {
                if (permanent != creature) {
                    final MagicDamage damage=new MagicDamage(permanent,creature,4);
                    game.doAction(new MagicDealDamageAction(damage));
                }
            }
        }
    }
]
