[
    new ThisAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(),
                this,
                "PN may\$ have SN deal 1 damage " +
                "to each creature defending player controls."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                final MagicSource source = event.getSource();
                CREATURE_YOU_CONTROL.filter(game.getDefendingPlayer()) each {
                    game.doAction(new DealDamageAction(source,it,1));
                }
            }
        }
    }
]
