[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicTarget target = damage.getTarget();
            return damage.getSource() == permanent &&
                   damage.isCombat() &&
                   target.isPlayer() ?
                new MagicEvent(
                    permanent,
                    permanent.isController(target) ?
                        MagicTargetChoice.TARGET_CREATURE_CARD_FROM_GRAVEYARD :
                        MagicTargetChoice.TARGET_CREATURE_CARD_FROM_OPPONENTS_GRAVEYARD,
                    MagicGraveyardTargetPicker.PutOntoBattlefield,
                    this,
                    "PN may put target creature card from ${target.toString()}'s graveyard\$ onto the battlefield under PN's control."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game, {
                final MagicCard targetCard ->
                game.doAction(new MagicReanimateAction(
                    targetCard,
                    event.getPlayer()
                ));
            });
        }
    }
]
