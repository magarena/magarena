package magic.ui.theme;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import javax.swing.ImageIcon;
import magic.data.GeneralConfig;
import magic.model.MagicRandom;
import magic.ui.ImageFileIO;
import magic.ui.MagicImages;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;
import org.apache.commons.io.FileUtils;

public class AvatarImages {

    private static final AvatarImages INSTANCE = new AvatarImages();

    private final File avatarPath;
    private String current = "";
    private PlayerAvatar[] avatars;

    private AvatarImages() {
        avatarPath = MagicFileSystem.getDataPath(DataPath.AVATARS).toFile();
    }

    private static PlayerAvatar loadAvatar(final File file) {
        return new PlayerAvatar(ImageFileIO.toImg(file, MagicImages.MISSING_BIG));
    }

    private synchronized void loadAvatars() {
        final String avatar=GeneralConfig.getInstance().getAvatar();
        if (!avatar.equals(current)) {
            current=avatar;
            final File[] files=new File(avatarPath,current).listFiles();
            if (files!=null&&files.length>=2) {
                Arrays.sort(files);
                avatars=new PlayerAvatar[files.length];
                for (int index=0;index<files.length;index++) {
                    avatars[index]=loadAvatar(files[index]);
                }
            } else {
                avatars=new PlayerAvatar[2];
                avatars[0]=loadAvatar(new File(""));
                avatars[1]=loadAvatar(new File(""));
            }
        }
    }

    public synchronized ImageIcon getAvatarIcon(int index,final int size) {
        loadAvatars();
        if (index < 0) {
            index = 0;
        } else if (index >= avatars.length) {
            index %= avatars.length;
        }
        return avatars[index].getIcon(size);
    }

    public static AvatarImages getInstance() {
        return INSTANCE;
    }

    public static File getRandomAvatarFile() {
        final Collection<File> files = FileUtils.listFiles(
            MagicFileSystem.getDataPath(DataPath.AVATARS).toFile(),
            new String[]{"png"},
            true
        );
        return files.stream()
            .skip(MagicRandom.nextRNGInt(files.size() - 1))
            .findFirst().orElse(new File(""));
    }
}
