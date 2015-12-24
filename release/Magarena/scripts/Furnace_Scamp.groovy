[
    new ThisCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(),
                damage.getTarget(),
                this,
                "PN may\$ sacrifice SN. If he or she does, SN deals 3 damage to RN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent it = event.getPermanent();
            if (event.isYes() && it.isValid()) {
                game.doAction(new SacrificeAction(it));
                game.doAction(new DealDamageAction(it, event.getRefPlayer(), 3));
            }
        }
    }
]
