[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "SN deals 2 damage to each other player. PN gains life equal to the damage dealt this way."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicDamage damage = new MagicDamage(event.getSource(),player.getOpponent(),2);
            game.doAction(new DealDamageAction(damage));
            game.doAction(new ChangeLifeAction(player,damage.getDealtAmount()));
        }
    }
]
