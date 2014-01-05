[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_SPELL,
                this,
                "Counter target spell. Put an X/X green Ooze creature token onto the battlefield, where X is that spell's converted mana cost."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
        event.processTargetCardOnStack(game, {
                final MagicCardOnStack card ->
                game.doAction(new MagicCounterItemOnStackAction(card));
                
            int x = card.getConvertedCost();
            final MagicPlayer player=event.getPlayer();
            final MagicCardDefinition oozeDef = TokenCardDefinitions.get("green Ooze creature token");
            final MagicPlayTokenAction act = new MagicPlayTokenAction(player,oozeDef)
            final MagicStatic PT = new MagicStatic(MagicLayer.SetPT){
                    @Override
                    public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
                        pt.set(x,x);
                    }
                };
            game.doAction(act);
            game.doAction(new MagicAddStaticAction(act.getPermanent(),PT));
        })
        }
    }
]
