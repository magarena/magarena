[
    new MagicStatic(
        MagicLayer.ModPT,
        MagicTargetFilter.TARGET_ZOMBIE_YOU_CONTROL
    ) {
        @Override
        public void modPowerToughness(
                final MagicPermanent source,
                final MagicPermanent permanent,
                final MagicPowerToughness pt) {
            pt.add(1,1);
        }
        @Override
        public boolean condition(
                final MagicGame game,
                final MagicPermanent source,
                final MagicPermanent target) {
            return source != target;
        }
    },
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "Token"
    ) {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostTapEvent(source, "{2}{B}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.TARGET_CREATURE_CARD_FROM_ALL_GRAVEYARDS,
                // exiling a high cost card is good here
                new MagicGraveyardTargetPicker(true),
                this,
                "Exile target creature card\$ from a graveyard. " +
                "Put a 2/2 black Zombie creature token onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game,new MagicCardAction() {
                public void doAction(final MagicCard card) {
                    final MagicPlayer player=event.getPlayer();
                    game.doAction(new MagicRemoveCardAction(
                        card,
                        MagicLocationType.Graveyard
                    ));
                    game.doAction(new MagicMoveCardAction(
                        card,
                        MagicLocationType.Graveyard,
                        MagicLocationType.Exile
                    ));
                    game.doAction(new MagicPlayTokenAction(
                        player,
                        TokenCardDefinitions.get("Zombie")
                    ));
                }
            });
        }
    }
]
