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
                    "Return target creature card\$ from ${target.toString()}'s graveyard to the battlefield under PN's control."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game,new MagicCardAction() {
                public void doAction(final MagicCard targetCard) {
                    game.doAction(new MagicReanimateAction(
                        targetCard,
                        event.getPlayer()
                    ));
                }
            });
        }
    }
]
