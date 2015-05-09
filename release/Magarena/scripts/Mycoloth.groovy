[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.getCounters(MagicCounterType.PlusOne) > 0 ?
                new MagicEvent(
                    permanent,
                    this,
                    "Put a 1/1 green Saproling creature token onto the battlefield for each +1/+1 counter on SN."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amt = event.getPermanent().getCounters(MagicCounterType.PlusOne);
            game.doAction(new PlayTokensAction(
                event.getPlayer(),
                CardDefinitions.getToken("1/1 green Saproling creature token"),
                amt
            ));
        }
    }
]
