[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int x=payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                this,
                "Put an "+x+"/"+x+" green Ooze creature token onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            int x = event.getCardOnStack().getX();
            final MagicPlayTokenAction act = new MagicPlayTokenAction(event.getPlayer(),TokenCardDefinitions.get("green Ooze creature token"))
            final MagicStatic PT = new MagicStatic(MagicLayer.SetPT){
                @Override
                public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
                    pt.set(x,x);
                }
            };
            game.doAction(act);
            game.doAction(new MagicAddStaticAction(act.getPermanent(),PT));
            
        }
    }
]
