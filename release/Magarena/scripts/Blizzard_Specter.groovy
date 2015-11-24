[
    new ThisCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return new MagicEvent(
                permanent,
                new MagicOrChoice(
                    MagicChoice.NONE,
                    MagicChoice.NONE
                ),
                damage.getTarget(),
                this,
                "Choose one\$ — (1) RN discards a card; or (2) RN returns a permanent he or she controls to its owner's hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isMode(1)) {
                game.addEvent(new MagicDiscardEvent(
                    event.getSource(),
                    event.getRefPlayer()
                ));
            } else if (event.isMode(2)) {
                game.addEvent(new MagicBounceChosenPermanentEvent(
                    event.getSource(), 
                    event.getRefPlayer(), 
                    A_PERMANENT_YOU_CONTROL
                ));
            }
        }
    }
]
