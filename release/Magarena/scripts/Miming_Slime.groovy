[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Put an X/X green Ooze creature token onto the battlefield, where X is the greatest power among creatures you control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets = game.filterPermanents(
                event.getPlayer(),
                CREATURE_YOU_CONTROL
            );
            int x = 0;
            for (final MagicPermanent creature : targets) {
                x = Math.max(x,creature.getPower());
            }
            game.doAction(new MagicPlayTokenAction(event.getPlayer(), MagicCardDefinition.create({
                it.setName("Ooze");
                it.setFullName("green Ooze creature token");
                it.setPowerToughness(x, x);
                it.setColors("g");
                it.addSubType(MagicSubType.Ooze);
                it.addType(MagicType.Creature);
                it.setToken();
                it.setValue(x);
            })));
        }
    }
]
