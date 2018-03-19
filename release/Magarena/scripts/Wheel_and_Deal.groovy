def effect = MagicRuleEventAction.create("you may have target opponent discards his or her hand~and opponent draws seven cards.")


[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN draw a card."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new DrawAction(event.getPlayer()));
                game.addEvent(effect.getEvent(event));
        }
    }
]
