[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            game.doAction(new ChangeCountersAction(
                permanent.getController(),
                permanent,
                MagicCounterType.Age,
                1
            ));
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Pay cumulative upkeep?"),
                this,
                "PN may\$ exile one card from the top of his or her library for each Age counter on SN. " +
                "If he or she doesn't, sacrifice SN and exile all cards from PN's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                final MagicCardList topN = event.getPlayer().getLibrary().getCardsFromTop(event.getPermanent().getCounters(MagicCounterType.Age));
                for (final MagicCard card : topN) {
                    game.doAction(new ShiftCardAction(
                        card,
                        MagicLocationType.OwnersLibrary,
                        MagicLocationType.Exile
                    ));
                }
            } else {
                game.doAction(new SacrificeAction(event.getPermanent()));
                for (final MagicCard card : new MagicCardList(event.getPlayer().getLibrary())) {
                    game.doAction(new ShiftCardAction(card, MagicLocationType.OwnersLibrary, MagicLocationType.Exile));
                }
            }
        }
    }
]
