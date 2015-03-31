[
    new MagicWhenOtherDrawnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCard card) {
            return (permanent.isEnemy(card) &&
                    permanent.getController().controlsPermanent(MagicColor.Red)) ?
                new MagicEvent(
                    permanent,
                    card.getController(),
                    this,
                    "SN deals 1 damage to PN."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicDealDamageAction(event.getSource(),event.getPlayer(),1));
        }
    }
]
