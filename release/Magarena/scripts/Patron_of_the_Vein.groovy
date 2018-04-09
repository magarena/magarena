def filter = MagicTargetFilterFactory.Permanent("Vampire you control")

[
    new OtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent died) {
            final MagicCard diedCard = died.getCard();
            return (died.isEnemy(permanent)) ?
                new MagicEvent(
                    permanent,
                    diedCard,
                    this,
                    "PN exiles RN and puts a +1/+1 counter on each Vampire he or she controls."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCard died = event.getRefCard();
            game.doAction(new ShiftCardAction(
                died,
                MagicLocationType.Graveyard,
                MagicLocationType.Exile
            ));
            filter.filter(event) each {
                game.doAction(new ChangeCountersAction(event.getPlayer(), it, MagicCounterType.PlusOne, 1));
            }
        }
    }
]
