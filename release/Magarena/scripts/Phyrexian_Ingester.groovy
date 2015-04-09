[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    NegOther(
                        "target nontoken creature", 
                        permanent
                    ),
                ),
                MagicExileTargetPicker.create(),
                this,
                "PN may\$ exile another target nontoken creature\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    game.doAction(new MagicExileLinkAction(
                        event.getPermanent(), 
                        it
                    ));
                });
            }
        }
    },
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final MagicCard card = source.getExiledCard();
            pt.add(card.getPower(),card.getToughness());
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return MagicCondition.HAS_EXILED_CREATURE_CARD.accept(source);
        }
    }
]
