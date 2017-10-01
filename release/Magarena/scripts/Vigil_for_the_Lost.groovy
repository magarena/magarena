[
    new OtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent died) {
            return died.hasType(MagicType.Creature) && died.isFriend(permanent) ?
                new MagicEvent(
                permanent,
                new MagicMayChoice(
                    new MagicPayManaCostChoice(MagicManaCost.create("{X}"))
                ),
                this,
                "PN may\$ pay {X}\$. If PN does, he or she gains X life."
            ):
            MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new ChangeLifeAction(
                    event.getPlayer(),
                    event.getPaidMana().getX()
                ));
            }
        }
    }
]
