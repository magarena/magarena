[
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (permanent == creature) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN puts a 4/4 white Angel creature token with " +
                    "flying onto the battlefield tapped and attacking. " +
                    "Exile that token at end of combat."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicCard card = MagicCard.createTokenCard(TokenCardDefinitions.get("Angel4"),player);
            game.doAction(new MagicPlayCardAction(
                card,
                player,
                [MagicPlayMod.TAPPED, MagicPlayMod.ATTACKING, MagicPlayMod.EXILE_AT_END_OF_COMBAT]
            ));
        }
    }
]
