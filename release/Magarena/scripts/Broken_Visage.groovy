def choice = new MagicTargetChoice("target nonartifact attacking creature");

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                choice,
                MagicDestroyTargetPicker.DestroyNoRegen,
                this,
                "Destroy target nonartifact attacking creature\$. It can't be regenerated. "+
                "PN puts a black Spirit creature token with that creature's power and toughness onto the battlefield. "+
                "Sacrifice the token at the beginning of the next end step."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent creature ->
                final int power = creature.getPower();
                final int toughness = creature.getToughness();
                game.doAction(ChangeStateAction.Set(creature,MagicPermanentState.CannotBeRegenerated));
                game.doAction(new MagicDestroyAction(creature));
                game.doAction(new MagicPlayTokenAction(
                    event.getPlayer(),
                    MagicCardDefinition.create({
                        it.setName("Spirit");
                        it.setFullName("black Spirit creature token");
                        it.setPowerToughness(power,toughness);
                        it.setColors("b");
                        it.addSubType(MagicSubType.Spirit);
                        it.addType(MagicType.Creature);
                        it.setToken();
                        it.setValue(1);
                    }),
                    MagicPlayMod.SACRIFICE_AT_END_OF_TURN
                ));
            });
        }
    }
]
