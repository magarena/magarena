[
    new OtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isEnemy(permanent)) ?
                new MagicEvent(
                    permanent,
                    otherPermanent.getController(),
                    new MagicMayChoice(),
                    otherPermanent.getCard(),
                    this,
                    "PN may\$ pay 3 life. If PN does not, put RN onto the battlefield under your control."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new ChangeLifeAction(event.getPlayer(),-3));
            } else {
                game.doAction(new ReanimateAction(event.getRefCard(), event.getPlayer().getOpponent()));
            }
        }
    }
]
