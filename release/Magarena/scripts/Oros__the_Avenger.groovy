[
    new MagicWhenSelfCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    new MagicPayManaCostChoice(MagicManaCost.create("{2}{W}"))
                ),
                this,
                "You may\$ pay {2}{W}\$. If you do, SN " +
                "deals 3 damage to each nonwhite creature."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                final Collection<MagicPermanent> targets=
                    game.filterPermanents(event.getPlayer(),MagicTargetFilterFactory.NONWHITE_CREATURE);
                for (final MagicPermanent target : targets) {
                    final MagicDamage damage=new MagicDamage(event.getPermanent(),target,3);
                    game.doAction(new MagicDealDamageAction(damage));
                }
            }
        }
    }
]
