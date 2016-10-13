[
    new ThisAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent creature) {
            return new MagicEvent(
                permanent,
                this,
                "PN creates a colorless Construct artifact creature token named Twin that's attacking. "+
                "Its power is equal to SN's power and its toughness is equal to SN's toughness. "+
                "Sacrifice the token at end of combat."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int power = event.getPermanent().getPower();
            final int toughness = event.getPermanent().getToughness();
            game.doAction(new PlayTokenAction(
                event.getPlayer(),
                CardDefinitions.getToken(power, toughness, "colorless Construct artifact creature token named Twin"),
                [MagicPlayMod.ATTACKING, MagicPlayMod.SACRIFICE_AT_END_OF_COMBAT]
            ));
        }
    }
]
