[
    new MagicWhenSelfCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Sacrifice Whispering Specter?"),
                damage.getTarget(),
                this,
                "PN may\$ sacrifice SN. If he or she does, RN discards a card for each poison counter he or she has."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new SacrificeAction(event.getPermanent()));
                final int amount = event.getRefPlayer().getPoison();
                game.logAppendValue(event.getPlayer(), amount);
                if (amount > 0) {
                    game.addEvent(new MagicDiscardEvent(event.getPermanent(), event.getRefPlayer(), amount));
                }
            }
        }
    }
]
