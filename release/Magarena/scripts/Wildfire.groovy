[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Each player sacrifices 4 lands. SN deals 4 damage to each creature."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets = game.filterPermanents(MagicTargetFilterFactory.CREATURE);
            final MagicSource source = event.getSource();
            for (final MagicPermanent target : targets) {
                final MagicDamage damage=new MagicDamage(source,target,4);
                game.doAction(new MagicDealDamageAction(damage));
            }
            for (final MagicPlayer player : game.getAPNAP()) {
                game.addEvent(new MagicSacrificePermanentEvent(event.getSource(),player,MagicTargetChoice.SACRIFICE_LAND));
        game.addEvent(new MagicSacrificePermanentEvent(event.getSource(),player,MagicTargetChoice.SACRIFICE_LAND));
        game.addEvent(new MagicSacrificePermanentEvent(event.getSource(),player,MagicTargetChoice.SACRIFICE_LAND));
        game.addEvent(new MagicSacrificePermanentEvent(event.getSource(),player,MagicTargetChoice.SACRIFICE_LAND));
        }
        }
    }
]
