[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int x=payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                this,
                "Put "+x+" "+x+"/"+x+" green Ooze creature tokens onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            int x = event.getCardOnStack().getX();
            final MagicPlayer player=event.getPlayer();
            final MagicCardDefinition oozeDef = TokenCardDefinitions.get("green Ooze creature token");
            final MagicPlayTokensAction act = new MagicPlayTokensAction(player,oozeDef,x)
            final MagicStatic PT = new MagicStatic(MagicLayer.SetPT){
                    @Override
                    public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
                        pt.set(x,x);
                    }
                };
            game.doAction(act);
            final Collection<MagicPermanent> targets=
                act.getPermanent();
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicAddStaticAction(target,PT));
            }
        }
    }
]
