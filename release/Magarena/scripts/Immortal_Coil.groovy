def YouLoseGame = MagicRuleEventAction.create("You lose the game.");

[
    new MagicStatic(MagicLayer.Game) {
        @Override
        public boolean condition(final MagicGame game, final MagicPermanent source, final MagicPermanent target) {
            return source.getController().getGraveyard().size() == 0;
        }

        @Override
        public void modGame(final MagicPermanent source, final MagicGame game) {
            game.doAction(new PutStateTriggerOnStackAction(
                YouLoseGame.getTriggerEvent(source)
            ));
        }
    },
    new IfDamageWouldBeDealtTrigger(MagicTrigger.REPLACE_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            int amount = permanent.isController(damage.getTarget()) ? damage.prevent() : 0;
            return amount > 0 ?
                new MagicEvent(
                    permanent,
                    amount,
                    this,
                    "Prevent RN damage and PN exiles RN cards from his or her graveyard."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicRepeatedCostEvent(
                event.getSource(),
                event.getPlayer(),
                MagicTargetChoice.A_CARD_FROM_GRAVEYARD,
                event.getRefInt(),
                MagicChainEventFactory.ExileCard
            ));
        }
    }
]
