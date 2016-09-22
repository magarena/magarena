[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_SPELL,
                payedCost.getX(),
                this,
                "Counter target spell\$ unless its controller pays {X}, "+
                "where X is the greatest power among creatures PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game, {
                int power = 0;
                CREATURE_YOU_CONTROL.filter(event) each {
                    power = Math.max(power, it.getPower());
                }
                game.logAppendValue(event.getPlayer(), power);
                game.addEvent(new MagicCounterUnlessEvent(
                    event.getSource(),
                    it,
                    MagicManaCost.create(power)
                ));
            });
        }
    }
]
