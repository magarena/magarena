[
    new MagicWhenAttacksUnblockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (creature == permanent) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(),
                    this,
                    "PN may\$ put the top creature card of " +
                    "opponent's graveyard onto the " +
                    "battlefield under his or her control. " +
                    "If you do, SN assigns no combat damage this turn."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                final MagicPlayer opponent = event.getPlayer().getOpponent();
                final List<MagicCard> targets =
                        game.filterCards(opponent,CREATURE_CARD_FROM_GRAVEYARD);
                if (targets.size() > 0) {
                    final MagicCard card = targets.get(targets.size()-1);
                    game.doAction(new MagicReanimateAction(
                        card,
                        event.getPlayer()
                     ));
                }
                game.doAction(MagicChangeStateAction.Set(
                    event.getPermanent(),
                    MagicPermanentState.NoCombatDamage
                ));
            }
        }
    }
]
