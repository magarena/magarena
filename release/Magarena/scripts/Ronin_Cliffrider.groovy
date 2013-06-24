[
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (permanent == creature) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(),
                    this,
                    "PN may\$ have SN deal 1 damage " +
                    "to each creature defending player controls."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                final MagicSource source = event.getSource();
                final MagicPlayer defendingPlayer = event.getPlayer().getOpponent();
                final Collection<MagicPermanent> creatures =
                        game.filterPermanents(defendingPlayer,MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
                for (final MagicPermanent creature : creatures) {
                    final MagicDamage damage = new MagicDamage(source,creature,1);
                    game.doAction(new MagicDealDamageAction(damage));
                }
            }
        }
    }
]
