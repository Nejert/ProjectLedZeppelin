package com.javarush.kazakov;

import com.javarush.kazakov.entity.Answer;
import com.javarush.kazakov.entity.Quest;
import com.javarush.kazakov.entity.Question;
import com.javarush.kazakov.entity.Result;

import java.util.List;

public class TestQuest {
    public static final String QUEST_NAME = "Test quest";
    public static final String FIRST_QUESTION = "First question";
    public static final String FIRST_QUESTION_TRUE_ANSWER = "First question true answer";
    public static final String FIRST_QUESTION_SECOND_FALSE_ANSWER = "First question second false answer";
    public static final String FIRST_QUESTION_FIRST_FALSE_ANSWER = "First question first false answer";
    public static final String FAIL_FIRST_QUESTION_SECOND_ANSWER = "Fail first question second answer";
    public static final String FAIL_FIRST_QUESTION_FIRST_ANSWER = "Fail first question first answer";
    public static final String SECOND_QUESTION = "Second question";
    public static final String SECOND_QUESTION_TRUE_ANSWER = "Second question true answer";
    public static final String SECOND_QUESTION_SECOND_FALSE_ANSWER = "Second question second false answer";
    public static final String SECOND_QUESTION_FIRST_FALSE_ANSWER = "Second question first false answer";
    public static final String FAIL_SECOND_QUESTION_SECOND_ANSWER = "Fail second question second answer";
    public static final String FAIL_SECOND_QUESTION_FIRST_ANSWER = "Fail second question first answer";
    public static final String THIRD_QUESTION = "Third question";
    public static final String THIRD_QUESTION_TRUE_ANSWER = "Third question true answer";
    public static final String THIRD_QUESTION_SECOND_FALSE_ANSWER = "Third question second false answer";
    public static final String THIRD_QUESTION_FIRST_FALSE_ANSWER = "Third question first false answer";
    public static final String SUCCEED_THIRD_QUESTION = "Succeed third question";
    public static final String FAIL_THIRD_QUESTION_SECOND_ANSWER = "Fail third question second answer";
    public static final String FAIL_THIRD_QUESTION_FIRST_ANSWER = "Fail third question first answer";

    public static Quest getTestQuest() {
        Result falseThirdQuestionFirstAnswerResult = new Result(FAIL_THIRD_QUESTION_FIRST_ANSWER);
        Result falseThirdQuestionSecondAnswerResult = new Result(FAIL_THIRD_QUESTION_SECOND_ANSWER);
        Result trueThirdQuestionResult = new Result(SUCCEED_THIRD_QUESTION);
        Answer thirdQuestionFirstFalseAnswer = new Answer(THIRD_QUESTION_FIRST_FALSE_ANSWER, null, falseThirdQuestionFirstAnswerResult);
        Answer thirdQuestionSecondFalseAnswer = new Answer(THIRD_QUESTION_SECOND_FALSE_ANSWER, null, falseThirdQuestionSecondAnswerResult);
        Answer thirdQuestionTrueAnswer = new Answer(THIRD_QUESTION_TRUE_ANSWER, null, trueThirdQuestionResult);
        List<Answer> thirdAnswers = List.of(thirdQuestionFirstFalseAnswer, thirdQuestionSecondFalseAnswer, thirdQuestionTrueAnswer);
        Question thirdQuestion = new Question(THIRD_QUESTION, thirdAnswers);

        Result falseSecondQuestionFirstAnswerResult = new Result(FAIL_SECOND_QUESTION_FIRST_ANSWER);
        Result falseSecondQuestionSecondAnswerResult = new Result(FAIL_SECOND_QUESTION_SECOND_ANSWER);
        Answer secondQuestionFirstFalseAnswer = new Answer(SECOND_QUESTION_FIRST_FALSE_ANSWER, null, falseSecondQuestionFirstAnswerResult);
        Answer secondQuestionSecondFalseAnswer = new Answer(SECOND_QUESTION_SECOND_FALSE_ANSWER, null, falseSecondQuestionSecondAnswerResult);
        Answer secondQuestionTrueAnswer = new Answer(SECOND_QUESTION_TRUE_ANSWER, thirdQuestion, null);
        List<Answer> secondAnswers = List.of(secondQuestionFirstFalseAnswer, secondQuestionSecondFalseAnswer, secondQuestionTrueAnswer);
        Question secondQuestion = new Question(SECOND_QUESTION, secondAnswers);

        Result falseFirstQuestionFirstAnswerResult = new Result(FAIL_FIRST_QUESTION_FIRST_ANSWER);
        Result falseFirstQuestionSecondAnswerResult = new Result(FAIL_FIRST_QUESTION_SECOND_ANSWER);
        Answer firstQuestionFirstFalseAnswer = new Answer(FIRST_QUESTION_FIRST_FALSE_ANSWER, null, falseFirstQuestionFirstAnswerResult);
        Answer firstQuestionSecondFalseAnswer = new Answer(FIRST_QUESTION_SECOND_FALSE_ANSWER, null, falseFirstQuestionSecondAnswerResult);
        Answer firstQuestionTrueAnswer = new Answer(FIRST_QUESTION_TRUE_ANSWER, secondQuestion, null);

        List<Answer> firstAnswers = List.of(firstQuestionFirstFalseAnswer, firstQuestionSecondFalseAnswer, firstQuestionTrueAnswer);
        Question firstQuestion = new Question(FIRST_QUESTION, firstAnswers);
        return new Quest(QUEST_NAME, firstQuestion);
    }
}
