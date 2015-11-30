[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_PLAYER,
                payedCost.getTarget(),
                this,
                "SN deals damage equal to RN's power to target player\$. "+
                "PN gains life equal to the damage dealt this way."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final MagicPermanent sacrificed = event.getRefPermanent();
                final int amount = sacrificed.getPower();
                final MagicPlayer player = event.getPlayer();
                final MagicDamage damage = new MagicDamage(event.getSource(),it,amount)
                game.logAppendMessage(player,"(Power=${amount})");
                game.doAction(new DealDamageAction(damage));
                game.doAction(new ChangeLifeAction(player,damage.getDealtAmount()));
            });
        }
    }
]
