package magic.ui.screen.cardflow;

import java.awt.Color;
import java.text.MessageFormat;
import magic.data.MagicIcon;
import magic.translate.MText;
import magic.ui.ScreenController;
import magic.ui.dialog.prefs.ImageSizePresets;
import magic.ui.screen.HeaderFooterScreen;
import magic.ui.screen.MScreen;
import magic.ui.screen.widget.PlainMenuButton;

@SuppressWarnings("serial")
public class CardFlowScreen extends HeaderFooterScreen
    implements ICardFlowListener {

    private static final Color BACKGROUND_COLOR = new Color(18, 30, 49);

    private final CardFlowLayeredPane layeredPane;
    private final CardFlowPanel cardFlowPanel;
    private final PlainMenuButton imageIndexButton = new PlainMenuButton("", null);
    private final ICardFlowProvider provider;
    private final OptionsPanel optionsPanel;
    private final ScreenSettings settings = new ScreenSettings();
    private final FlashTextOverlay flashOverlay = new FlashTextOverlay();
    private final ICardFlowListener listener;

    public CardFlowScreen(ICardFlowProvider provider, ICardFlowListener listener, String screenTitle) {
        super(screenTitle);
        this.provider = provider;
        this.listener = listener;
        optionsPanel = new OptionsPanel(this, settings);
        cardFlowPanel = new CardFlowPanel(provider, settings);
        layeredPane = new CardFlowLayeredPane(cardFlowPanel, flashOverlay);
        initialize();
    }

    public CardFlowScreen(ICardFlowProvider provider, String screenTitle) {
        this(provider, null, screenTitle);
    }

    private void initialize() {

        cardFlowPanel.addListener(this);
        cardFlowPanel.addListener(listener);
        cardFlowPanel.setBackground(BACKGROUND_COLOR);

        setMainContent(layeredPane);

        PlainMenuButton[] btns = new PlainMenuButton[3];
        btns[0] = getScrollBackButton();
        btns[1] = imageIndexButton;
        btns[2] = getScrollForwardsButton();
        addFooterGroup(btns);

        setNewActiveImage(provider.getStartImageIndex());

        setHeaderOptions(optionsPanel);
    }

    private PlainMenuButton getScrollForwardsButton() {
        PlainMenuButton btn = PlainMenuButton.build(this::doScrollForwards,
            MagicIcon.GO_NEXT,
            "Scroll forwards",
            "You can also use the right arrow key or by moving the mouse-wheel back."
        );
        return btn;
    }

    private PlainMenuButton getScrollBackButton() {
        PlainMenuButton btn = PlainMenuButton.build(this::doScrollBack,
            MagicIcon.GO_BACK,
            "Scroll back",
            "You can also use the left arrow key or by moving the mouse-wheel forwards."
        );
        return btn;
    }

    private void doScrollForwards() {
        cardFlowPanel.doClickRight();
    }

    private void doScrollBack() {
        cardFlowPanel.doClickLeft();
    }

    @Override
    public void setNewActiveImage(int activeImageIndex) {
        imageIndexButton.setText(
            MessageFormat.format("{0,number,integer} of {1,number,integer}",
                activeImageIndex + 1,
                provider.getImagesCount()
            )
        );
    }

    @Override
    public void cardFlowClicked() {
        ScreenController.closeActiveScreen();
    }

    void flashImageSizePreset(ImageSizePresets preset) {
        flashOverlay.flashText(preset.name().replaceAll("SIZE_", "").replaceAll("x", " x "));
    }

    void setAnimateSetting(boolean b) {
        cardFlowPanel.setAnimation(b);
        flashOverlay.flashText(b ? MText.get("On") : MText.get("Off"));
    }

    void setImageSize(ImageSizePresets preset) {
        cardFlowPanel.setImageSize(preset);
        flashImageSizePreset(preset);
    }

    @Override
    public boolean isScreenReadyToClose(MScreen aScreen) {
        if (super.isScreenReadyToClose(aScreen)) {
            optionsPanel.saveSettings();
            return true;
        }
        return false;
    }

}
