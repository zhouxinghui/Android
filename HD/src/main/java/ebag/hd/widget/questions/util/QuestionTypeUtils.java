package ebag.hd.widget.questions.util;

import ebag.core.bean.QuestionBean;


/**
 * Created by zdw on 2017/5/17.
 */

public class QuestionTypeUtils {

    /**
     * 英语：看单词选图
     */
    public static final int QUESTIONS_CHOOSE_PIC_BY_WORD = 1;
    /**
     * 英语：看图选单词
     */
    public static final int QUESTIONS_CHOOSE_WORD_BY_PIC = 2;
    /**
     * 英语：看图写单词
     */
    public static final int QUESTIONS_WRITE_WORD_BY_PIC = 3;
    /**
     * 英语：听录音填空
     */
    public static final int QUESTIONS_COMPLETION_BY_VOICE = 4;
    /**
     * 英语：排列句子
     */
    public static final int QUESTIONS_ORDER_SENTENCE = 5;
    /**
     * 英语：判断题
     */
    public static final int QUESTIONS_JUDGE = 6;
    /**
     * 英语：选择题
     */
    public static final int QUESTIONS_CHOISE = 7;
    /**
     * 英语：听录音选择
     */
    public static final int QUESTIONS_CHOOSE_BY_VOICE = 8;
    /**
     * 英语：连线
     */
    public static final int QUESTIONS_DRAW_LINE = 9;
    /**
     * 英语：分类
     */
    public static final int QUESTIONS_CLASSIFICATION = 10;
    /**
     * 英语：填空题
     */
    public static final int QUESTIONS_COMPLETION = 11;
    /**
     * 英语：朗读作业
     */
    public static final int QUESTIONS_READ_ALOUD = 12;
    /**
     * 英语：看图写作文
     */
    public static final int QUESTIONS_WRITE_COMPOSITION_BY_PIC = 13;
    /**
     * 英语：跟读作业
     */
    public static final int QUESTIONS_FOLLOW_READ = 14;
    /**
     * 数学：计算题
     */
    public static final int QUESTION_MATH_CALCULATION = 15;

    /**
     * 数学：计算题
     */
    public static final int QUESTION_MATH_CALCULATION_1 = 15;

    /**
     * 数学：计算题
     */
    public static final int QUESTION_MATH_CALCULATION_2 = 16;

    /**
     * 数学：计算题
     */
    public static final int QUESTION_MATH_CALCULATION_3 = 17;

    /**
     * 数学：计算题
     */
    public static final int QUESTION_MATH_CALCULATION_4 = 18;
    /**
     * 语文：词组或句子
     */
    public static final int QUESTIONS_CHINESE_SENTENCE = 19;
    /**
     * 语文：听写
     */
    public static final int QUESTIONS_CHINESE_WRITE_BY_VOICE = 20;
    /**
     * 语文：阅读理解
     */
    public static final int QUESTIONS_CHINESE_READ_UNDERSTAND = 21;
    /**
     * 数学：应用题
     */
    public static final int QUESTION_MATH_APPLICATION = 22;
    /**
     * 英语：书写作业
     */
    public static final int QUESTIONS_WRITE = 23;
    /**
     * 没有匹配上的题型
     */
    public static final int QUESTION_NONE = -1;


    private QuestionTypeUtils(){}

    public static String getTitle(String type) {
        switch (type) {
			case "dx": return "选择题";//
            case "pd": return "判断题";//
            case "tk": return "填空题";//
            case "yd": return "阅读理解";//
            case "yy": return "应用题";//
            case "zw": return "作文";//
            case "tx": return "听写题";
            case "1": return "看单词选图";//
            case "2": return "看图选单词";//
            case "3": return "看图写单词";//
            case "4": return "听录音填空";//
            case "5": return "排列句子";//
            case "8": return "听录音选择";//
            case "9": return "连线";//
            case "10": return "分类";//
            case "12": return "朗读";
            case "14": return "跟读";
            case "15": return "计算题";
            case "16": return "词组或句子";//
            case "21": return "书写";//
            default:return "作业";
        }
    }

    public static int getIntType(QuestionBean baseBean) {
        switch (baseBean.getQuestionType()) {
            case "dx": return QUESTIONS_CHOISE;
            case "pd": return QUESTIONS_JUDGE;
            case "tk": return QUESTIONS_COMPLETION;
            case "yd": return QUESTIONS_CHINESE_READ_UNDERSTAND;
            case "yy": return QUESTION_MATH_APPLICATION;
            case "zw": return QUESTIONS_WRITE_COMPOSITION_BY_PIC;
            case "tx": return QUESTIONS_CHINESE_WRITE_BY_VOICE;
            case "1": return QUESTIONS_CHOOSE_PIC_BY_WORD;
            case "2": return QUESTIONS_CHOOSE_WORD_BY_PIC;
            case "3": return QUESTIONS_WRITE_WORD_BY_PIC;
            case "4": return QUESTIONS_COMPLETION_BY_VOICE;
            case "5": return QUESTIONS_ORDER_SENTENCE;
            case "8": return QUESTIONS_CHOOSE_BY_VOICE;
            case "9": return QUESTIONS_DRAW_LINE;
            case "10": return QUESTIONS_CLASSIFICATION;
            case "12": return QUESTIONS_READ_ALOUD;
            case "14": return QUESTIONS_FOLLOW_READ;
            case "15":
                switch (baseBean.getQuestionTypeSx()){
                    case "17":
                    case "21": return QUESTION_MATH_CALCULATION_1;
                    case "18": return QUESTION_MATH_CALCULATION_2;
                    case "19": return QUESTION_MATH_CALCULATION_3;
                    case "20": return QUESTION_MATH_CALCULATION_4;
                    default: return QUESTION_NONE;
                }
            case "16": return QUESTIONS_CHINESE_SENTENCE;
            case "21": return QUESTIONS_WRITE;
            default: return QUESTION_NONE;
        }
    }
}
