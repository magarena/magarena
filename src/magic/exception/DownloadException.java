package magic.exception;

import magic.data.DownloadableFile;

@SuppressWarnings("serial")
public class DownloadException extends Exception {

    private final DownloadableFile dfile;

    public DownloadException(String message, Throwable cause, final DownloadableFile dfile) {
        super(message, cause);
        this.dfile = dfile;
    }

    public DownloadException(String message, Throwable cause) {
        super(message, cause);
        this.dfile = null;
    }

    public boolean hasFile() {
        return dfile != null;
    }
}
