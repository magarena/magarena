[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicTargetChoice("target nonartifact attacking creature"),
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
                game.doAction(MagicChangeStateAction.Set(creature,MagicPermanentState.CannotBeRegenerated));
                game.doAction(new MagicDestroyAction(creature));
                final MagicPlayTokenAction tokenAction = new MagicPlayTokenAction(
                    event.getPlayer(),
                    TokenCardDefinitions.get("black Spirit creature token"),
                    MagicPlayMod.SACRIFICE_AT_END_OF_TURN
                );
                final MagicStatic PT = new MagicStatic(MagicLayer.SetPT){
                    @Override
                    public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
                        pt.set(power,toughness);
                    }
                };
                game.doAction(tokenAction);
                game.doAction(new MagicAddStaticAction(tokenAction.getPermanent(),PT));
            });
        }
    }
]
//Should set PT before entering the battlefield
