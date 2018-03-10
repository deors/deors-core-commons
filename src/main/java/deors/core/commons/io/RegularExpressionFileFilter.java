package deors.core.commons.io;

import java.io.File;
import java.io.FileFilter;

/**
 * An implementation of <code>java.io.FileFilter</code> interface that filters out all
 * files except those which name matches the regular expression configured in the filter.
 *
 * <p>Directories are always accepted. By default the filter returns all files and directories.
 *
 * @author deors
 * @version 1.0
 */
public final class RegularExpressionFileFilter
    implements FileFilter {

    /**
     * Regular expression used to filter files.
     */
    private final String regexp;

    /**
     * The default regular expression.
     */
    private static final String DEFAULT_REGEXP = ".*"; //$NON-NLS-1$

    /**
     * Creates a file filter. If the regular expression is not configured the default is used and
     * all files are accepted.
     *
     * @see RegularExpressionFileFilter#DEFAULT_REGEXP
     */
    public RegularExpressionFileFilter() {
        super();
        this.regexp = DEFAULT_REGEXP;
    }

    /**
     * Creates a file filter using the given regular expression.
     *
     * @param regexp the regular expression used to filter files
     */
    public RegularExpressionFileFilter(String regexp) {
        super();
        this.regexp = regexp;
    }

    /**
     * Returns <code>true</code> if the given file name matches the configured regular expression.
     *
     * @param f <code>java.io.File</code> object referencing the file
     *
     * @return whether the given file name matches the configured regular expression
     */
    public boolean accept(File f) {

        if (f != null && f.exists()) {
            if (f.isDirectory()) {
                return true;
            }

            return f.getName().matches(regexp);
        }

        return false;
    }
}
