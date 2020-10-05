package seedu.tr4cker.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.tr4cker.logic.parser.CliSyntax.PREFIX_DEADLINE;
import static seedu.tr4cker.logic.parser.CliSyntax.PREFIX_DELETE_TAG;
import static seedu.tr4cker.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.tr4cker.logic.parser.CliSyntax.PREFIX_NEW_TAG;
import static seedu.tr4cker.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.tr4cker.logic.parser.CliSyntax.PREFIX_TASK_DESCRIPTION;
import static seedu.tr4cker.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.tr4cker.commons.core.index.Index;
import seedu.tr4cker.logic.commands.exceptions.CommandException;
import seedu.tr4cker.model.Model;
import seedu.tr4cker.model.Tr4cker;
import seedu.tr4cker.model.task.NameContainsKeywordsPredicate;
import seedu.tr4cker.model.task.Task;
import seedu.tr4cker.testutil.EditTaskDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final String VALID_NAME_1 = "Amy Bee";
    public static final String VALID_NAME_2 = "Bob Choo";
    public static final String VALID_DEADLINE_1 = "20-Oct-2021 1800";
    public static final String VALID_DEADLINE_2 = "24-Sep-2021 2359";
    public static final int VALID_COMPLETION_STATUS_1 = 0;
    public static final int VALID_COMPLETION_STATUS_2 = 0;
    public static final String VALID_DESCRIPTION_1 = "description 1";
    public static final String VALID_DESCRIPTION_2 = "description 2";
    public static final String VALID_TAG_URGENT = "urgent";
    public static final String VALID_TAG_HELP = "help";
    public static final String VALID_TAG_HOMEWORK = "homework";
    public static final String VALID_TAG_WORK = "work";
    public static final String VALID_TAG_ASSIGNMENT = "assignment";
    public static final String VALID_TAG_MISSION = "mission";

    public static final String NAME_DESC_1 = " " + PREFIX_NAME + VALID_NAME_1;
    public static final String NAME_DESC_2 = " " + PREFIX_NAME + VALID_NAME_2;
    public static final String DEADLINE_DESC_1 = " " + PREFIX_DEADLINE + VALID_DEADLINE_1;
    public static final String DEADLINE_DESC_2 = " " + PREFIX_DEADLINE + VALID_DEADLINE_2;
    public static final String DESCRIPTION_DESC_1 = " " + PREFIX_TASK_DESCRIPTION + VALID_DESCRIPTION_1;
    public static final String DESCRIPTION_DESC_2 = " " + PREFIX_TASK_DESCRIPTION + VALID_DESCRIPTION_2;
    public static final String TAG_DESC_HELP = " " + PREFIX_TAG + VALID_TAG_HELP;
    public static final String TAG_DESC_URGENT = " " + PREFIX_TAG + VALID_TAG_URGENT;

    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + "CS2103&"; // '&' not allowed in names
    public static final String INVALID_DEADLINE_DESC = " " + PREFIX_DEADLINE + "2020-09-a"; // 'a' not allowed
    // in deadlines
    public static final String INVALID_DESCRIPTION_DESC =
            " " + PREFIX_TASK_DESCRIPTION; // empty string not allowed for addresses
    public static final String INVALID_TAG_DESC = " " + PREFIX_TAG + "tag*"; // '*' not allowed in tags
    public static final String INVALID_NEW_TAG = " " + PREFIX_NEW_TAG + "tag*"; // '*' not allowed in tags
    public static final String INVALID_DELETE_TAG = " " + PREFIX_DELETE_TAG + "tag*"; // '*' not allowed in tags

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditCommand.EditTaskDescriptor DESC_1;
    public static final EditCommand.EditTaskDescriptor DESC_2;

    public static final String TAG_NEW_HOMEWORK = " " + PREFIX_NEW_TAG + VALID_TAG_HOMEWORK;
    public static final String TAG_NEW_WORK = " " + PREFIX_NEW_TAG + VALID_TAG_WORK;
    public static final String TAG_DELETE_ASSIGNMENT = " " + PREFIX_DELETE_TAG + VALID_TAG_ASSIGNMENT;
    public static final String TAG_DELETE_MISSION = " " + PREFIX_DELETE_TAG + VALID_TAG_MISSION;
    public static final String INVALID_NO_NEW_TAG = " " + PREFIX_NEW_TAG;
    public static final String INVALID_NO_DELETE_TAG = " " + PREFIX_DELETE_TAG;

    static {
        DESC_1 = new EditTaskDescriptorBuilder().withName(VALID_NAME_1)
                .withDeadline(VALID_DEADLINE_1).withTaskDescription(VALID_DESCRIPTION_1)
                .withTags(VALID_TAG_HELP).build();
        DESC_2 = new EditTaskDescriptorBuilder().withName(VALID_NAME_2)
                .withDeadline(VALID_DEADLINE_2).withTaskDescription(VALID_DESCRIPTION_2)
                .withTags(VALID_TAG_URGENT, VALID_TAG_HELP).build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandResult expectedCommandResult,
            Model expectedModel) {
        try {
            CommandResult result = command.execute(actualModel);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Convenience wrapper to {@link #assertCommandSuccess(Command, Model, CommandResult, Model)}
     * that takes a string {@code expectedMessage}.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
            Model expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage);
        assertCommandSuccess(command, actualModel, expectedCommandResult, expectedModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the Tr4cker, filtered task list and selected task in {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        Tr4cker expectedTr4cker = new Tr4cker(actualModel.getTr4cker());
        List<Task> expectedFilteredList = new ArrayList<>(actualModel.getFilteredTaskList());

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(actualModel));
        assertEquals(expectedTr4cker, actualModel.getTr4cker());
        assertEquals(expectedFilteredList, actualModel.getFilteredTaskList());
    }
    /**
     * Updates {@code model}'s filtered list to show only the task at the given {@code targetIndex} in the
     * {@code model}'s Tr4cker.
     */
    public static void showTaskAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredTaskList().size());

        Task task = model.getFilteredTaskList().get(targetIndex.getZeroBased());
        final String[] splitName = task.getName().taskName.split("\\s+");
        model.updateFilteredTaskList(new NameContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredTaskList().size());
    }

}
