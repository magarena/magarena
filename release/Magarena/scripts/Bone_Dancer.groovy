[
    new AttacksUnblockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (creature == permanent) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(),
                    this,
                    "PN may\$ put the top creature card of defending player's graveyard onto the battlefield under PN's control. "+
                    "If PN does, SN assigns no combat damage this turn."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                final MagicPlayer opponent = event.getPlayer().getOpponent();
                final List<MagicCard> targets = CREATURE_CARD_FROM_GRAVEYARD.filter(opponent);
                if (targets.size() > 0) {
                    final MagicCard card = targets.get(targets.size()-1);
                    game.doAction(new ReanimateAction(
                        card,
                        event.getPlayer()
                    ));
                }
                game.doAction(ChangeStateAction.Set(
                    event.getPermanent(),
                    MagicPermanentState.NoCombatDamage
                ));
            }
        }
    }
]
