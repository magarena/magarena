[
    new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent getEvent(final MagicPermanent permanent) {
            final MagicTargetFilter<MagicCard> targetFilter = new MagicTargetFilter.MagicCMCCardFilter(
                MagicTargetFilter.TARGET_CREATURE_CARD_FROM_GRAVEYARD,
                MagicTargetFilter.Operator.LESS_THAN_OR_EQUAL,
                2
            );

            final MagicTargetChoice targetChoice = new MagicTargetChoice(
                targetFilter,
                true,
                MagicTargetHint.None,
                "target creature card from your graveyard)"
            );

            return new MagicEvent(
                permanent,
                targetChoice,
                new MagicGraveyardTargetPicker(false),
                this,
                "PN returns target creature card\$ with " +
                "converted mana cost 2 or less " +
                "from his or her graveyard to the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game,new MagicCardAction() {
                public void doAction(final MagicCard card) {
                    game.doAction(new MagicReanimateAction(
                        card,
                        event.getPlayer()
                    ));
                }
            });
        }
    }
]
