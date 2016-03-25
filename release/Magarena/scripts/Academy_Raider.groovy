def EFFECT = MagicRuleEventAction.create("Draw a card.");

[
    new ThisCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Discard a card?"),
                this,
                "PN may\$ discard a card. If PN does, he or she draws a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicEvent discardEvent = new MagicDiscardEvent(event.getSource(), event.getPlayer())
            if (event.isYes() && discardEvent.isSatisfied()) {
                game.addEvent(discardEvent);
                game.addEvent(EFFECT.getEvent(event.getSource(), player, MagicEvent.NO_REF));
            }
        }
    }
]
