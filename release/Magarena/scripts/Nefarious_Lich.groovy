[

    new IfLifeWouldChangeTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final ChangeLifeAction act) {
            int amount = 0;
            if (permanent.isController(act.getPlayer()) && act.getLifeChange() > 0) {
                amount = act.getLifeChange();
                act.setLifeChange(0);
                return new MagicEvent(
                    permanent,
                    amount,
                    this,
                    "PN draws RN cards."
                );
            } else {
                return MagicEvent.NONE;
            }
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DrawAction(event.getPlayer(), event.getRefInt()));
        }
    },

    new IfLifeWouldChangeTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final ChangeLifeAction act) {
            final int amount = Math.abs(act.getLifeChange());
            if (permanent.isController(act.getPlayer()) && act.getLifeChange() < 0 && act.isDamage()) {
                act.setLifeChange(0);
                return new MagicEvent(
                    permanent,
                    amount,
                    this,
                    "PN exiles RN cards from his or her graveyard."
                );
            } else {
                return MagicEvent.NONE;
            }
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicEvent exile = new MagicRepeatedCostEvent(
                event.getSource(),
                new MagicTargetChoice("a card from your graveyard"),
                event.getRefInt(),
                MagicChainEventFactory.ExileCard
            );
            if (exile.isSatisfied()) {
                game.addEvent(exile);
            } else {
                game.doAction(new LoseGameAction(event.getPlayer()," lost the game because of not being able to exile enough cards from his or her graveyard."))
            }
        }
    }
]
