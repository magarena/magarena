[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "SN deals 2 damage to each creature with flying and each player."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicSource source=event.getSource();
            final Collection<MagicPermanent> creatures=
                game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_CREATURE_WITH_FLYING);
            for (final MagicPermanent creature : creatures) {
                final MagicDamage damage=new MagicDamage(source,creature,2);
                game.doAction(new MagicDealDamageAction(damage));
            }
            for (final MagicPlayer player : game.getPlayers()) {
                final MagicDamage damage=new MagicDamage(source,player,2);
                game.doAction(new MagicDealDamageAction(damage));
            }
        }
    }
]
