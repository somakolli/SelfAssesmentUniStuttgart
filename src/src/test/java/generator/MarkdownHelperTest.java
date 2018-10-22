package generator;

import org.junit.Assert;
import org.junit.Test;

public class MarkdownHelperTest {
    @Test
    public void markdownToHtmlTest(){
        String markdown = "Emphasis, aka italics, with *asterisks* or _underscores_.\n" +
                "\n" +
                "Strong emphasis, aka bold, with **asterisks** or __underscores__.\n" +
                "\n" +
                "Combined emphasis with **asterisks and _underscores_**.\n" +
                "\n" +
                "Strikethrough uses two tildes. ~~Scratch this.~~";
        String actualHtml = MarkdownHelper.markdownToHtml(markdown);
        String expectedHtml = "<p>Emphasis, aka italics, with <em>asterisks</em> or <em>underscores</em>.</p>\n" +
                "<p>Strong emphasis, aka bold, with <strong>asterisks</strong> or <strong>underscores</strong>.</p>\n" +
                "<p>Combined emphasis with <strong>asterisks and <em>underscores</em></strong>.</p>\n" +
                "<p>Strikethrough uses two tildes. ~~Scratch this.~~</p>\n";
        Assert.assertEquals(expectedHtml, actualHtml);
    }
}
