package deors.core.commons;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * An implementation of <code>java.io.FileFilter</code> interface that filters out all
 * files except those which extension is configured.<br>
 *
 * Directories are always accepted. If there is no extension configured the filter only
 * accepts directories.<br>
 *
 * @author deors
 * @version 1.0
 */
public final class ExtensionFileFilter
    implements FileFilter {

    /**
     * Array that contains the file filters.
     */
    private List<String> filters;

    /**
     * Creates a file filter. If no extension is added all files are accepted.
     *
     * @see ExtensionFileFilter#addExtension(String)
     */
    public ExtensionFileFilter() {
        super();
        this.filters = new ArrayList<String>();
    }

    /**
     * Creates a file filter using the extensions contained in the given string array.
     * Note that the <code>.</code> character before the extension is not needed and will be ignored.
     *
     * @param extensions string list that contains the file extensions
     *
     * @see ExtensionFileFilter#addExtension(String)
     */
    public ExtensionFileFilter(List<String> extensions) {
        this();
        for (String extension : extensions) {
            addExtension(extension);
        }
    }

    /**
     * Creates a file filter using the extensions contained in the given string array.
     * Note that the <code>.</code> character before the extension is not needed and will be ignored.
     *
     * @param extensions string array that contains the file extensions
     *
     * @see ExtensionFileFilter#addExtension(String)
     */
    public ExtensionFileFilter(String[] extensions) {
        this();
        for (String extension : extensions) {
            addExtension(extension);
        }
    }

    /**
     * Creates a file filter that accepts files with the given extension. Note that the
     * <code>'.'</code> character before the extension is not needed and will be ignored.
     *
     * @param extension string that contains the file extension
     *
     * @see ExtensionFileFilter#addExtension(String)
     */
    public ExtensionFileFilter(String extension) {
        this();
        addExtension(extension);
    }

    /**
     * Returns <code>true</code> if the given file passes the extension filter or is a directory.
     * Note that files that begin with <code>.</code> character are ignored.
     *
     * @param f <code>java.io.File</code> object referencing the file to check
     *
     * @return whether the given file passes the extension filter
     *
     * @see ExtensionFileFilter#getExtension(java.io.File)
     */
    public boolean accept(File f) {

        if (f != null && f.exists()) {
            if (f.isDirectory()) {
                return true;
            }

            String extension = getExtension(f);

            if (extension != null && filters.contains(extension)) {
                return true;
            }

            if (extension == null && filters.isEmpty()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Adds an extension to the filter extension list. Note that the <code>.</code>
     * character before the extension is not needed and will be ignored.
     *
     * @param extension string containing the extension to add
     */
    public void addExtension(String extension) {

        if (extension.charAt(0) == '.') {
            filters.add(extension.substring(1).toLowerCase(Locale.getDefault()));
        } else {
            filters.add(extension.toLowerCase(Locale.getDefault()));
        }
    }

    /**
     * Returns the extension of the given file.
     *
     * @param f <code>java.io.File</code> object referencing the file
     *
     * @return the file extension without the <code>.</code> character
     *
     * @see java.io.FileFilter#accept(java.io.File)
     */
    public static String getExtension(File f) {

        if (f != null) {
            String filename = f.getName();
            int i = filename.lastIndexOf('.');
            if (i > 0 && i < filename.length() - 1) {
                return filename.substring(i + 1).toLowerCase(Locale.getDefault());
            }
        }

        return null;
    }
}
