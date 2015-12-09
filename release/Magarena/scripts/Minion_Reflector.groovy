[
    new OtherEntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent otherPermanent) {
            return (otherPermanent.isNonToken() &&
                    otherPermanent.hasType(MagicType.Creature)) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        new MagicPayManaCostChoice(MagicManaCost.create("{2}"))
                    ),
                    otherPermanent,
                    this,
                    "PN may\$ pay {2}\$. If PN does, he or she puts a token that's a copy of that creature "+
                    "onto the battlefield. That token has haste and \"At the beginning of the end step, sacrifice this permanent.\""
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new PlayTokenAction(
                    event.getPlayer(),
                    event.getRefPermanent(),
                    [MagicPlayMod.HASTE_UEOT, MagicPlayMod.SACRIFICE_AT_END_OF_TURN]
                ));
            }
        }
    }
]
