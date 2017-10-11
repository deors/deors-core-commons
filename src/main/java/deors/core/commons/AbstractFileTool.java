package deors.core.commons;

import static deors.core.commons.CommonsContext.getMessage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract base class for generic file processing tools.
 *
 * @author deors
 * @version 1.0
 */
public abstract class AbstractFileTool {

    /**
     * The number of errors found while processing files.
     */
    protected int errors;

    /**
     * The list of registered activity listeners.
     */
    protected List<ActivityListener> listeners = new ArrayList<ActivityListener>();

    /**
     * The messages produced while processing files.
     */
    protected List<String> messages = new ArrayList<String>();

    /**
     * The root directory. Files are searched for starting from this directory.
     */
    protected final File rootDir;

    /**
     * Whether to recurse directories when searching for files to process.
     */
    private final boolean recurse;

    /**
     * Whether the process will be executed to files.
     */
    private final boolean applyToFiles;

    /**
     * Whether the process will be executed to directories.
     */
    private final boolean applyToDirectories;

    /**
     * Whether to filter out files by their name.
     */
    private final boolean filter;

    /**
     * The regular expression used to filter out files by their name.
     */
    private final String filterRegex;

    /**
     * The logger. It is protected so it can be overriden by subclasses.
     */
    protected static Logger logger = LoggerFactory.getLogger(AbstractFileTool.class);

    /**
     * The compiled pattern created from the given regular expression.
     */
    private Pattern pattern;

    /**
     * Flag for aborted processes.
     */
    private boolean aborted;

    /**
     * Constructor that sets up the tool execution parameters.
     *
     * @param rootDir the root directory
     * @param recurse whether to recurse directories when searching for files to process
     * @param applyToFiles whether the process will be executed to files
     * @param applyToDirectories whether the process will be executed to directories
     * @param filter whether to filter out files by their name
     * @param filterRegex the regular expression used to filter out files by their name
     */
    public AbstractFileTool(File rootDir, boolean recurse, boolean applyToFiles,
                            boolean applyToDirectories, boolean filter, String filterRegex) {

        super();

        this.rootDir = rootDir;
        this.recurse = recurse;
        this.applyToFiles = applyToFiles;
        this.applyToDirectories = applyToDirectories;
        this.filter = filter;
        this.filterRegex = filterRegex;

        compileRegexPattern();
    }

    /**
     * Adds an activity listener.
     *
     * @param listener the activity listener to register
     */
    public void addActivityListener(ActivityListener listener) {

        listeners.add(listener);
    }

    /**
     * Removes an activity listener.
     *
     * @param listener the activity listener to remove
     */
    public void removeActivityListener(ActivityListener listener) {

        listeners.remove(listener);
    }

    /**
     * Compiles the regular expression pattern (if defined).
     */
    private void compileRegexPattern() {

        if (filterRegex != null && filterRegex.length() != 0) {
            pattern = Pattern.compile(filterRegex);
        }
    }

    /**
     * Whether the given text matches the regular expression pattern (if defined).
     * If the pattern has not been defined, the method returns always <code>true</code>
     * (no files are filtered out).
     *
     * @param text the text to check against the regular expression pattern
     *
     * @return whether the text matches the regular expression pattern
     */
    private boolean matchesRegexPattern(String text) {

        if (pattern == null) {
            return true;
        } else {
            Matcher matcher = pattern.matcher(text);
            return matcher.find();
        }
    }

    /**
     * Executes the process starting in the root directory.
     *
     * @return the number of errors found while processing files
     */
    @SuppressWarnings("PMD.AvoidCatchingThrowable")
    public int doProcess() {

        try {
            if (rootDir.exists() && rootDir.isDirectory()) {
                doPreProcess();
                doProcessInDirectory(rootDir);
                doPostProcess();
            } else {
                logError(getMessage("FTOOL_ERR_STARTING_POINT")); //$NON-NLS-1$
            }
        // CHECKSTYLE:OFF
        } catch (Throwable t) {
        // CHECKSTYLE:ON
            // in the case a tool implementation throws an unchecked exception
            // the process should be flagged as aborted and always return the error count
            aborted = true;
        }

        return errors;
    }

    /**
     * Returns whether debug messages are enabled in the logger.
     *
     * @return whether debug messages are enabled
     */
    protected boolean isDebugEnabled() {

        return logger.isDebugEnabled();
    }

    /**
     * Logs a debug message and calls registered listeners. It assumes that the caller
     * method has already verified that debug is enabled.
     *
     * @param message the message
     */
    protected void logDebug(String message) {

        String fmtMessage = getMessage("FTOOL_DEB_FMT", message); //$NON-NLS-1$

        logger.debug(message);
        messages.add(fmtMessage);

        for (ActivityListener listener : listeners) {
            listener.activityLog(fmtMessage);
        }
    }

    /**
     * Logs a debug message (using a replacement string to format the message)
     * and calls registered listeners. It assumes that the caller
     * method has already verified that debug is enabled.
     *
     * @param message the message
     * @param replacement the replacement string
     *
     * @see StringToolkit#replace(String, String)
     */
    protected void logDebug(String message, String replacement) {

        String formatted = StringToolkit.replace(message, replacement);

        logDebug(formatted);
    }

    /**
     * Logs a debug message (using a list of replacement strings to format the
     * message) and calls registered listeners. It assumes that the caller
     * method has already verified that debug is enabled.
     *
     * @param message the message
     * @param replacements the replacement strings
     *
     * @see StringToolkit#replaceMultiple(String, String[])
     */
    protected void logDebug(String message, String[] replacements) {

        String formatted = StringToolkit.replaceMultiple(message, replacements);

        logDebug(formatted);
    }

    /**
     * Logs an error message and calls registered listeners.
     *
     * @param message the message
     */
    protected void logError(String message) {

        String fmtMessage = getMessage("FTOOL_ERR_FMT", message); //$NON-NLS-1$

        errors++;

        logger.error(message);
        messages.add(fmtMessage);

        for (ActivityListener listener : listeners) {
            listener.activityLog(fmtMessage);
        }
    }

    /**
     * Logs a error message (using a replacement string to format the message)
     * and calls registered listeners.
     *
     * @param message the message
     * @param replacement the replacement string
     *
     * @see StringToolkit#replace(String, String)
     */
    protected void logError(String message, String replacement) {

        String formatted = StringToolkit.replace(message, replacement);

        logError(formatted);
    }

    /**
     * Logs a error message (using a list of replacement strings to format the
     * message) and calls registered listeners.
     *
     * @param message the message
     * @param replacements the replacement strings
     *
     * @see StringToolkit#replaceMultiple(String, String[])
     */
    protected void logError(String message, String[] replacements) {

        String formatted = StringToolkit.replaceMultiple(message, replacements);

        logError(formatted);
    }

    /**
     * Logs an informational message and calls registered listeners.
     *
     * @param message the message
     */
    protected void logInfo(String message) {

        messages.add(message);
        logger.info(message);

        for (ActivityListener listener : listeners) {
            listener.activityLog(message);
        }
    }

    /**
     * Logs an informational message (using a replacement string to format the
     * message) and calls registered listeners.
     *
     * @param message the message
     * @param replacement the replacement string
     *
     * @see StringToolkit#replace(String, String)
     */
    protected void logInfo(String message, String replacement) {

        String formatted = StringToolkit.replace(message, replacement);

        logInfo(formatted);
    }

    /**
     * Logs an informational message (using a list of replacement strings to format
     * the message) and calls registered listeners.
     *
     * @param message the message
     * @param replacements the replacement strings
     *
     * @see StringToolkit#replaceMultiple(String, String[])
     */
    protected void logInfo(String message, String[] replacements) {

        String formatted = StringToolkit.replaceMultiple(message, replacements);

        logInfo(formatted);
    }

    /**
     * Executes the process in the given directory. IIf the applyToDirectories flag
     * is activated the method calls the applyActionsToDirectory(File) over itself.
     * Then the method continue searching for files if the applyToFiles flag is activated.
     * For each file its name is checked against the regular expression pattern (if any)
     * and then executes the applyActionsToFile(File) method. Finally, if the recurse flag
     * is activated, the method starts by searching for subdirectories and recursively
     * calls itself over each subdirectory.
     *
     * @param directory the directory to be processed
     */
    protected void doProcessInDirectory(File directory) {

        if (logger.isDebugEnabled()) {
            logDebug(getMessage("FTOOL_DEB_PROCESSING_DIRECTORY", directory.getAbsolutePath())); //$NON-NLS-1$
        }

        File[] list = directory.listFiles();

        if (recurse) {
            for (File file : list) {
                if (file.isDirectory()) {
                    doProcessInDirectory(file);
                }
            }
        }

        if (applyToFiles) {
            for (File file : list) {
                if (file.isFile()
                    && ((filter && matchesRegexPattern(file.getName())) || !filter)) {
                    if (logger.isDebugEnabled()) {
                        logger.debug(getMessage("FTOOL_DEB_ACTIONS_FILE", file.getAbsolutePath())); //$NON-NLS-1$
                    }
                    applyActionsToFile(file);
                }
            }
        }

        if (applyToDirectories
            && ((filter && matchesRegexPattern(directory.getName())) || !filter)) {
            if (logger.isDebugEnabled()) {
                logger.debug(getMessage("FTOOL_DEB_ACTIONS_DIRECTORY", directory.getAbsolutePath())); //$NON-NLS-1$
            }
            applyActionsToDirectory(directory);
        }
    }

    /**
     * Method used to customize actions to be executed before the process is
     * actually run.
     */
    protected abstract void doPreProcess();

    /**
     * Method used to customize the actions that should be applied to a file.
     *
     * @param file the file to process
     */
    protected abstract void applyActionsToFile(File file);

    /**
     * Method used to customize the actions that should be applied to a directory.
     *
     * @param directory the directory to process
     */
    protected abstract void applyActionsToDirectory(File directory);

    /**
     * Method used to customize actions to be executed after the process is
     * actually run.
     */
    protected abstract void doPostProcess();

    /**
     * Returns the number of errors found while processing files.
     *
     * @return the number of errors found while processing files
     */
    public int getErrors() {

        return errors;
    }

    /**
     * Returns the messages produced while processing files.
     *
     * @return the messages produced while processing files
     */
    public List<String> getMessages() {

        return messages;
    }

    /**
     * Returns the activity listeners registered to this process.
     *
     * @return the activity listeners registered
     */
    public List<ActivityListener> getListeners() {

        return listeners;
    }

    /**
     * Returns the process logger.
     *
     * @return the process logger
     */
    public Logger getLogger() {

        return logger;
    }

    /**
     * Returns whether the tool process was aborted.
     *
     * @return whether the tool process was aborted
     */
    public boolean isAborted() {

        return aborted;
    }
}
