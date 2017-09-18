package daon.core.util;

import java.util.HashMap;
import java.util.Map;

import daon.core.config.POSTag;

public class Utils {

    /**
     * http://jrgraphix.net/r/Unicode/AC00-D7AF
     */
    //한글 시작 문자
    public final static int KOR_START = 0xAC00;
    //한글 종료 문자
    public final static int KOR_END = 0xD7A3;

    /**
     * http://jrgraphix.net/r/Unicode/3130-318F
     */
    public final static int JAMO_START = 0x3130;

    public final static int JAMO_END = 0x318F;

    public final static int HANJA_START = 0x4E00;
    public final static int HANJA_END = 0x9FFF;

    public final static int INDEX_NOT_FOUND = -1;


    /**
     * 맨앞부터는 0,1,2
     * 맨뒤부터는 -1,-2,-3
     *
     * @param word word
     * @param idx decomposed index
     * @return decompose value
     */
    public static char[] getCharAtDecompose(String word, int idx) {

        if (word == null) return new char[]{};

        int len = word.length();

        //마지막 문자 가져오기
        if (idx <= -1) {
            idx = len + idx;

            if (idx < 0) {
                return new char[]{};
            }
        }

        if (idx >= len) {
            return new char[]{};
        }

        char c = word.charAt(idx);

        char[] lastchar = decompose(c);

        return lastchar;
    }

    public static char[] getFirstChar(String word) {

        return getCharAtDecompose(word, 0);
    }

    public static char[] getLastChar(String word) {

        return getCharAtDecompose(word, -1);
    }

    public static int indexOf(final char[] array, final char valueToFind) {
        if (array == null) {
            return INDEX_NOT_FOUND;
        }

        for (int i = 0; i < array.length; i++) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * @param c decomposed value
     * @param array match value's
     * @param findIdx 찾을 idx. 0 : 초성, 1 : 중성, 2 : 종성
     * @return isMatch
     */
    public static boolean isMatch(char[] c, char[] array, int findIdx) {
        boolean isMatched = false;
        int chk = findIdx < 1 ? 1 : findIdx;
        int len = c.length;

        if (len == 0) {
            return isMatched;
        }

        // 분리 내용이 있는 경우 있을 경우
        if (len > chk) {
            int idx = indexOf(array, c[findIdx]);

            if (idx > -1) {
                isMatched = true;
            }
        }

        return isMatched;
    }

    public static boolean isMatch(char[] c, char[] choseong, char[] jungseong, char[] jongseong) {
        boolean isMatched = false;

        if (isMatch(c, choseong, 0)
                && isMatch(c, jungseong, 1)
                && isMatch(c, jongseong, 2)) {
            isMatched = true;
        }

        return isMatched;
    }

    public static boolean isMatch(char[] c, char[] choseong, char[] jungseong) {
        boolean isMatched = false;

        if (isMatch(c, choseong, 0)
                && isMatch(c, jungseong, 1)) {
            isMatched = true;
        }

        return isMatched;
    }


    public static boolean isMatch(char[] c, char choseong) {
        boolean isMatched = false;

        if (c[0] == choseong) {
            isMatched = true;
        }

        return isMatched;
    }

    public static boolean isMatch(char[] c, char choseong, char jungseong) {
        boolean isMatched = false;

        if ((c[0] == choseong)
                && (c[1] == jungseong)) {
            isMatched = true;
        }

        return isMatched;
    }

    public static boolean startsWith(String word, char[] choseong, char[] jungseong, char[] jongseong) {

        char[] c = getFirstChar(word);

        return isMatch(c, choseong, jungseong, jongseong);
    }

    public static boolean startsWith(String word, char[] choseong, char[] jungseong) {

        char[] c = getFirstChar(word);

        return isMatch(c, choseong, jungseong);
    }

    public static boolean startsWith(String word, String[] prefixes) {
        if (word == null) return false;

        for (final String searchString : prefixes) {
            if (word.startsWith(searchString)) {
                return true;
            }
        }

        return false;
    }

    public static boolean startsWith(String word, String prefix) {
        return startsWith(word, new String[]{prefix});
    }

    public static boolean startsWithChoseong(String word, char[] choseong) {

        char[] c = getFirstChar(word);

        return isMatch(c, choseong, 0);
    }

    public static boolean startsWithChoseong(String word, char choseong) {
        return startsWithChoseong(word, new char[]{choseong});
    }

    public static boolean startsWithChoseong(String word) {
        return startsWithChoseong(word, CHOSEONG);
    }

    public static boolean startsWithJongseong(String word, char[] jongseong) {
        boolean isMatched = false;

        char[] c = getFirstChar(word);

        //종성만 있는 경우
        if (c.length == 1) {
            int idx = indexOf(jongseong, c[0]);

            if (idx > -1) {
                isMatched = true;
            }
        }

        return isMatched;
    }

    public static boolean startsWithJongseong(String word, char jongseong) {
        return startsWithJongseong(word, new char[]{jongseong});
    }

    public static boolean startsWithJongseong(String word) {
        return startsWithJongseong(word, JONGSEONG);
    }


    public static boolean endWith(String word, String[] suffixes) {
        if (word == null) return false;

        for (final String searchString : suffixes) {
            if (word.startsWith(searchString)) {
                return true;
            }
        }

        return false;
    }

    public static boolean endWith(String word, String suffix) {

        return endWith(word, new String[]{suffix});
    }

    public static boolean endWith(String word, char[] choseong, char[] jungseong, char[] jongseong) {

        char[] c = getLastChar(word);

        return isMatch(c, choseong, jungseong, jongseong);
    }

    public static boolean endWith(String word, char[] choseong, char[] jungseong) {

        char[] c = getLastChar(word);

        return isMatch(c, choseong, jungseong);
    }

    public static boolean endWithChoseong(String word, char[] choseong) {
        char[] c = getLastChar(word);

        return isMatch(c, choseong, 0);
    }

    public static boolean endWithChoseong(String word, char choseong) {
        return endWithChoseong(word, new char[]{choseong});
    }

    public static boolean endWithJungseong(String word, char[] jungseong) {
        char[] c = getLastChar(word);

        return isMatch(c, jungseong, 1);
    }

    public static boolean endWithJungseong(String word, char jungseong) {
        return endWithJungseong(word, new char[]{jungseong});
    }

    public static boolean endWithJongseong(String word, char[] jongseong) {
        char[] c = getLastChar(word);

        return isMatch(c, jongseong, 2);
    }

    public static boolean endWithJongseong(String word, char jongseong) {
        return endWithJongseong(word, new char[]{jongseong});
    }

    public static boolean endWithNoJongseong(String word) {

        char[] lc = getLastChar(word);

        return isMatch(lc, NO_JONGSEONG, 2);
    }

    public static boolean isLength(String word, int len) {
        boolean is = false;

        if (word == null) return is;

        if (word.length() == len) {
            is = true;
        }

        return is;
    }

    public static final char EMPTY_JONGSEONG = '\0';

    public static final char[] CHOSEONG = {'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ',
            'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'};

    public static final char[] JUNGSEONG = {'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ', 'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ',
            'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ'};

    public static final char[] JONGSEONG = {EMPTY_JONGSEONG, 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ',
            'ㄿ', 'ㅀ', 'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'};

    public static final char[] NO_JONGSEONG = {EMPTY_JONGSEONG};

    private static final Map<Character, Integer> CHOSEONG_MAP;
    private static final Map<Character, Integer> JUNGSEONG_MAP;
    private static final Map<Character, Integer> JONGSEONG_MAP;

    static {
        CHOSEONG_MAP = new HashMap<Character, Integer>();

        for (int i = 0, len = CHOSEONG.length; i < len; i++) {
            char c = CHOSEONG[i];
            CHOSEONG_MAP.put(c, i);
        }
        JUNGSEONG_MAP = new HashMap<Character, Integer>();

        for (int i = 0, len = JUNGSEONG.length; i < len; i++) {
            char c = JUNGSEONG[i];
            JUNGSEONG_MAP.put(c, i);
        }
        JONGSEONG_MAP = new HashMap<Character, Integer>();

        for (int i = 0, len = JONGSEONG.length; i < len; i++) {
            char c = JONGSEONG[i];
            JONGSEONG_MAP.put(c, i);
        }
    }

    private static final int JUNG_JONG = JUNGSEONG.length * JONGSEONG.length;

    /**
     * 한글 한글자를 초성/중성/종성의 배열로 만들어 반환한다.
     *
     * @param c 분리 할 한글 문자
     * @return 초/중/종성 분리 된 char 배열
     */
    public static char[] decompose(char c) {
        char[] result = null;

        //한글이 아닌 경우 [0]에 그대로 리턴. 자음/모음만 있는 경우도 그대로 리턴
        if (c < KOR_START || c > KOR_END)
            return new char[]{c};

        c -= KOR_START;

        char choseong = CHOSEONG[c / JUNG_JONG];
        c = (char) (c % JUNG_JONG);

        char jungseong = JUNGSEONG[c / JONGSEONG.length];

        char jongseong = JONGSEONG[c % JONGSEONG.length];

        if (jongseong != 0) {
            result = new char[]{choseong, jungseong, jongseong};
        } else {
            result = new char[]{choseong, jungseong, EMPTY_JONGSEONG};
        }
        return result;
    }


    public static char compound(char choseong, char jungseong) {

        return compound(choseong, jungseong, EMPTY_JONGSEONG);
    }

    public static char compound(char choseong, char jungseong, char jongseong) {

        int first = CHOSEONG_MAP.get(choseong);
        int middle = JUNGSEONG_MAP.get(jungseong);
        int last = JONGSEONG_MAP.get(jongseong);

        char c = (char) (KOR_START + (first * JUNG_JONG) + (middle * JONGSEONG.length) + last);
        return c;
    }


    public static long hashCode(String string){

        long h = 98764321261L;
        int l = string.length();
        char[] chars = string.toCharArray();

        for (int i = 0; i < l; i++) {
            h = 31 * h + chars[i];
        }
        return h;

    }

    public static boolean isTag(POSTag a, POSTag b){
        boolean is = false;

        long tagBit = a.getBit();
        // 사전의 tag 정보와 포함여부 tag 의 교집합 구함.
        long result = tagBit & b.getBit();

        if(result > 0){
            is = true;
        }

        return is;
    }

    public static int getSeq(POSTag tag){
        int seq = 0;

        if(tag == POSTag.SN){
            seq = 1;
        }else if(tag == POSTag.SL || tag == POSTag.SH){
            seq = 2;
        }
        return seq;
    }

    public static int getSeq(String tag){

        return getSeq(POSTag.valueOf(tag));
    }

    public static int getIdx(String tag){

        return POSTag.valueOf(tag).getIdx();
    }


}