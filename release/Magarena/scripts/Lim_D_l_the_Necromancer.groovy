[
    new MagicWhenOtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isEnemy(permanent) &&
                    otherPermanent.isCreature()) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        new MagicPayManaCostChoice(MagicManaCost.create("{1}{B}"))
                    ),
                    otherPermanent.getCard(),
                    this,
                    "You may\$ pay {1}{B}\$. If you do, return RN to the battlefield under your control. " +
                    "If it's a creature, it's a Zombie in addition to its other creature types."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new ReanimateAction(event.getRefCard(), event.getPlayer(), [MagicPlayMod.ZOMBIE]));
            }
        }
    }
]
