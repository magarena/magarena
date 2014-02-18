[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "SN deals 1 damage to each creature PN's opponents control. Scry 1."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets =
                game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_CREATURE_YOUR_OPPONENT_CONTROLS);
            for (final MagicPermanent target : targets) {
                final MagicDamage damage = new MagicDamage(event.getSource(),target,1);
                game.doAction(new MagicDealDamageAction(damage));
            };
            game.addEvent(new MagicScryEvent(event));
        }
    }
]
