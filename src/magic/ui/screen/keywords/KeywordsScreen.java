package magic.ui.screen.keywords;

import java.awt.event.KeyEvent;
import magic.data.GeneralConfig;
import magic.data.settings.StringSetting;
import magic.translate.MText;
import magic.ui.ScreenController;
import magic.ui.helpers.KeyEventAction;
import magic.ui.screen.HeaderFooterScreen;
import magic.ui.screen.widget.PlainMenuButton;

@SuppressWarnings("serial")
public class KeywordsScreen extends HeaderFooterScreen {

    // translatable strings
    private static final String _S1 = "Keywords Glossary";

    public KeywordsScreen() {
        super(MText.get(_S1));
        setDefaultProperties();
        setContent();
    }

    private void doSaveSettings() {
        GeneralConfig.set(StringSetting.KEYWORDS_SCREEN, ScreenLayout.getLayout().name());
    }

    private void setContent() {
        setMainContent(ScreenLayout.getLayout() == ScreenLayout.Layout_A
                ? new ContentPanelA()
                : new ContentPanelB()
        );
        clearFooterButtons();
        addToFooter(PlainMenuButton.buildLayoutButton(this::doChangeLayout));
        doSaveSettings();
    }

    private void doChangeLayout() {
        ScreenLayout.setNextLayout();
        setContent();
    }

    private void setDefaultProperties() {
        KeyEventAction.doAction(this, ()->ScreenController.closeActiveScreen())
            .on(0, KeyEvent.VK_K);
    }
}
