[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "SN deals 1 damage to each creature with flying your opponents control. Tap those creatures."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> creatures = game.filterPermanents(
                event.getPlayer(),
                MagicTargetFilterFactory.CREATURE_WITH_FLYING_YOUR_OPPONENT_CONTROLS
            );
            final MagicPermanent permanent = event.getPermanent();
            for (final MagicPermanent creature : creatures) {
                game.doAction(new MagicDealDamageAction(permanent,creature,1));
                game.doAction(new MagicTapAction(creature));
            }
        }
    }
]
