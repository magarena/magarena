[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getSource() == permanent &&
                    damage.getTarget().isPlayer()) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN may put a Goblin permanent card from " +
                    "his or her hand onto the battlefield."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicPutOntoBattlefieldEvent(
                event,
                new MagicMayChoice(
                    "Put a Goblin card onto the battlefield?",
                    MagicTargetChoice.GOBLIN_CARD_FROM_HAND
                )
            ));
        }
    }
]
