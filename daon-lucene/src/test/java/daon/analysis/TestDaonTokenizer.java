package daon.analysis;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.junit.Before;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TestDaonTokenizer extends BaseTokenStreamTestCase {
    private Analyzer analyzer;
    private String input = "하루아침에 되나?";
//    private String input = "우리나라 만세 " + line() + " ee " + line();

    @Before
    public void before() throws IOException {
        analyzer = new DaonAnalyzer();

//        input = getStringFromTestCase();
    }

    private String getStringFromTestCase() throws IOException {
        InputStream input = this.getClass().getResourceAsStream("testcase.txt");


        StringBuilder textBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            String line;
            while ((line = reader.readLine()) != null) {
                textBuilder.append(line);
                textBuilder.append(System.lineSeparator());
            }
        }

        return textBuilder.toString();
    }

    public void test() throws IOException {


        StringBuilder actual = new StringBuilder();

        TokenStream ts = analyzer.tokenStream("bogus", input);
        CharTermAttribute termAtt = ts.addAttribute(CharTermAttribute.class);
        OffsetAttribute offsetAtt = ts.addAttribute(OffsetAttribute.class);

        ts.reset();
        while (ts.incrementToken()) {
            System.out.println(termAtt.toString() + " : " + offsetAtt.startOffset() + "," + offsetAtt.endOffset());
        }
        ts.end();
        ts.close();
    }

    public void testAnalyze() throws IOException {
        String input = "우리나라 만세 " + line() + " ee " + line();

        assertAnalyzesTo(analyzer, input,
                new String[] { "우리나라 만세 ", " ee " },
                new int[] { 0, 10},
                new int[] { 8, 14}
        );
    }

    public String line(){
//        return System.lineSeparator();
        return "\r\n";
    }
}
