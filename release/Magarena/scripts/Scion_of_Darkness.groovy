[
    new SelfCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicTarget target = damage.getTarget();
            return new MagicEvent(
                permanent,
                new MagicMayChoice(permanent.isController(target) ?
                    TARGET_CREATURE_CARD_FROM_GRAVEYARD :
                    TARGET_CREATURE_CARD_FROM_OPPONENTS_GRAVEYARD),
                MagicGraveyardTargetPicker.PutOntoBattlefield,
                this,
                "PN may\$ put target creature card from ${target.toString()}'s graveyard\$ onto the battlefield under PN's control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetCard(game, {
                    game.doAction(new ReanimateAction(
                        it,
                        event.getPlayer()
                    ));
                });
            }
        }
    }
]
