def choice = Negative("target nonartifact, nonblack creature");

[
    new MagicSpellCardEvent() {  
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {      
        return new MagicEvent(
                cardOnStack,
                choice,
                MagicExileTargetPicker.create(),
                this,
                "Gain control of target nonartifact, nonblack creature\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new GainControlAction(
                    event.getPlayer(),
                    it
                ));
            });
        }
    }
]
