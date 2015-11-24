[
    new OtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isEnemy(permanent) &&
                    otherPermanent.isCreature() &&
                    otherPermanent.hasCounters(MagicCounterType.MinusOne)) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(),
                    otherPermanent.getCard(),
                    this,
                    "PN may\$ return RN to the battlefield under his or her control."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new ReanimateAction(event.getRefCard(), event.getPlayer()));
            }
        }
    }
]
