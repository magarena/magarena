def trigger = new DamageIsDealtTrigger() {
    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
        final MagicPlayer player = permanent.getController();
        final MagicTarget target = damage.getTarget();
        if (player == target && player.getLife() < 7) {
            player.setLife(7);
        }
        return MagicEvent.NONE;
    }
}

[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {      
            return (permanent.getController().getLife() < 7) ?
                new MagicEvent(
                    permanent,
                    this,
                    "If PN's life total is less than 7, PN's life total becomes 7."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int life = player.getLife();
            if (life < 7) {
                game.doAction(new ChangeLifeAction(player, 7 - life)) 
            }
        }
    },
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            if (permanent.getController().getLife() >= 7) {
                permanent.addAbility(trigger);
            }
        }
    }
]
