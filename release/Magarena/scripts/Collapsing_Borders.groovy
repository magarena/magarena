[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                upkeepPlayer,
                this,
                "PN gains 1 life for each basic land type among lands he or she controls. ("+upkeepPlayer.getDomain()+") "+
                "Then Collapsing Borders deals 3 damage to him or her."
            )
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer()
            final MagicDamage damage = new MagicDamage(event.getSource(),player,3);
            game.doAction(new MagicChangeLifeAction(player,player.getDomain()));
            game.doAction(new MagicDealDamageAction(damage));
        }
    }
]
