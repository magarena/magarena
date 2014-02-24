[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{R}"),
                new MagicSacrificeEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "SN deals 6 damage to each creature and each player."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets =
                game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_CREATURE);
            for (final MagicPermanent target : targets) {
                final MagicDamage damage=new MagicDamage(event.getSource(),target,6);
                game.doAction(new MagicDealDamageAction(damage));
            }
            for (final MagicPlayer player : game.getPlayers()) {
                final MagicDamage damage=new MagicDamage(event.getSource(),player,6);
                game.doAction(new MagicDealDamageAction(damage));
            }
        }
    }
]
