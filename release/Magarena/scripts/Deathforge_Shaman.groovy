[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
            final MagicGame game,
            final MagicPermanent permanent,
            final MagicPlayer player) {   
            return permanent.isKicked() ?
                new MagicEvent(
                    permanent,
                    player,
                    MagicTargetChoice.NEG_TARGET_PLAYER,
                    this,
                    "SN deals damage to target player\$ equal to twice the number of times it was kicked."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game,new MagicPlayerAction() {
                public void doAction(final MagicPlayer player) {
                    final MagicDamage damage = new MagicDamage(
                        event.getSource(),
                        player,
                        event.getPermanent().getKicker() * 2
                    );
                    game.doAction(new MagicDealDamageAction(damage));
                }
            });
        }
    }
]
