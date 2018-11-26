[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicColorChoice.UNSUMMON_INSTANCE,
                this,
                "PN choose a color\$, " +
                "return all permanents of that color to their owner's hand."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicColor color = event.getChosenColor();
            final MagicPermanentList all = new MagicPermanentList();
            PERMANENT.filter(event) each {
                if (it.hasColor(color)) {
                    all.add(it);
                }
            }
            game.doAction(new RemoveAllFromPlayAction(all, MagicLocationType.OwnersHand));
        }
    }
]
