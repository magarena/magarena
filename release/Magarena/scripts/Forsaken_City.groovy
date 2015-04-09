[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return MagicConditionFactory.HandAtLeast(1) ? 
                new MagicEvent(
                    permanent,
                    new MagicMayChoice("Exile a card from your hand?"),
                    this,
                    "PN may exile a card from his or her hand\$. If PN does, untap SN."
                ):
            MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.addEvent(new MagicExileCardEvent(event.getSource(), event.getPlayer(), A_CARD_FROM_HAND));
                game.doAction(new MagicUntapAction(event.getPermanent()));
            }
        }
    }
]
