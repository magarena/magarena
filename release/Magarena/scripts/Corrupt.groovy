[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                this,
                "SN deals damage equal to the number of Swamps PN controls to target creature or player\$. PN gains life equal to the damage dealt this way."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPlayer().getNrOfPermanents(MagicSubType.Swamp);
            event.processTarget(game, {
                final MagicDamage damage = new MagicDamage(event.getSource(),it,amount);
                game.doAction(new MagicDealDamageAction(damage));
                game.doAction(new MagicChangeLifeAction(event.getPlayer(),damage.getDealtAmount()));
            });
        }
    }
]
