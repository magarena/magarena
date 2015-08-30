def ATTACKING_KITHKIN = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isAttacking() &&
                   target.hasSubType(MagicSubType.Kithkin);
        }
}
[
    new MagicWhenSelfAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent attacker) {
            return new MagicEvent(
                permanent,
                attacker,
                this,
                "SN gets +1/+1 until end of turn for each other attacking Kithkin."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicPermanent permanent = event.getPermanent();
            final int amount = ATTACKING_KITHKIN.except(event.getPermanent()).filter(player).size()
            game.doAction(new ChangeTurnPTAction(event.getPermanent(), amount, amount));
            
        }
    }
]
