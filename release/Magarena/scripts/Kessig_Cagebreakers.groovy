[
    new MagicWhenSelfAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return new MagicEvent(
                permanent,
                this,
                "PN puts a 2/2 green Wolf creature token onto " +
                "the battlefield tapped and attacking for each creature " +
                "card in his or her graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = CREATURE_CARD_FROM_GRAVEYARD.filter(event).size();
            for (int count=amount;count>0;count--) {
                final MagicCard card = MagicCard.createTokenCard(CardDefinitions.getToken("2/2 green Wolf creature token"),player);
                game.doAction(new PlayCardAction(
                    card,
                    player,
                    [MagicPlayMod.TAPPED, MagicPlayMod.ATTACKING]
                ));
            }
        }
    }
]
