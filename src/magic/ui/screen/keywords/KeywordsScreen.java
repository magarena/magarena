package magic.ui.screen.keywords;

import java.awt.event.KeyEvent;
import magic.data.GeneralConfig;
import magic.translate.UiString;
import magic.ui.ScreenController;
import magic.ui.helpers.KeyEventAction;
import magic.ui.screen.HeaderFooterScreen;
import magic.ui.screen.widget.MenuButton;

@SuppressWarnings("serial")
public class KeywordsScreen extends HeaderFooterScreen {

    // translatable strings
    private static final String _S1 = "Keywords Glossary";

    public KeywordsScreen() {
        super(UiString.get(_S1));
        setDefaultProperties();
        setContent();
    }

    private void doSaveSettings() {
        final GeneralConfig config = GeneralConfig.getInstance();
        config.setKeywordsSettings(ScreenLayout.getLayout().name());

    }
    
    private void setContent() {
        setMainContent(ScreenLayout.getLayout() == ScreenLayout.Layout1
                ? new KeywordsContentPanel()
                : new KeywordsContentPanel()
        );
        clearFooterButtons();
        addToFooter(MenuButton.buildLayoutButton(this::doChangeLayout));                
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
