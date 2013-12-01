[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Tapping),
        "Tap"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{3}"),
                new MagicTapEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_CREATURE,
                MagicTapTargetPicker.Tap,
                this,
                "Tap target creature\$. Gain control of that creature instead " + 
                "if you control artifacts named Scepter of Empires and Throne of Empires."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicTargetFilter<MagicPermanent> throne = new MagicTargetFilter.NameTargetFilter("Throne of Empires");
            final MagicTargetFilter<MagicPermanent> scepter = new MagicTargetFilter.NameTargetFilter("Scepter of Empires");
            final MagicSource source = event.getSource();
            final MagicPlayer player = source.getController();
            if(player.controlsPermanent(throne) && player.controlsPermanent(scepter)){
                event.processTargetPermanent(game, {
                    final MagicPermanent perm ->
                    game.doAction(new MagicGainControlAction(
                        player,
                        perm
                    ));
                });
            }else{
                event.processTargetPermanent(game, {
                    final MagicPermanent creature ->
                    game.addEvent(new MagicTapEvent(creature));
                });
            }
        }
    }
]
