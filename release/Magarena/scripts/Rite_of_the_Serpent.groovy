[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target creature.\$ If that creature had a +1/+1 counter on it, "+
                "PN puts a 1/1 green Snake creature token onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new DestroyAction(it));
                if (it.hasCounters(MagicCounterType.PlusOne)) {
                    game.doAction(new PlayTokenAction(
                        event.getPlayer(),
                        CardDefinitions.getToken("1/1 green Snake creature token")
                    ));
                }
            })
        }
    }
]
