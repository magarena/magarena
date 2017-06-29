[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Remove all counters from all permanents and exile all tokens."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            PERMANENT.filter(event) each {
                if (it.hasCounters()) {
                    for (MagicCounterType type : MagicCounterType.values()) {
                        game.doAction(new ChangeCountersAction(it, type, -it.getCounters(type)));
                    }
                }
                if (it.isToken()) {
                    game.doAction(new RemoveFromPlayAction(it, MagicLocationType.Exile));
                }
            }
        }
    }
]
