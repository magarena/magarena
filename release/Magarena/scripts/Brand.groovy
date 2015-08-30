[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN gain control of all permanents he or she owns."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            PERMANENT_YOU_OWN.filter(event) each {
                game.doAction(new GainControlAction(event.getPlayer(), it));
            }
        }
    }
]
