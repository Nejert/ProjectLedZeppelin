package com.javarush.kazakov.service;


import com.javarush.kazakov.dto.quest.QuestTo;
import com.javarush.kazakov.dto.quest.QuestionTo;
import com.javarush.kazakov.dto.user.UserTo;
import com.javarush.kazakov.entity.quest.Quest;
import com.javarush.kazakov.repository.QuestRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.javarush.kazakov.dto.Dto.DTO;

public class QuestService {
    private final QuestRepository<Quest> questRepository;

    public QuestService() {
        questRepository = new QuestRepository<>(Quest.class);
    }

    public static QuestTo setCurrentQuestion(QuestTo quest, QuestionTo question) {
        return new QuestTo(
                quest.id(),
                quest.title(),
                question,
                quest.author()
        );
    }

    public Map<String, String> getQuestAuthorMap() {
        Map<String, String> questAuthorMap = new HashMap<>();
        getAll().forEach(q -> questAuthorMap.put(q.title(), q.author().login()));
        return questAuthorMap;
    }

    public List<QuestTo> getAll() {
        return questRepository.getAll().stream().map(DTO::from).toList();
    }

    public Optional<QuestTo> get(long id) {
        return questRepository.get(id).map(DTO::from);
    }

    public Optional<QuestTo> get(String name) {
        return questRepository.get(name).map(DTO::from);
    }

    public void create(QuestTo quest) {
        questRepository.save(DTO.from(quest));
    }

    public void create(QuestTo quest, UserTo author) {
        create(quest);
        quest = get(quest.title()).get();
        quest = new QuestTo(
                quest.id(),
                quest.title(),
                quest.currentQuestion(),
                author
        );
        update(quest);
    }


    public void update(QuestTo quest) {
        questRepository.update(DTO.from(quest));
    }

    public void delete(QuestTo quest) {
        questRepository.delete(DTO.from(quest));
    }

    public void delete(long id) {
        questRepository.delete(id);
    }
}
