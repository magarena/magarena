[
    new MagicWhenAttacksUnblockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (creature == permanent) ?
                new MagicEvent(
                    permanent,
                    this,
                    "SN assigns no combat damage this turn and defending player loses 2 life."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(ChangeStateAction.Set(
                event.getPermanent(),
                MagicPermanentState.NoCombatDamage
            ));
            game.doAction(new ChangeLifeAction(game.getDefendingPlayer(), -2));
        }
    }
]
