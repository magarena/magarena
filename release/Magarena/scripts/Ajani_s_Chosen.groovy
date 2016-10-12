[
    new OtherEntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return otherPermanent.isEnchantment() && otherPermanent.isFriend(permanent) ?
                new MagicEvent(
                    permanent,
                    otherPermanent,
                    this,
                    "PN creates a 2/2 white Cat creature token."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent rn = event.getRefPermanent();
            game.doAction(new PlayTokenAction(
                event.getPlayer(),
                CardDefinitions.getToken("2/2 white Cat creature token"),
                {
                    final MagicPermanent token ->
                    final MagicGame G1 = token.getGame();
                    final MagicPermanent enchantment = rn.map(G1);
                    if (enchantment.isAura()) {
                        G1.addEvent(new MagicEvent(
                            enchantment,
                            new MagicMayChoice("Attach ${enchantment} to ${token}?"),
                            token,
                            {
                                final MagicGame G2, final MagicEvent E ->
                                if (E.isYes()) {
                                    G2.doAction(new AttachAction(
                                        E.getPermanent(),
                                        E.getRefPermanent()
                                    ));
                                }
                            },
                            "PN may\$ attach SN to RN."
                        ));
                    }
                }
            ));
        }
    }
]
