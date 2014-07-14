[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                MagicTargetChoice.TARGET_CREATURE_CARD_FROM_GRAVEYARD,
                MagicGraveyardTargetPicker.PutOntoBattlefield,
                this,
                "PN returns target creature card\$ from his or her graveyard to the battlefield. "+
                "PN loses life equal to that card's converted mana cost."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game, {
                final MagicCard card ->
                final MagicPlayer player = event.getPlayer()
                game.doAction(new MagicReanimateAction(card,player));
                game.doAction(new MagicChangeLifeAction(player,-card.getConvertedCost()));
            });
        }
    }
]
