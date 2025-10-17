package com.javarush.kazakov.dto;

import com.javarush.kazakov.dto.quest.AnswerTo;
import com.javarush.kazakov.dto.quest.QuestTo;
import com.javarush.kazakov.dto.quest.QuestionTo;
import com.javarush.kazakov.dto.quest.ResultTo;
import com.javarush.kazakov.dto.user.UserTo;
import com.javarush.kazakov.entity.quest.Answer;
import com.javarush.kazakov.entity.quest.Quest;
import com.javarush.kazakov.entity.quest.Question;
import com.javarush.kazakov.entity.quest.Result;
import com.javarush.kazakov.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface Dto {

    Dto DTO = Mappers.getMapper(Dto.class);

    AnswerTo from(Answer answer);
    Answer from(AnswerTo answerTo);

    QuestionTo from(Question question);
    Question from(QuestionTo questionTo);

    @Mapping(source = "firstQuestion", target = "currentQuestion")
    QuestTo from(Quest quest);
    @Mapping(source = "currentQuestion", target = "firstQuestion")
    Quest from(QuestTo questTo);

    ResultTo from(Result result);
    Result from(ResultTo resultTo);

    UserTo from(User user);
    User from(UserTo user);

}
