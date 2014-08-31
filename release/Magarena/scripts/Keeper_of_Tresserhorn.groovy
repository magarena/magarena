[
    new MagicWhenAttacksUnblockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (creature == permanent) ?
                new MagicEvent(
                    permanent,
                    this,
                    "SN assigns no combat damage this turn. Defending player loses 2 life."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final MagicPermanent permanent = event.getPermanent();
                    game.doAction(new MagicChangeLifeAction(event.getPlayer().getOpponent(),-2));
                    game.doAction(MagicChangeStateAction.Set(
                        permanent,
                        MagicPermanentState.NoCombatDamage
                    ));
            }
        }
]
