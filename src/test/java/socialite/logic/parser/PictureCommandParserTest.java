package socialite.logic.parser;

import org.junit.jupiter.api.Test;

import socialite.commons.core.Messages;
import socialite.logic.commands.PictureCommand;
import socialite.testutil.TypicalIndexes;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the DeleteCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the DeleteCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class PictureCommandParserTest {

    private PictureCommandParser parser = new PictureCommandParser();

    @Test
    public void parse_validArgs_returnsPictureCommand() {
        CommandParserTestUtil.assertParseSuccess(
                parser, "1", new PictureCommand(TypicalIndexes.INDEX_FIRST_PERSON, true, null));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        CommandParserTestUtil.assertParseFailure(
                parser, "a", String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, PictureCommand.MESSAGE_HELP_GUIDE)
        );
    }
}
