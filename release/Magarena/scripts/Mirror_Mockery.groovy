[
    new AttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            return attacker == permanent.getEnchantedPermanent() ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(),
                    attacker,
                    this,
                    "PN may\$ put a token onto the battlefield that's a copy of RN. Exile that token at end of combat."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                final MagicPlayer player = event.getPlayer();
                final MagicCard card = MagicCard.createTokenCard(event.getRefPermanent(), player);
                game.doAction(new PlayCardAction(card, player, [MagicPlayMod.EXILE_AT_END_OF_COMBAT]));
            }
        }
    }
]
