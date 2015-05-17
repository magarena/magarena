[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "SN deals damage to each player equal to twice the number of creatures with flying that player controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.getAPNAP() each {
                final int amount = CREATURE_WITH_FLYING_YOU_CONTROL.filter(it).size() * 2;
                game.doAction(new DealDamageAction(event.getSource(), it, amount));
                game.logAppendMessage(event.getPlayer(), it.getName()+" is dealt "+amount+" damage.");
            }
        }
    }
]
