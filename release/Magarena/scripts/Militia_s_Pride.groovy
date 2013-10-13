[
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (creature.isFriend(permanent) && creature.isNonToken()) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        new MagicPayManaCostChoice(MagicManaCost.create("{W}"))
                    ),
                    this,
                    "PN may\$ pay {W}\$. If you do, put a 1/1 " +
                    "white Kithkin Soldier creature token onto the battlefield tapped and attacking."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                final MagicPlayer player=event.getPlayer();
                final MagicCard card=MagicCard.createTokenCard(TokenCardDefinitions.get("Kithkin Soldier"),player);
                game.doAction(new MagicPlayCardAction(
                    card,
                    player,
                    [MagicPlayMod.TAPPED, MagicPlayMod.ATTACKING]
                ));
            }
        }
    }
]
