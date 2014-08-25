[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int x=payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                this,
                "Put an ${x}/${x} green Wurm creature token onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int x = event.getCardOnStack().getX();
            game.doAction(new MagicPlayTokenAction(event.getPlayer(), MagicCardDefinition.create({
                it.setName("Wurm");
                it.setFullName("green Wurm creature token");
                it.setPowerToughness(x, x);
                it.setColors("g");
                it.addSubType(MagicSubType.Wurm);
                it.addType(MagicType.Creature);
                it.setToken();
                it.setValue(x);
            })));
        }
    }
]
